package info.podlesov.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import info.podlesov.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByName(String username);
}
