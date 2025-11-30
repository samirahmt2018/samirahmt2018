package com.gundan.terragold.repository;

import com.gundan.terragold.entity.SalaryPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalaryPaymentRepository extends JpaRepository<SalaryPayment, Long> {
}