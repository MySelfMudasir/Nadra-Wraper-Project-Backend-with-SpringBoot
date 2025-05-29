package com.NadraWraper.Repository;

import com.NadraWraper.Model.entity.NadraSchema;
import com.NadraWraper.Model.entity.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRegistrationRepository extends JpaRepository<NadraSchema, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Verisys_Management SET PIN = :pin WHERE CNIC = :cnic", nativeQuery = true)
    void updateAllPinsByCnic(@Param("pin") String pin, @Param("cnic") String cnic);

    @Query(value = "SELECT pin FROM Verisys_Management WHERE cnic = :cnic AND ROWNUM = 1", nativeQuery = true)
    String findPinByCnic(@Param("cnic") String cnic);



}
