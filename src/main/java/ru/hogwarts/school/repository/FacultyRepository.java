package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findByColor(String color);

//    Collection<Faculty> findByNameLikeIgnoreCaseOrColorLikeIgnoreCase(String name, String color);

    @Query("select f from Faculty f where lower(f.name) = lower(:filter) or lower(f.color) = lower(:filter)")
    Collection<Faculty> findNameOrColor(@Param("filter") String filter);
}
