package org.smartshop.smartshop.repository;

import org.smartshop.smartshop.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface ConfigRepository  extends JpaRepository<Config,Long> {

    @Query("select c.tva from Config  c where c.id=1" )
    BigDecimal getTva();
}
