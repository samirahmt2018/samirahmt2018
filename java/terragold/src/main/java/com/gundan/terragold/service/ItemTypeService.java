package com.gundan.terragold.service;

import com.gundan.terragold.dto.ItemTypeDto;
import com.gundan.terragold.dto.request.ItemTypeCreateRequest;
import com.gundan.terragold.entity.ItemType;
import com.gundan.terragold.mapper.ItemTypeMapper;
import com.gundan.terragold.repository.ItemTypeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemTypeService {

    private final ItemTypeRepository itemTypeRepo;

    /* ------------------------------------------------------
       CREATE MACHINE
    ------------------------------------------------------- */
    public ItemTypeDto create(@Valid @RequestBody ItemTypeCreateRequest request) {
        ItemType entity = ItemTypeMapper.fromCreateRequest(request);
        ItemType saved = itemTypeRepo.save(entity);

        return ItemTypeMapper.toDto(saved);
    }

    /* ------------------------------------------------------
       RETRIEVE MACHINE BY ID (SHOW ENDPOINT LOGIC)
    ------------------------------------------------------- */
    public ItemTypeDto findById(Long id) {
        ItemType itemType = itemTypeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemType not found with ID: " + id));

        return ItemTypeMapper.toDto(itemType);
    }

    /* ------------------------------------------------------
       EXPOSE REPOSITORY FOR GENERIC LIST SERVICE
    ------------------------------------------------------- */
    public ItemTypeRepository getRepository() {
        return itemTypeRepo;
    }
}