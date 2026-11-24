package Jar.analytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import Jar.analytics.entities.ClickEvent;
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

}
