package Jar.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Jar.entities.TempEmails;

public interface TempEmailRepository extends JpaRepository<TempEmails, Integer> {
    
}
