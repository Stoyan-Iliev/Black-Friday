package com.store.controller;

import com.store.entity.PromotionalCampaign;
import com.store.payload.request.DiscountProductRequest;
import com.store.service.PromotionalCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/blackFriday/api")
public class PromotionalCampaignController {
    private final PromotionalCampaignService promotionalCampaignService;

    @Autowired
    public PromotionalCampaignController(PromotionalCampaignService promotionalCampaignService) {
        this.promotionalCampaignService = promotionalCampaignService;
    }

    @PostMapping(path = "/createCampaign")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<PromotionalCampaign> addPromotionalCampaign(@Valid @RequestBody PromotionalCampaign promotionalCampaign) {
        return ResponseEntity.ok(promotionalCampaignService.createPromotionalCampaign(promotionalCampaign));
    }

    @PutMapping(path = "/addProductsToCampaign")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<PromotionalCampaign> addProductsToCampaign(@RequestParam String name,
                                                                     @Valid @RequestBody List<DiscountProductRequest> discountInfo) {
        return ResponseEntity.ok(promotionalCampaignService.addProductsToCampaign(name, discountInfo));
    }

    @GetMapping(path = "/campaigns")
    public ResponseEntity<List<PromotionalCampaign>> getAllCampaigns(){
        return ResponseEntity.ok(promotionalCampaignService.getAllCampaigns());
    }
}
