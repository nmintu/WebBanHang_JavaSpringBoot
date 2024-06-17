package com.example.WebBanHang.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không để trống")
    @Size(max = 50, message = "Không được quá 50 ký tự")
    private String name;

    @NotNull(message = "Giá không được để trống")
    private double price;

    @Size(max = 100, message = "Mô tả không quá 100 ký tự")
    private String description;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer quantity;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
