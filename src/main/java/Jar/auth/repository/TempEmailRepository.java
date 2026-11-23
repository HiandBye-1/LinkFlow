package Jar.auth.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Jar.auth.entities.TempEmails;
import jakarta.persistence.LockModeType;
import java.util.Optional;


public interface TempEmailRepository extends JpaRepository<TempEmails, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TempEmails t WHERE t.userId = :userId")
    Optional<TempEmails> findByUserIdWithLock(@Param("userId") Integer userId);

}
