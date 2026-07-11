package sanos.salvos.report_service.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sanos.salvos.report_service.dto.ReportRequest;
import sanos.salvos.report_service.dto.ReportResponse;
import sanos.salvos.report_service.model.Report;
import sanos.salvos.report_service.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @PostMapping
    public ReportResponse create(@Valid @RequestBody ReportRequest request) {
        return service.process(request);
    }

    @GetMapping
    public List<Report> getAll() {
        return service.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/pet/{petId}")
    public List<Report> getByPetId(@PathVariable Long petId) {
        return service.getReportsByPetId(petId);
    }

    @GetMapping("/user/{userId}")
    public List<Report> getByUserId(@PathVariable Long userId) {
        return service.getReportsByUserId(userId);
    }

    @PutMapping("/{id}")
    public Report update(@PathVariable Long id, @Valid @RequestBody Report report) {
        return service.update(id, report);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}