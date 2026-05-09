package sanos.salvos.pet_service.repository;

import sanos.salvos.pet_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}