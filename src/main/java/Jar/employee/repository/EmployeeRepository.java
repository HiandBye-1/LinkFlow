package Jar.employee.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import Jar.employee.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> { 
    
}
