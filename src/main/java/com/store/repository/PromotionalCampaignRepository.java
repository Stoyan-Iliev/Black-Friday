package com.store.repository;

import com.store.entity.PromotionalCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PromotionalCampaignRepository extends JpaRepository<PromotionalCampaign, Long> {

    default Optional<List<PromotionalCampaign>> findAllByCampaignStartBetweenOrCampaignEndBetween(LocalDateTime start, LocalDateTime end){
        return findAllByCampaignStartBetweenOrCampaignEndBetween(start, end, start, end);
    }

     Optional<List<PromotionalCampaign>> findAllByCampaignStartBetweenOrCampaignEndBetween(
            @NotNull(message = "Start must not be empty") LocalDateTime start,
            @NotNull(message = "Start must not be empty") LocalDateTime start2,
            @NotNull(message = "End must not be empty") LocalDateTime end,
            @NotNull(message = "End must not be empty") LocalDateTime end2);

    Optional<PromotionalCampaign> findByName(String name);
}
