package com.gundan.terragold.repository;

import com.gundan.terragold.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    Optional<ItemType> findByNameIgnoreCase(String name);
}
