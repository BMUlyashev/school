package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final FacultyRepository facultyRepository;

    private final RecordMapper recordMapper;
    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public StudentRecord createStudent(StudentRecord studentRecord) {
        Student student = recordMapper.toEntity(studentRecord);
        student.setFaculty(Optional.ofNullable(student.getFaculty())
                .map(Faculty::getId)
                .flatMap(facultyRepository::findById)
                .orElse(null));
        return recordMapper.toRecord(studentRepository.save(student));
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
