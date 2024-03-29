package com.store.service;

import com.store.entity.Product;
import com.store.entity.PromotionalCampaign;
import com.store.exception.CampaignNotFoundException;
import com.store.exception.CampaignStartDateAfterEndDateException;
import com.store.exception.ProductAlreadyPartOfCampaign;
import com.store.payload.request.DiscountProductRequest;
import com.store.repository.PromotionalCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PromotionalCampaignService {

    private final PromotionalCampaignRepository promotionalCampaignRepository;
    private final ProductService productService;

    @Autowired
    public PromotionalCampaignService(PromotionalCampaignRepository promotionalCampaignRepository, ProductService productService) {
        this.promotionalCampaignRepository = promotionalCampaignRepository;
        this.productService = productService;
    }

    @Transactional
    public PromotionalCampaign createPromotionalCampaign(PromotionalCampaign promotionalCampaign) {
        if (promotionalCampaign.getCampaignStart().isAfter(promotionalCampaign.getCampaignEnd())) {
            throw new CampaignStartDateAfterEndDateException("Campaign Start Date must be after the End Date");
        }
        Optional<List<PromotionalCampaign>> overlappingCampaigns = promotionalCampaignRepository
                .findAllByCampaignStartBetweenOrCampaignEndBetween(promotionalCampaign.getCampaignStart(),
                        promotionalCampaign.getCampaignEnd());

        PromotionalCampaign newCampaign = promotionalCampaignRepository.save(promotionalCampaign);
        overlappingCampaigns.ifPresent(newCampaign::setOverlappingCampaigns);

        overlappingCampaigns.ifPresent(campaigns -> campaigns
                .forEach((campaign) -> campaign.addOverlappingCampaign(newCampaign)));

        scheduleStartAndEndOfCampaign(newCampaign);

        promotionalCampaignRepository.saveAndFlush(newCampaign);

        return newCampaign;
    }

    public PromotionalCampaign addProductsToCampaign(String campaignName, @Valid List<DiscountProductRequest> productRequests) {
        PromotionalCampaign campaign = getCampaign(campaignName);

        assignEveryProductToCampaign(productRequests, campaign);

        return promotionalCampaignRepository.save(campaign);
    }

    private void assignEveryProductToCampaign(@Valid List<DiscountProductRequest> productRequests, PromotionalCampaign campaign) {
        for (DiscountProductRequest discountProductRequest : productRequests) {
            Product product = productService.getProductById(discountProductRequest.getId());
            ensureProductIsNotPartOfCampaign(discountProductRequest, product);

            updateDiscountInfoForProduct(discountProductRequest, product);

            campaign.addProduct(product);
        }
    }

    private void updateDiscountInfoForProduct(DiscountProductRequest discountInfo, Product product) {
        product.setWaitingForCampaignStart(true);
        product.setDiscountPercent(discountInfo.getDiscountPercent());
    }

    private void ensureProductIsNotPartOfCampaign(DiscountProductRequest discountInfo, Product product) {
        if (product.isWaitingForCampaignStart()) {
            throw new ProductAlreadyPartOfCampaign("Product with id = " + discountInfo.getId() +
                    " is already part of campaign");
        }
    }

    private PromotionalCampaign getCampaign(String campaignName) {
        return promotionalCampaignRepository.findByName(campaignName)
                .orElseThrow(() -> new CampaignNotFoundException("No campaign with name: " + campaignName));
    }

    public void scheduleStartAndEndOfCampaign(PromotionalCampaign campaign) {
        Duration timeToWaitUntilStart = Duration.between(LocalDateTime.now(), campaign.getCampaignStart());
        Duration timeToWaitUntilEnd = Duration.between(LocalDateTime.now(), campaign.getCampaignEnd());

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.schedule(() -> startCampaign(campaign), timeToWaitUntilStart.getSeconds(), TimeUnit.SECONDS);
        scheduler.schedule(() -> endCampaign(campaign), timeToWaitUntilEnd.getSeconds(), TimeUnit.SECONDS);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void endCampaign(PromotionalCampaign campaign) {
        Set<Product> products = new HashSet<>();

        PromotionalCampaign currentCampaign = promotionalCampaignRepository.findById(campaign.getId()).get();

        for (Product product : currentCampaign.getProducts()) {
            if(product.isOnSale()){
                product.setIsOnSale(false);
                products.add(product);
            }
        }
        productService.addProducts(products);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    protected void startCampaign(PromotionalCampaign campaign) {
        Set<Product> products = new HashSet<>();

        PromotionalCampaign currentCampaign = promotionalCampaignRepository.findById(campaign.getId()).get();

        for (Product product : currentCampaign.getProducts()) {
            if(!product.isOnSale()){
                product.setIsOnSale(true);
                product.setWaitingForCampaignStart(false);
                products.add(product);
            }
        }
        productService.addProducts(products);
    }

    public List<PromotionalCampaign> getAllCampaigns() {
        return promotionalCampaignRepository.findAll();
    }
}
