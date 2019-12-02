package pl.weilandt.wms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.weilandt.wms.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNameIgnoreCase(String name);
}
