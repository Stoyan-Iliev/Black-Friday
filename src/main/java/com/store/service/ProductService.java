package com.store.service;

import com.store.entity.Product;
import com.store.exception.ProductNotFoundException;
import com.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {

    private Set<String> lastUploadedImageUrls = new HashSet<>();

    private final ProductRepository productRepository;
    private final ImageUploaderService imageUploaderService;

    @Autowired
    public ProductService(ProductRepository productRepository, ImageUploaderService imageUploaderService) {
        this.productRepository = productRepository;
        this.imageUploaderService = imageUploaderService;
    }

    public Product addProduct(Product product, Collection<MultipartFile> images) {
        Product savedProduct = productRepository.save(product);
        return tryToSaveProduct(images, savedProduct);
    }

    private Product tryToSaveProduct(Collection<MultipartFile> images, Product product) {
        try {
            Collection<MultipartFile> imagesToRemove = findImagesToBeRemoved(images, product.getImageUrls());
            images.removeAll(imagesToRemove);
            return productRepository.save(uploadImages(images, product));
        } catch (Exception exception) {
            imageUploaderService.deleteImagesForProduct(lastUploadedImageUrls);
            throw exception;
        }
    }

    private Collection<MultipartFile> findImagesToBeRemoved(Collection<MultipartFile> images,
                                                            Set<String> existingImageUrls) {
        Collection<MultipartFile> imagesToRemove = new HashSet<>();
        if (!isEmpty(images)) {
            for (String url : existingImageUrls) {
                for (MultipartFile image : images) {
                    String imageName = nonNull(image.getOriginalFilename())
                            ? image.getOriginalFilename()
                            : EMPTY;
                    if (url.contains(imageName)) {
                        imagesToRemove.add(image);
                        break;
                    }
                }
            }
        }
        return imagesToRemove;
    }

    private Product uploadImages(Collection<MultipartFile> images, Product product) {
        if (!isEmpty(images)) {
            lastUploadedImageUrls = imageUploaderService.upload(images, product.getName());
            Set<String> existingImageUrls = product.getImageUrls();
            existingImageUrls.addAll(lastUploadedImageUrls);
            product.setImageUrls(existingImageUrls);
        }
        return product;
    }

    public List<Product> addProducts(Collection<Product> products) {
        return productRepository.saveAll(products);
    }

    public Map<String, List<Product>> getAllProducts() {
        List<Product> products = productRepository.getAllByCountGreaterThanOrderByType(0);
        setDiscountPriceForAllProductsOnSale(products);
        Map<String, List<Product>> filteredProducts = new TreeMap<>();
        products.forEach(p -> {
            if (filteredProducts.containsKey(p.getType())) {
                filteredProducts.put(p.getType(), new ArrayList<>());
            }
            filteredProducts.get(p.getType()).add(p);
        });
        return filteredProducts;
    }

    public List<Product> getAllProductsOnSale() {
        List<Product> products = productRepository.getAllByIsOnSaleIsTrue();
        setDiscountPriceForAllProductsOnSale(products);
        return products;
    }

    private void setDiscountPriceForAllProductsOnSale(List<Product> products) {
        if (isNull(products)) {
            return;
        }

        for (Product product : products) {
            setNewPriceIfNeeded(product);
        }
    }

    private boolean isNull(List<Product> products) {
        return products == null;
    }

    private void setNewPriceIfNeeded(Product product) {
        if (product.isOnSale()) {
            product.setPrice(calculateNewPrice(product));
        }
    }

    private BigDecimal calculateNewPrice(Product product) {
        BigDecimal newPrice = calculatePriceWithDiscount(product);
        newPrice = ensurePriceIsNotBelowMinPrice(product, newPrice);
        return newPrice;
    }

    private BigDecimal ensurePriceIsNotBelowMinPrice(Product product, BigDecimal newPrice) {
        if (isFirstLessThanSecond(newPrice, product.getMinPrice())) {
            newPrice = product.getMinPrice();
        }
        return newPrice;
    }

    private boolean isFirstLessThanSecond(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) < 0;
    }

    private BigDecimal calculatePriceWithDiscount(Product product) {
        return product.getPrice()
                .subtract(getDiscountSum(product));
    }

    private BigDecimal getDiscountSum(Product product) {
        return product.getPrice()
                .multiply(BigDecimal.valueOf(product.getDiscountPercent() / 100));
    }

    public Product getProductById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id = " + id + " does not exist"));
    }

    public Product updateProduct(long id, Product product) {
        getProductById(id);
        product.setId(id);
        return productRepository.save(product);
    }
}
