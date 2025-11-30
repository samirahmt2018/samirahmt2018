// 5. InventoryService.java
package com.gundan.terragold.service;

import com.gundan.terragold.entity.*;
import com.gundan.terragold.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryPurchaseRepository purchaseRepo;
    private final InventoryConsumptionRepository consumptionRepo;
    private final ItemTypeRepository itemTypeRepo;
    private final AdvanceAccountService advanceService;
private final MachineRepository machineRepo;
    @Transactional
    public InventoryPurchase purchaseItem(Long itemTypeId,
                                          BigDecimal quantity,
                                          BigDecimal unitCost,
                                          String supplier,
                                          Long purchaserEmployeeId) {
        ItemType item = itemTypeRepo.findById(itemTypeId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        BigDecimal total = unitCost.multiply(quantity);

        // Record expense from advance
        advanceService.spend(purchaserEmployeeId, total,
                "Purchased " + quantity + " " + item.getName() + " from " + supplier, null);
        Employee purchasedBy = Employee.builder().id(purchaserEmployeeId).build();
        InventoryPurchase purchase = InventoryPurchase.builder()
                .purchaseDate(LocalDate.now())
                .itemType(item)
                .quantityPurchased(quantity)
                .unitCost(unitCost)
                .supplierName(supplier)
                .purchasedBy(purchasedBy)
                .build();

        return purchaseRepo.save(purchase);
    }

    @Transactional
    public InventoryConsumption issueItemToMachine(Long itemTypeId,
                                                   BigDecimal quantity,
                                                   Long machineId,
                                                   Long issuedById) {
        Employee issuedBy = Employee.builder().id(issuedById).build();
        return consumptionRepo.save(InventoryConsumption.builder()
                .dateOfIssue(LocalDate.now())
                .itemType(itemTypeRepo.findById(itemTypeId).get())
                .quantityIssued(quantity)
                .machine(machineRepo.findById(machineId).get())
                .issuedBy(issuedBy)
                .build());
    }
}