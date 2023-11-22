package com.novianto.challange6.service.impl;

import com.novianto.challange6.dto.MerchantDto;
import com.novianto.challange6.entity.Merchant;
import com.novianto.challange6.repository.MerchantRepository;
import com.novianto.challange6.service.MerchantService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private Response response;

    @Override
    public Page<Merchant> getAllMerchant(Pageable pageable) {
        return merchantRepository.findAll(pageable);
    }

    @Override
    public Map<String, Object> saveMerchant(MerchantDto merchantDto) {
        Map<String, Object> responseMap = new HashMap<>();

        if (merchantDto == null) {
            return response.errorResponse(ConfigValidation.MERCHANT_DATA_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else if (StringUtils.isBlank(merchantDto.getMerchantName())) {
            return response.errorResponse(ConfigValidation.MERCHANT_NAME_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else if (StringUtils.isBlank(merchantDto.getMerchantLocation())) {
            return response.errorResponse(ConfigValidation.MERCHANT_LOCATION_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        } else if (!response.isValidName(merchantDto.getMerchantName())) {
            return response.errorResponse(ConfigValidation.MERCHANT_NAME_NOT_VALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
        }

        try {
            Merchant merchant = new Merchant();
            merchant.setId(UUID.randomUUID());
            merchant.setMerchantName(merchantDto.getMerchantName());
            merchant.setMerchantLocation(merchantDto.getMerchantLocation());
            merchant.setOpen(true);

            Optional<Merchant> optionalMerchant = Optional.of(merchantRepository.save(merchant));

            responseMap = response.successResponse(optionalMerchant.get());
        } catch (DataAccessException e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }

    @Override
    public Map<String, Object> updateMerchant(UUID idMerchant, MerchantDto merchantDto) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            if (merchantDto == null) {
                return response.errorResponse(ConfigValidation.MERCHANT_DATA_INVALID, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (merchantDto.getMerchantName() != null && merchantDto.getMerchantName().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.MERCHANT_NAME_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            } else if (merchantDto.getMerchantLocation() != null && merchantDto.getMerchantLocation().trim().isEmpty()) {
                return response.errorResponse(ConfigValidation.MERCHANT_LOCATION_EMPTY, ConfigValidation.STATUS_CODE_BAD_REQUEST);
            }

            Optional<Merchant> existingMerchant = merchantRepository.findById(idMerchant);

            if (existingMerchant.isPresent()) {
                Merchant updatedMerchant = existingMerchant.get();

                if (merchantDto.getMerchantName() != null && !merchantDto.getMerchantName().trim().isEmpty()) {
                    updatedMerchant.setMerchantName(merchantDto.getMerchantName());
                }

                if (merchantDto.getMerchantLocation() != null && !merchantDto.getMerchantLocation().trim().isEmpty()) {
                    updatedMerchant.setMerchantLocation(merchantDto.getMerchantLocation());
                }

                updatedMerchant.setOpen(merchantDto.isOpen());
                Merchant savedMerchant = merchantRepository.save(updatedMerchant);
                responseMap = response.successResponse(savedMerchant);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_MERCHANT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }

        return responseMap;
    }



    @Override
    public Map<String, Object> deleteMerchant(UUID idMerchant) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            Optional<Merchant> merchantToDelete = Optional.ofNullable(merchantRepository.getByIdMerchant(idMerchant));

            if (merchantToDelete.isPresent()) {
                Merchant merchant = merchantToDelete.get();
                merchant.setDeleted_date(new Date());
                merchant.setOpen(false);
                merchantRepository.save(merchant);

                responseMap = response.successResponse(ConfigValidation.SUCCESS);
            } else {
                responseMap = response.errorResponse(ConfigValidation.ID_MERCHANT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
            }
        } catch (Exception e) {
            responseMap = response.errorResponse(e.getMessage(), ConfigValidation.STATUS_CODE_INTERNAL_SERVER_ERROR);
        }
        return responseMap;
    }


    @Override
    public Map<String, Object> getMerchantById(UUID idMerchant) {
        Map<String, Object> responseMap = new HashMap<>();

        Optional<Merchant> merchantOptional = Optional.ofNullable(merchantRepository.getByIdMerchant(idMerchant));

        if (merchantOptional.isPresent()) {
            Merchant merchant = merchantOptional.get();
            responseMap = response.successResponse(merchant);
        } else {
            responseMap = response.errorResponse(ConfigValidation.ID_MERCHANT_NOT_FOUND, ConfigValidation.STATUS_CODE_NOT_FOUND);
        }
        return responseMap;
    }

    @Override
    public Page<Merchant> getOpenMerchants(Pageable pageable) {
        return merchantRepository.findOpenMerchants(pageable);
    }

    @Override
    public Page<Merchant> getCloseMerchants(Pageable pageable) {
        return merchantRepository.findClosedMerchants(pageable);
    }
}
