package com.novianto.challange6.service;

import com.novianto.challange6.dto.MerchantDto;
import com.novianto.challange6.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface MerchantService {

    Page<Merchant> getAllMerchant(Pageable pageable);

    Map<String, Object> saveMerchant(MerchantDto merchantDto);

    Map<String, Object> updateMerchant(UUID idMerchant, MerchantDto merchantDto);

    Map<String, Object> deleteMerchant(UUID idMerchant);

    Map<String, Object> getMerchantById(UUID idMerchant);
    Page<Merchant> getOpenMerchants(Pageable pageable);
    Page<Merchant> getCloseMerchants(Pageable pageable);
}
