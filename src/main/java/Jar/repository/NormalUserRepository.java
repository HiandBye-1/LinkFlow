package Jar.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Jar.entities.NormalUser;
import jakarta.persistence.LockModeType;

import java.util.Optional;//if no user found

public interface NormalUserRepository extends JpaRepository<NormalUser, Integer> {

    public Optional<NormalUser> findByUsername(String username);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM NormalUser u WHERE u.username = :username")
    public Optional<NormalUser> findByUsernameWithLock(@Param("username") String username);
}