package com.NadraWraper.Repository;

import com.NadraWraper.Model.Verisys.VerisysLogsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerisysLogsRepository extends JpaRepository<VerisysLogsModel, String> {


    VerisysLogsModel findTopByCnicAndApiEndpointOrderByLogIdDesc(String cnic, String apiEndpoint);
}
