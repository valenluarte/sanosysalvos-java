package sanos.salvos.pet_service.service;

import sanos.salvos.pet_service.model.Pet;
import sanos.salvos.pet_service.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository repository;

    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public List<Pet> getAll() {
        return repository.findAll();
    }

    public Pet save(Pet pet) {
        return repository.save(pet);
    }

    public Pet getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}