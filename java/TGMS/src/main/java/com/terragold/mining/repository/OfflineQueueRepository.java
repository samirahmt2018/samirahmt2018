package com.terragold.mining.repository;

import com.terragold.mining.entity.OfflineQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
public interface OfflineQueueRepository extends JpaRepository<OfflineQueue, Long> {
    List<OfflineQueue> findByDateBetween(LocalDate start, LocalDate end);
}