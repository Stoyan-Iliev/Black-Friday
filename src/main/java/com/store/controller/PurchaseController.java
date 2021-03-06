package com.store.controller;

import com.store.entity.Purchase;
import com.store.payload.request.PurchaseRequest;
import com.store.payload.response.IncomeResponse;
import com.store.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("blackFriday/api/purchase")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Purchase> buyProducts(@Valid @RequestBody PurchaseRequest purchase){
        return ResponseEntity.ok(purchaseService.createPurchase(purchase));
    }

    @GetMapping(path = "/income")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<IncomeResponse> getIncome(@RequestParam String startDate, @RequestParam String endDate){
        LocalDate startDate1 = LocalDate.parse(startDate);
        LocalDate endDate1 = LocalDate.parse(endDate);

        return ResponseEntity.ok(purchaseService.getIncomeBetween(startDate1, endDate1));
    }
}
