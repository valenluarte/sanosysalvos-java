package sanos.salvos.report_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sanos.salvos.report_service.model.Report;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByPetId(Long petId);
    List<Report> findByUserId(Long userId);
    List<Report> findByType(String type);
}