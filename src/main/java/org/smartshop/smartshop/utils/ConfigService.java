package org.smartshop.smartshop.utils;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.entity.Config;
import org.smartshop.smartshop.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private  final ConfigRepository configRepository ;


    public  BigDecimal getTva() {
        return configRepository.getTva();
    }

}
