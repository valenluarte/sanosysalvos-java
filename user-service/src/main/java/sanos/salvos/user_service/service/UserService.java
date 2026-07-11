package sanos.salvos.user_service.service;

import sanos.salvos.user_service.model.User;
import sanos.salvos.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User createUser(User user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email '" + user.getEmail() + "' ya está registrado.");
        }
        return repository.save(user);
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public User update(Long id, User newData) {
        User user = getById(id);

        if (!user.getEmail().equals(newData.getEmail())) {
            repository.findByEmail(newData.getEmail()).ifPresent(existingUser -> {
                throw new IllegalArgumentException("El email '" + newData.getEmail() + "' ya está registrado.");
            });
        }

        user.setName(newData.getName());
        user.setEmail(newData.getEmail());
        user.setPassword(newData.getPassword());
        user.setRole(newData.getRole());

        return repository.save(user);
    }

    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}