package com.store.service;

import com.store.entity.BoughtProduct;
import com.store.entity.Product;
import com.store.entity.Purchase;
import com.store.entity.User;
import com.store.exception.UserNotFoundException;
import com.store.payload.request.BoughtProductRequest;
import com.store.payload.request.PurchaseRequest;
import com.store.repository.BoughtProductRepository;
import com.store.repository.PurchaseRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final BoughtProductRepository boughtProductRepository;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, ProductService productService, UserRepository userRepository, BoughtProductRepository boughtProductRepository) {
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.boughtProductRepository = boughtProductRepository;
    }

    public Purchase createPurchase(PurchaseRequest purchase) {
        return purchaseRepository.save(generatePurchase(purchase));
    }

    private Purchase generatePurchase(PurchaseRequest purchaseRequest) {
        Purchase purchase = new Purchase();

        Set<BoughtProduct> products = new HashSet<>();
        for (BoughtProductRequest boughtProductRequest : purchaseRequest.getBoughtProducts()) {
            products.add(generateBoughtProduct(boughtProductRequest));
        }
        purchase.setProducts(new HashSet<>(boughtProductRepository.saveAll(products)));
        purchase.setPurchaseDate(LocalDate.now());
        purchase.setAddress(purchaseRequest.getAddress());
        purchase.setUser(generateUser(purchaseRequest.getUserId()));

        return purchase;
    }

    private User generateUser(long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kvo se opitvash da kupish kat nemash reg we"));
    }

    private BoughtProduct generateBoughtProduct(BoughtProductRequest boughtProductRequest) {
        Product product = productService.getProductById(boughtProductRequest.getId());
        return new BoughtProduct(product,
                boughtProductRequest.getCount(), product.getPrice());
    }
}
