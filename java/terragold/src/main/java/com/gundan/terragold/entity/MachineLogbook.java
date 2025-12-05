package com.gundan.terragold.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "machine_logbooks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineLogbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    private LocalDate logDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private Employee operator;

    private LocalTime startTime;
    private BigDecimal startHourMeter;  // e.g., odometer or hour meter

    private LocalTime endTime;
    private BigDecimal endHourMeter;

    private BigDecimal totalHours; // computed automatically

    private Integer repetitions; // optional, e.g., trips completed by vehicle

    @Column(updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
