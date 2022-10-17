package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    FacultyRepository facultyRepository;

    @InjectMocks
    StudentService studentService;

    @Mock
    RecordMapper recordMapper;


    @Test
    public void createStudent() {
        Student student = createStudent(1, "1", 1);
        Faculty faculty = createFaculty(2, "2", "red");
        StudentRecord studentRecord = createStudentRecord(1, "1", 1);

        when(studentRepository.save(any())).thenReturn(student);
//        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        when(recordMapper.toEntity(any(StudentRecord.class))).thenReturn(student);
        when(recordMapper.toRecord(any(Student.class))).thenReturn(studentRecord);
        assertThat(studentService.createStudent(studentRecord)).isEqualTo(studentRecord);
    }

    @Test
    public void findStudent() {
        Student student = createStudent(1, "1", 18);

        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(student));

        Student actual = studentService.findStudent(1);
        assertThat(actual).isEqualTo(student);
    }

    @Test
    public void findStudentNull() {
        Student student = createStudent(1, "1", 18);

        when(studentRepository.findById(any()))
                .thenReturn(Optional.empty());

        Student actual = studentService.findStudent(1);
        assertThat(actual).isNull();
    }

    @Test
    public void editStudent() {
        Student student = createStudent(1, "1", 18);
        Student studentNew = createStudent(1, "2", 20);
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(student))
                .thenReturn(Optional.empty());

        when(studentRepository.save(any(Student.class)))
                .thenReturn(studentNew);

        assertThat(studentService.editStudent(student)).isEqualTo(studentNew);
        assertThat(studentService.editStudent(student)).isNull();
    }

    @Test
    public void deleteStudent() {
        doNothing().when(studentRepository).deleteById(any());
        studentService.deleteStudent(1);
        verify(studentRepository, times(1)).deleteById(any());
    }

    @Test
    public void getByAge() {
        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 18),
                createStudent(5, "5", 18)
        );

        when(studentRepository.findByAge(any()))
                .thenReturn(students)
                .thenReturn(Collections.emptyList());

        assertThat(studentService.getByAge(18))
                .filteredOn(student -> student.getAge() == 18)
                .hasSize(3);
        assertThat(studentService.getByAge(28))
                .filteredOn(student -> student.getAge() == 25)
                .hasSize(0);
    }

    @Test
    public void getAll() {

        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 19),
                createStudent(5, "5", 20)
        );

        when(studentRepository.findAll())
                .thenReturn(students);

        assertThat(studentService.getAll()).hasSize(3);
        assertThat(studentService.getAll())
                .containsExactlyInAnyOrderElementsOf(students);
    }

    @Test
    public void findByAgeBetween() {
        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 19),
                createStudent(5, "5", 20)
        );

        int minAge = 18;
        int maxAge = 20;
        int minAgeFail = 10;
        int maxAgeFail = 13;

        when(studentRepository.findByAgeBetween(any(), any()))
                .thenReturn(students)
                .thenReturn(Collections.emptyList());

        assertThat(studentService.findByAgeBetween(minAge, maxAge))
                .filteredOn(student -> student.getAge() >= minAge && student.getAge() <= maxAge)
                .hasSize(3);
        assertThat(studentService.findByAgeBetween(minAgeFail, maxAgeFail))
                .hasSize(0);
    }

    @Test
    public void findByAgeLessMax() {
        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 19),
                createStudent(5, "5", 20)
        );

        int maxAge = 20;
        int maxAgeFail = 17;

        when(studentRepository.findByAgeLessThanEqual(any()))
                .thenReturn(students)
                .thenReturn(Collections.emptyList());

        assertThat(studentService.findByAgeLessMax(maxAge))
                .filteredOn(student -> student.getAge() <= maxAge)
                .hasSize(3);
        assertThat(studentService.findByAgeLessMax(maxAgeFail))
                .hasSize(0);
    }

    @Test
    public void findByAgeGreaterMin() {
        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 19),
                createStudent(5, "5", 20)
        );

        int minAge = 18;
        int minAgeFail = 22;

        when(studentRepository.findByAgeGreaterThanEqual(any()))
                .thenReturn(students)
                .thenReturn(Collections.emptyList());

        assertThat(studentService.findByAgeGreaterMin(minAge))
                .filteredOn(student -> student.getAge() >= minAge)
                .hasSize(3);
        assertThat(studentService.findByAgeGreaterMin(minAgeFail))
                .hasSize(0);
    }

    @Test
    public void findFaculty() {
        Faculty faculty = createFaculty(1, "1", "red");
        when(studentRepository.findFaculty(any()))
                .thenReturn(faculty);

        assertThat(studentService.findFaculty(1)).isEqualTo(faculty);

    }

    private Student createStudent(long id, String name, int age) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        return student;
    }

    private Faculty createFaculty(long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    private StudentRecord createStudentRecord(long id, String name, int age) {
        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setId(id);
        studentRecord.setName(name);
        studentRecord.setAge(age);
        return studentRecord;
    }
}
