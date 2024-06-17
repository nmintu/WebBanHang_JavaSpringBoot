document.addEventListener('DOMContentLoaded', function() {
    const images = document.querySelectorAll('.clickable-image');
    const modal = document.createElement('div');
    modal.id = 'image-modal';
    modal.style.display = 'none';
    modal.style.position = 'fixed';
    modal.style.top = '0';
    modal.style.left = '0';
    modal.style.width = '100%';
    modal.style.height = '100%';
    modal.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    modal.style.display = 'flex';
    modal.style.justifyContent = 'center';
    modal.style.alignItems = 'center';
    modal.style.zIndex = '1000';

    const modalImage = document.createElement('img');
    modalImage.style.maxWidth = '90%';
    modalImage.style.maxHeight = '90%';
    modalImage.style.boxShadow = '0 0 10px white';

    modal.appendChild(modalImage);
    document.body.appendChild(modal);

    images.forEach(image => {
        image.addEventListener('click', function() {
            modalImage.src = this.src;
            modal.style.display = 'flex';
        });
    });

    modal.addEventListener('click', function() {
        modal.style.display = 'none';
    });
});