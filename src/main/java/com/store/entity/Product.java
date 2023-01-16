package com.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.persistence.ElementCollection;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String productCode;

    @Column(unique = true)
    @Size(max = 255, message = "Name must be at most 255 characters long")
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Column()
    @Size(max = 255, message = "Brand must be at most 255 characters long")
    @NotBlank(message = "Brand must not be blank")
    private String Brand;

    @Column()
    @Size(max = 255, message = "Model must be at most 255 characters long")
    @NotBlank(message = "Model must not be blank")
    private String Model;

    @Column
    @Size(max = 255, message = "Type must be at most 255 characters long")
    @NotBlank(message = "Type must not be blank")
    private String type;
    @Column
    @Lob
    private String description;

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
    @JsonIgnoreProperties("products")
    private Set<PromotionalCampaign> campaigns;
    @ElementCollection
    @Column(name = "image_urls", length = 10000)
    private Set<String> imageUrls;

    @Column
    @JsonIgnore
    private boolean isWaitingForCampaignStart;

    public Product() {
        this.campaigns = new HashSet<>();
        this.imageUrls = new HashSet<>();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }
}
