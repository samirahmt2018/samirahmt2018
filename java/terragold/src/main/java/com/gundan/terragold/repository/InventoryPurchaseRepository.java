package com.gundan.terragold.repository;

import com.gundan.terragold.entity.InventoryPurchase;
import com.gundan.terragold.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface InventoryPurchaseRepository extends
        JpaRepository<InventoryPurchase, Long>,
        JpaSpecificationExecutor<InventoryPurchase> {
    List<InventoryPurchase> findByPurchaseDateBetween(LocalDate from, LocalDate to);
    List<InventoryPurchase> findByItemTypeId(Long itemTypeId);
}