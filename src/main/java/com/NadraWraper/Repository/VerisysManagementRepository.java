package com.NadraWraper.Repository;

import com.NadraWraper.Model.Verisys.VerisysManagementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerisysManagementRepository extends JpaRepository<VerisysManagementModel,String> {
}
