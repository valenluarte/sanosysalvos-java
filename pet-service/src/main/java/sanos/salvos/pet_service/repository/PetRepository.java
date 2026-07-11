package sanos.salvos.pet_service.repository;

import sanos.salvos.pet_service.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUserId(Long userId);
    List<Pet> findByStatus(String status);
    List<Pet> findByLocationAddressContaining(String locationAddress);
}