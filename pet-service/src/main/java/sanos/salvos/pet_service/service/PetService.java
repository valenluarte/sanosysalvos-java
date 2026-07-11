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
        if (pet.getUserId() == null) {
            throw new IllegalArgumentException("El campo userId es obligatorio");
        }
        return repository.save(pet);
    }

    public Pet getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
    }

    public Pet update(Long id, Pet newData) {
        Pet pet = getById(id);
        pet.setName(newData.getName());
        pet.setType(newData.getType());
        pet.setBreed(newData.getBreed());
        pet.setColor(newData.getColor());
        pet.setStatus(newData.getStatus());
        pet.setLatitude(newData.getLatitude());
        pet.setLongitude(newData.getLongitude());
        pet.setLocationAddress(newData.getLocationAddress());
        return repository.save(pet);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Mascota no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    public List<Pet> getPetsByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public List<Pet> getPetsByStatus(String status) {
        return repository.findByStatus(status);
    }
}