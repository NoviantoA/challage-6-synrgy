package com.novianto.challange6.controller;

import com.novianto.challange6.dto.MerchantDto;
import com.novianto.challange6.entity.Merchant;
import com.novianto.challange6.repository.MerchantRepository;
import com.novianto.challange6.service.MerchantService;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private SimpleStringUtil simpleStringUtil;
    @Autowired
    public Response response;

    @PostMapping(value = {"/save", "/save/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> saveMerchant(@RequestBody MerchantDto request) {
        try {
            return new ResponseEntity<Map>(merchantService.saveMerchant(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update/{merchantId}", "/update/{merchantId}/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> updateMerchant(@RequestBody MerchantDto request, @PathVariable("merchantId") UUID merchantId) {
        try {
            if (merchantId == null) {
                return new ResponseEntity<Map>(response.routeNotFound(ConfigValidation.ID_MERCHANT_REQUIRED), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Map>(merchantService.updateMerchant(merchantId, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/delete/{merchantId}", "/delete/{merchantId}/"})
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<Map> deleteMerchant(@PathVariable("merchantId") UUID merchantId) {
        try {
            return new ResponseEntity<Map>(merchantService.deleteMerchant(merchantId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/get/{merchantId}", "/get/{merchantId}/"})
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<Map> getMerchantById(@PathVariable("merchantId") UUID merchantId) {
        try {
            return new ResponseEntity<Map>(response.successResponse(merchantRepository.getByIdMerchant(merchantId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-merchants")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public Page<Merchant> getAllMerchant(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return merchantService.getAllMerchant(pageable);
    }

    @GetMapping("/open")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public Page<Merchant> getAllMerchantOpen(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return merchantService.getOpenMerchants(pageable);
    }

    @GetMapping("/close")
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public Page<Merchant> getAllMerchantClose(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return merchantService.getCloseMerchants(pageable);
    }

    @GetMapping(value = {"/list-spec", "/list-spec/"})
    @PreAuthorize("hasRole('MERCHANT') or hasRole('CUSTOMER')")
    public ResponseEntity<Map> listMerchantHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) String merchantLocation,
            @RequestParam(required = false) Boolean isOpen,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtil.getShort(orderby, ordertype, page, size);
        Specification<Merchant> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (merchantName != null && !merchantName.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("merchantName")), "%" + merchantName.toLowerCase() + "%"));
                    }
                    if (merchantLocation != null && !merchantLocation.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("merchantLocation")), "%" + merchantLocation.toLowerCase() + "%"));
                    }
                    if (isOpen != null) {
                        predicates.add(criteriaBuilder.equal(root.get("isOpen"), isOpen));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });
        Page<Merchant> list = merchantRepository.findAll(spec, show_data);
        return new ResponseEntity<Map>(response.successResponse(list), new HttpHeaders(), HttpStatus.OK);
    }
}
