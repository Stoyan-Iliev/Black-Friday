package com.store.service;

import com.store.entity.BoughtProduct;
import com.store.entity.Product;
import com.store.entity.Purchase;
import com.store.entity.User;
import com.store.exception.UserNotFoundException;
import com.store.payload.request.BoughtProductRequest;
import com.store.payload.request.PurchaseRequest;
import com.store.payload.response.IncomeResponse;
import com.store.repository.BoughtProductRepository;
import com.store.repository.PurchaseRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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

    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        Purchase purchase = new Purchase(purchaseRequest.getAddress(), getUser(purchaseRequest.getUserId()));
        purchase = purchaseRepository.save(purchase);

        Set<BoughtProduct> products = getAllBoughtProducts(purchaseRequest, purchase);
        boughtProductRepository.saveAll(products);

        return purchase;
    }

    private Set<BoughtProduct> getAllBoughtProducts(PurchaseRequest purchaseRequest, Purchase purchase) {
        Set<BoughtProduct> products = new HashSet<>();
        for (BoughtProductRequest boughtProductRequest : purchaseRequest.getBoughtProducts()) {
            BoughtProduct boughtProduct = generateBoughtProduct(boughtProductRequest);
            linkPurchaseToBoughtProduct(purchase, boughtProduct);
            products.add(boughtProduct);
        }
        return products;
    }

    private void linkPurchaseToBoughtProduct(Purchase purchase, BoughtProduct boughtProduct) {
        purchase.addProduct(boughtProduct);
    }

    private User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Not found user with id: " + id));
    }

    private BoughtProduct generateBoughtProduct(BoughtProductRequest boughtProductRequest) {
        Product product = productService.getProductById(boughtProductRequest.getId());
        return new BoughtProduct(product,
                boughtProductRequest.getCount(), product.getPrice());
    }

    public IncomeResponse getIncomeBetween(LocalDate startDate, LocalDate endDate) {
        List<Purchase> purchases = purchaseRepository.findAllByPurchaseDateBetweenOrderByPurchaseDate(startDate, endDate);

        return calculateIncome(startDate, endDate, purchases);
    }

    private IncomeResponse calculateIncome(LocalDate startDate, LocalDate endDate, List<Purchase> purchases) {
        IncomeResponse incomeResponse = new IncomeResponse();

        LocalDate currentDate = startDate;
        int currentPurchaseIndex = 0;

        while (currentDate.isBefore(endDate)){
            BigDecimal dayIncomeSum = BigDecimal.ZERO;

            while (currentPurchaseIndex < purchases.size()) {
                if (!isPurchaseDateEqualToCurrentDate(purchases.get(currentPurchaseIndex), currentDate)) {
                    break;
                }

                dayIncomeSum = getDayIncomeSum(purchases.get(currentPurchaseIndex), dayIncomeSum);
                currentPurchaseIndex++;
            }
            incomeResponse.addDayIncome(currentDate, dayIncomeSum);

            currentDate = currentDate.plusDays(1);
        }
        return incomeResponse;
    }

    private BigDecimal getDayIncomeSum(Purchase purchase,  BigDecimal dayIncomeSum) {
        return dayIncomeSum.add(purchase.getTotalPrice());
    }

    private boolean isPurchaseDateEqualToCurrentDate(Purchase purchase, LocalDate currentDate) {
        return purchase.getPurchaseDate().isEqual(currentDate);
    }
}
