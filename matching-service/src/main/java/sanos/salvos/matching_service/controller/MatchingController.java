package sanos.salvos.matching_service.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sanos.salvos.matching_service.model.PetMatchRequest;
import sanos.salvos.matching_service.model.MatchResponse;
import sanos.salvos.matching_service.service.MatchingService;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService service;

    public MatchingController(MatchingService service) {
        this.service = service;
    }

    @PostMapping
    public MatchResponse match(@Valid @RequestBody PetMatchRequest request) {
        return service.calculateMatch(request);
    }
}