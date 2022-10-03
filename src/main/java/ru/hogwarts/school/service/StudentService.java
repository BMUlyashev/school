package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

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
        if (studentRepository.findById(id).isPresent()) {
            return studentRepository.findById(id).get();
        }
        return null;
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getStudentByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(c -> c.getAge() == age)
                .collect(Collectors.toUnmodifiableList());
    }

    public Collection<Student> getStudentByAge() {
        return studentRepository.findAll();
    }
}
