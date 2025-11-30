package com.terragold.mining.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class OfflineQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // P-1, C-1, A-2

    private String payload; // JSON string of data

    private LocalDateTime queuedAt;

    private boolean synced = false;

    private String syncStatus; // pending, complete, error
}