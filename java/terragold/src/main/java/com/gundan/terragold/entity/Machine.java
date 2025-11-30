package com.gundan.terragold.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "machines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String machineType;
    private String assetMachineId;
    private String description;
    //timestamps createdAt and updatedAt
    @Column(updatable = false)
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
