package com.gundan.terragold.repository;

import com.gundan.terragold.entity.InventoryPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface InventoryPurchaseRepository extends JpaRepository<InventoryPurchase, Long> {
    List<InventoryPurchase> findByPurchaseDateBetween(LocalDate from, LocalDate to);
    List<InventoryPurchase> findByItemTypeId(Long itemTypeId);
}