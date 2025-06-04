package com.NadraWraper.Repository;

import com.NadraWraper.Model.VerisysStatusModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VerisysStatusRepository extends JpaRepository<VerisysStatusModel,String> {
    @Modifying
    @Query(value = "UPDATE verisys_management SET status = :status WHERE cnic = :cnic", nativeQuery = true)
    void updateStatusByCnic(@Param("cnic") String cnic, @Param("status") String status);
}
