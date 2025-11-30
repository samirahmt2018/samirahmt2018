package com.gundan.terragold.entity;

import com.gundan.terragold.enums.UnitOfMeasurement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    @Enumerated(EnumType.STRING)
    private UnitOfMeasurement unitOfMeasurement;

    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
