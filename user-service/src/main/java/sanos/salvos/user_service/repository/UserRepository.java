package sanos.salvos.user_service.repository;

import sanos.salvos.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}