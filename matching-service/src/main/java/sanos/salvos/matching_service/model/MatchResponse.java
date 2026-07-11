package sanos.salvos.matching_service.model;

public class MatchResponse {

    private Double score;
    private String recommendation;

    public MatchResponse() {}

    public MatchResponse(Double score, String recommendation) {
        this.score = score;
        this.recommendation = recommendation;
    }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}