package com.NadraWraper.Repository;

import com.NadraWraper.Model.Verisys.VerisysLogsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerisysLogsRepository extends JpaRepository<VerisysLogsModel, String> {
}
