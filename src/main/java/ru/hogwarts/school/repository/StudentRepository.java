package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAge(Integer age);

    Collection<Student> findByAgeBetween(Integer min, Integer max);

    Collection<Student> findByAgeGreaterThanEqual(Integer min);

    Collection<Student> findByAgeLessThanEqual(Integer max);

    //    @Query("select f from Faculty f, Student s where s.faculty_id = f.id and s.id = :id")
    @Query(value = "select f from Faculty as f, Student as s where f.id = s.faculty.id and s.id = :id")
    Faculty findFaculty(Long id);

}
