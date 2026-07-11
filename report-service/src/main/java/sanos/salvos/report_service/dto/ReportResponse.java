package sanos.salvos.report_service.dto;

public class ReportResponse {

    public Long reportId;
    public String status;
    public MatchResponse matchResult;

    public ReportResponse(Long reportId, String status, MatchResponse matchResult) {
        this.reportId = reportId;
        this.status = status;
        this.matchResult = matchResult;
    }
}