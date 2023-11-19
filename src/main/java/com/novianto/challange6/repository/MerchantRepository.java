package com.novianto.challange6.repository;

import com.novianto.challange6.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {

    @Query(value = "SELECT m FROM Merchant m WHERE m.id = :id")
    public Merchant getByIdMerchant(@Param("id") UUID id);

    @Query(value = "SELECT m FROM merchants m WHERE id = :id", nativeQuery = true)
    public Object getByIdNative(@Param("id") UUID id);

    @Query(value = "select m from Merchant m where m.merchantName like :nameParam")
    public Page<Merchant> getByLikeMerchantName(@Param("nameParam") String nameParam, Pageable pageable);

    @Query(value = "select m from Merchant m ")
    public Page<Merchant> getALlPage(Pageable pageable);

    @Query(value = "SELECT m FROM Merchant m WHERE m.merchantName = :merchantName AND m.merchantLocation = :merchantLocation AND m.open = :open", nativeQuery = false)
    public Merchant findByMerchantNameLocationAndOpenWithQuery(@Param("merchantName") String merchantName, @Param("merchantLocation") String merchantLocation, @Param("open") boolean open);

    Page<Merchant> findByMerchantNameAndMerchantLocationAndOpen(String merchantName, String merchantLocation, boolean open, Pageable pageable);

    @Query("select count(m) from Merchant m where m.merchantName = ?1")
    long countByMerchantName(Double totalPrice);

    @Query("SELECT m FROM Merchant m WHERE m.open = true")
    Page<Merchant> findOpenMerchants(Pageable pageable);

    @Query("SELECT m FROM Merchant m WHERE m.open = false")
    Page<Merchant> findClosedMerchants(Pageable pageable);

    long count();

    @Query("select sum(m.id) from Merchant m")
    long sumMerchant();

    //Store prosedure
    @Query(value = "select * from getMerchant()",nativeQuery = true)
    public List<Object> getListWithStoreProcedure();

    @Modifying
    @Procedure(procedureName = "add_merchant")
    String addMerchant(
            @Param("p_merchant_name") String pMerchantName,
            @Param("p_merchant_location") String pMerchantLocation,
            @Param("p_open") boolean pOpen
    );

    @Modifying
    @Procedure(procedureName = "update_merchant")
    void updateMerchant(
            @Param("p_merchant_id") UUID pMerchantId,
            @Param("p_merchant_name") String pMerchantName,
            @Param("p_merchant_location") String pMerchantLocation,
            @Param("p_open") boolean pOpen
    );

    @Modifying
    @Procedure(procedureName = "delete_merchant")
    void deleteMerchant(@Param("p_merchant_id") UUID pMerchantId);

    @Procedure(procedureName = "get_all_merchant")
    List<Object[]> callGetAllMerchantProcedure();

    @Procedure(procedureName = "get_id_merchant")
    List<Object[]> getMerchantById(@Param("p_merchant_id") UUID pMerchantId);
}
