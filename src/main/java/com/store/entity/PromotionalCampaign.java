package com.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity(name = "promotional_campaigns")
public class PromotionalCampaign {
    @Id
    @GeneratedValue
    private long id;

    @Column
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Column
    @NotNull(message = "Start must not be empty")
    private LocalDateTime campaignStart;

    @Column
    @NotNull(message = "End must not be empty")
    private LocalDateTime campaignEnd;

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="promotional_campaign_id")
    @JsonBackReference
    private PromotionalCampaign campaign;

    @OneToMany(mappedBy = "campaign")
    @JsonManagedReference
    private List<PromotionalCampaign> overlappingCampaigns;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "campaigns_products",
            joinColumns = @JoinColumn(name = "promotional_campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonBackReference
    private Set<Product> products;

    public PromotionalCampaign() {
        overlappingCampaigns = new LinkedList<>();
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

    public LocalDateTime getCampaignStart() {
        return campaignStart;
    }

    public void setCampaignStart(LocalDateTime start) {
        this.campaignStart = start;
    }

    public LocalDateTime getCampaignEnd() {
        return campaignEnd;
    }

    public void setCampaignEnd(LocalDateTime end) {
        this.campaignEnd = end;
    }

    public PromotionalCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(PromotionalCampaign campaign) {
        this.campaign = campaign;
    }

    public List<PromotionalCampaign> getOverlappingCampaigns() {
        return overlappingCampaigns;
    }

    public void setOverlappingCampaigns(List<PromotionalCampaign> overlappingCampaigns) {
        this.overlappingCampaigns = overlappingCampaigns;
    }

    public void addOverlappingCampaign(PromotionalCampaign campaign) {
        overlappingCampaigns.add(campaign);
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        products.add(product);
        product.addCampaign(campaign);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionalCampaign that = (PromotionalCampaign) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(campaignStart, that.campaignStart) &&
                Objects.equals(campaignEnd, that.campaignEnd) &&
                Objects.equals(overlappingCampaigns, that.overlappingCampaigns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, campaignStart, campaignEnd, overlappingCampaigns);
    }
}
