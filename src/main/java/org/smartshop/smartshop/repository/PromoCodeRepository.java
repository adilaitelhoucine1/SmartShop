package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

}