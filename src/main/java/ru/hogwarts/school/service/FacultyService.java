package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        return faculty.orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.findById(faculty.getId())
                .map(s -> {
                    s.setName(faculty.getName());
                    s.setColor(faculty.getColor());
                    return facultyRepository.save(s);
                }).orElse(null);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }
}
