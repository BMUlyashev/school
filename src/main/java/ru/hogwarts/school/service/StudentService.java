package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.findById(student.getId())
                .map(s -> {
                    s.setName(student.getName());
                    s.setAge(student.getAge());
                    return studentRepository.save(s);
                }).orElse(null);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(Integer min, Integer max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    public Collection<Student> findByAgeLessMax(Integer max) {
        return studentRepository.findByAgeLessThanEqual(max);
    }

    public Collection<Student> findByAgeGreaterMin(Integer min) {
        return studentRepository.findByAgeGreaterThanEqual(min);
    }

    public Faculty findFaculty(long id) {
        return studentRepository.findFaculty(id);
    }
}
