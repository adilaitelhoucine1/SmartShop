package org.smartshop.smartshop.service.Impl;

import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.repository.ConfigRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements  ConfigService{

    private  final ConfigRepository configRepository ;


    public  BigDecimal getTva() {
        return configRepository.getTva();
    }

}
