package main.repository;

import main.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {
    boolean existsByUserIdentifier(String userIdentifier);
    Optional<UsersEntity> findByCipherEmail(String email);

    @Query(nativeQuery = true,
    value = "SELECT cipher_email FROM users WHERE role_id = 5")
    List<String> selectAdminsEmails();
}
