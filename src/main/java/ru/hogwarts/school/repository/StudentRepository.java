package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(Integer age);

    Collection<Student> findByAgeBetween(Integer min, Integer max);

    Collection<Student> findByAgeGreaterThanEqual(Integer min);

    Collection<Student> findByAgeLessThanEqual(Integer max);

}
