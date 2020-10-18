package com.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Size(max = 255, message = "Name must be at most 255 characters long")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Column
    @Size(max = 255, message = "Type must be at most 255 characters long")
    @NotBlank(message = "Type must not be blank")
    private String type;

    @Column
    @Min(value = 0, message = "Count must be greater or equal to zero")
    private int count;

    @Column
    @NotNull(message = "Price must not be empty")
    @Min(value = 0, message = "Price must be greater than zero")
    private BigDecimal price;

    @Column
    @NotNull(message = "Minimal price must not be empty")
    @Min(value = 0, message = "Minimal price must be greater than zero")
    private BigDecimal minPrice;

    @Column
    private boolean isOnSale;

    @Column
    @Min(value = 0, message = "Discount percent must be greater or equal to zero")
    private double discountPercent;

    @ManyToMany(mappedBy = "products")
    @JsonManagedReference
    private Set<PromotionalCampaign> campaigns;

    @Column
    @JsonIgnore
    private boolean isWaitingForCampaignStart;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setIsOnSale(boolean isOnSale) {
        this.isOnSale = isOnSale;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Set<PromotionalCampaign> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(Set<PromotionalCampaign> campaigns) {
        this.campaigns = campaigns;
    }

    public void addCampaign(PromotionalCampaign campaign){
        campaigns.add(campaign);
    }

    public boolean isWaitingForCampaignStart() {
        return isWaitingForCampaignStart;
    }

    public void setWaitingForCampaignStart(boolean waitingForCampaignStart) {
        isWaitingForCampaignStart = waitingForCampaignStart;
    }
}
