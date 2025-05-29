package com.NadraWraper.Repository;

import com.NadraWraper.Model.entity.NadraSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.sql.Clob;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NadraRepository extends JpaRepository<NadraSchema, Long> {
    List<NadraSchema> id(long id);


    // Custom query to fetch the next ID
    @Query(value = "SELECT NVL(MAX(ID), 0) + 1 FROM verisys_management", nativeQuery = true)
    Long getNextManagementId();


    //findByRecordType
    @Query(value = "SELECT * FROM verisys_management WHERE RECORD_TYPE = 'primary' ", nativeQuery = true)
    List<NadraSchema> findByRecordTypeNative(String recordType);


    //findBySubAccountId
    @Query(value = "SELECT * FROM verisys_management WHERE SUB_ACCOUNT_ID = ?1", nativeQuery = true)
    List<NadraSchema> findBySubAccountIdNative(Long subAccountId);


    //updateAccountById
    @Modifying
    @Transactional
    @Query("UPDATE NadraSchema u SET u.email = :email, u.accountType = :accountType, u.cnic = :cnic, u.cnicIssueDate = :cnicIssueDate, u.mobile = :mobile, u.recordType = :recordType, u.ntn = :ntn, u.pin = :pin WHERE u.id = :id")
    int updateUserById(Long id, String email, String accountType, String cnic, LocalDate cnicIssueDate, String mobile, String recordType, String ntn, String pin);

    // Sub-records might not have all fields, so we update what’s available
    @Modifying
    @Transactional
    @Query("UPDATE NadraSchema u SET u.cnic = :cnic, u.cnicIssueDate = :cnicIssueDate, u.mobile = :mobile, u.recordType = :recordType, u.ntn = :ntn, u.pin = :pin WHERE u.id = :id")
    int updateSubRecordById(Long id, String cnic, LocalDate cnicIssueDate, String mobile, String recordType, String ntn, String pin);


    //resetStatusByCnic
    @Modifying
    @Transactional
    @Query("UPDATE NadraSchema u SET u.status = :status WHERE u.cnic = :cnic AND u.status = 'A'")
    int resetStatusByCnic(@Param("status") String status, @Param("cnic") String cnic);


    //findByCnic
    @Query(value = "SELECT RESPONSE_TIMESTAMP, NADRA_VERIFY_CITIZEN_RESPONSE FROM VERISY_LOGS WHERE CNIC = ?1", nativeQuery = true)
    List<Object[]> findNadraResponseByCnic(@Param("cnic") String cnic);










}
