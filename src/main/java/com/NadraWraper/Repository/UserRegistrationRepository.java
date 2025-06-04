package com.NadraWraper.Repository;

import com.NadraWraper.Model.entity.UserRegistration;
import com.NadraWraper.Model.entity.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserSchema, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Verisys_Management SET PIN = :pin WHERE CNIC = :cnic", nativeQuery = true)
    void updateAllPinsByCnic(@Param("pin") String pin, @Param("cnic") String cnic);


    @Query(value = "SELECT PIN FROM Verisys_Management WHERE CNIC = :cnic AND ROWNUM=1", nativeQuery = true)
    String findPinByCnic(@Param("cnic") String cnic);


    @Query(value = "SELECT * FROM verisys_management WHERE cnic = :cnic AND ROWNUM = 1", nativeQuery = true)
    UserSchema findByCnic(@Param("cnic") String cnic);

    @Modifying
//    @Transactional
    @Query(value = "UPDATE Verisys_Management SET OTP = :otp WHERE CNIC = :cnic", nativeQuery = true)
    void updateO(@Param("otp") String otp, @Param("cnic") String cnic);

    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM Verisys_Management WHERE CNIC = :cnic", nativeQuery = true)
    List<UserSchema> findAllByCnic(@Param("cnic") String cnic);

    @Modifying
    @Query(value = "UPDATE Verisys_Management SET OTP = :otp, OTP_GENERATED_TIME = :otpTime WHERE CNIC = :cnic", nativeQuery = true)
    void updateOtpAndTime(@Param("otp") String otp, @Param("otpTime") Timestamp otpTime, @Param("cnic") String cnic);

}
