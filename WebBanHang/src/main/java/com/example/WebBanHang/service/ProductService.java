package com.example.WebBanHang.service;

import com.example.WebBanHang.model.Product;
import com.example.WebBanHang.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final String imageDirectory = System.getenv("IMAGE_DIR") != null ? System.getenv("IMAGE_DIR") : "uploads";

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void addProduct(Product product, List<MultipartFile> imageFiles) {
        productRepository.save(product);
        saveImages(product, imageFiles);
    }

    public void updateProduct(Product product, List<MultipartFile> imageFiles, List<String> imagesToDelete) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with Id:" + product.getId() + " does not exist."));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());

        // Delete images if necessary
        deleteImages(existingProduct, imagesToDelete);

        if (!imageFiles.isEmpty()) {
            saveImages(existingProduct, imageFiles);
        }

        productRepository.save(existingProduct);
    }

    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID" + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    private void saveImages(Product product, List<MultipartFile> imageFiles) {
        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String newFileName = System.currentTimeMillis() + "_" + fileName;
                String filePath = Paths.get(imageDirectory, newFileName).toString();
                try {
                    Files.copy(imageFile.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                    product.addImageUrl("/uploads/" + newFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        productRepository.save(product);
    }

    private void deleteImages(Product product, List<String> imagesToDelete) {
        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            List<String> remainingImages = product.getImageUrls().stream()
                    .filter(image -> !imagesToDelete.contains(image))
                    .collect(Collectors.toList());
            product.setImageUrls(remainingImages);
        }
    }
}
