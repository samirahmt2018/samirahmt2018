package com.gundan.terragold.repository;

import com.gundan.terragold.entity.ItemType;
import com.gundan.terragold.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ItemTypeRepository extends
        JpaRepository<ItemType, Long>,
        JpaSpecificationExecutor<ItemType>
{
    Optional<ItemType> findByNameIgnoreCase(String name);
}
