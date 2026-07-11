package sanos.salvos.pet_service.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sanos.salvos.pet_service.model.Pet;
import sanos.salvos.pet_service.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService service;

    public PetController(PetService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pet> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Pet getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Pet> getPetsByUserId(@PathVariable Long userId) {
        return service.getPetsByUserId(userId);
    }

    @GetMapping("/status/{status}")
    public List<Pet> getByStatus(@PathVariable String status) {
        return service.getPetsByStatus(status);
    }

    @PostMapping
    public Pet create(@Valid @RequestBody Pet pet) {
        return service.save(pet);
    }

    @PutMapping("/{id}")
    public Pet update(@PathVariable Long id, @Valid @RequestBody Pet pet) {
        return service.update(id, pet);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}