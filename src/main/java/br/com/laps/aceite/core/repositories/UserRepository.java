package br.com.laps.aceite.core.repositories;

import br.com.laps.aceite.core.models.User;
import br.com.laps.aceite.core.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsBySendEmailTrueAndIdNot(Long id);

    User findNameByEmail(String email);

    @Query(value = "SELECT role FROM users WHERE email = :email LIMIT 1", nativeQuery = true)
    Role findRoleByEmail(@Param("email") String email);

    @Query("SELECT COUNT(u) FROM User u")
    long countAllUsers();

    Optional<User> findBySendEmail(Boolean sendEmail);

    boolean existsBySendEmail(Boolean sendEmail);
}