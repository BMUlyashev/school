package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentServiceTest {

    private final StudentService studentService = new StudentService();

    @Test
    public void createStudent() {
        Student studentOne = new Student(1L, "one", 12);
        Student studentTwo = new Student(1L, "two", 15);

        assertThat(studentService.createStudent(studentOne)).isEqualTo(studentOne);
        assertThat(studentService.createStudent(studentTwo)).isEqualTo(studentTwo);

        assertThat(studentService.findStudent(1)).isEqualTo(studentOne);
        assertThat(studentService.findStudent(2)).isEqualTo(studentTwo);
    }

    @Test
    public void findStudent() {
        Student studentOne = new Student(1L, "one", 12);
        assertThat(studentService.createStudent(studentOne)).isEqualTo(studentOne);

        assertThat(studentService.findStudent(1)).isEqualTo(studentOne);
        assertThat(studentService.findStudent(2)).isNull();
    }

    @Test
    public void editStudentPositive() {
        Student studentOne = new Student(1L, "one", 12);
        Student studentTwo = new Student(1L, "two", 14);
        studentService.createStudent(studentOne);

        assertThat(studentService.findStudent(1)).isEqualTo(studentOne);
        assertThat(studentService.editStudent(studentTwo)).isEqualTo(studentTwo);
        assertThat(studentService.findStudent(1)).isEqualTo(studentTwo);
    }

    @Test
    public void editStudentNull() {
        Student studentOne = new Student(1L, "one", 12);
        Student studentTwo = new Student(2L, "two", 14);
        studentService.createStudent(studentOne);

        assertThat(studentService.editStudent(studentTwo)).isNull();
    }

    @Test
    public void deleteStudent() {
        Student studentOne = new Student(1L, "one", 12);
        studentService.createStudent(studentOne);

        assertThat(studentService.findStudent(1)).isEqualTo(studentOne);
        assertThat(studentService.deleteStudent(1)).isEqualTo(studentOne);
        assertThat(studentService.findStudent(1)).isNull();
    }

    @Test
    public void getStudentByAge() {
        Student studentOne = new Student(1L, "one", 12);
        Student studentTwo = new Student(1L, "two", 14);
        Student studentThree = new Student(1L, "three", 11);
        Student studentFour = new Student(1L, "four", 14);

        List<Student> students = List.of(
                studentTwo,
                studentFour
        );

        studentService.createStudent(studentOne);
        studentService.createStudent(studentTwo);
        studentService.createStudent(studentThree);
        studentService.createStudent(studentFour);

        assertThat(studentService.getStudentByAge(14))
                .hasSize(2)
                .containsExactlyElementsOf(students);
        assertThat(studentService.getStudentByAge(1))
                .hasSize(0);
    }
}
