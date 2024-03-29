package com.store.service;

import com.store.entity.BoughtProduct;
import com.store.entity.Product;
import com.store.entity.Purchase;
import com.store.entity.User;
import com.store.exception.NotEnoughProductsException;
import com.store.exception.UserNotFoundException;
import com.store.payload.request.BoughtProductRequest;
import com.store.payload.request.PurchaseRequest;
import com.store.payload.response.IncomeResponse;
import com.store.repository.BoughtProductRepository;
import com.store.repository.PurchaseRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final EmailService emailService;
    private final SimpleMailMessage template;
    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, ProductService productService, UserRepository userRepository, BoughtProductRepository boughtProductRepository, EmailService emailService, SimpleMailMessage template) {
        this.purchaseRepository = purchaseRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.boughtProductRepository = boughtProductRepository;
        this.emailService = emailService;
        this.template = template;
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        Purchase purchase = new Purchase(purchaseRequest.getAddress(), getUser(purchaseRequest.getUserId()));
        purchase = purchaseRepository.save(purchase);

        Set<BoughtProduct> products = getAllBoughtProducts(purchaseRequest, purchase);
        boughtProductRepository.saveAll(products);

        User user = purchase.getUser();
        String text = String.format(template.getText(), user.getFirstName(),
                purchase.getTotalPrice(), purchase.getAddress());
        emailService.sendSimpleMessage(user.getEmail(), "Purchase Finished", text);

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
        Product product = getProduct(boughtProductRequest);
        return new BoughtProduct(product,
                boughtProductRequest.getCount(), product.getPrice());
    }

    private Product getProduct(BoughtProductRequest boughtProductRequest) {
        Product product = productService.getProductById(boughtProductRequest.getId());
        ensureHaveEnoughProducts(boughtProductRequest, product);
        product.setCount(product.getCount() - boughtProductRequest.getCount());
        return product;
    }

    private void ensureHaveEnoughProducts(BoughtProductRequest boughtProductRequest, Product product) {
        if(product.getCount() < boughtProductRequest.getCount()){
            throw new NotEnoughProductsException("We currently have " + product.getCount() + " " + product.getName());
        }
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
