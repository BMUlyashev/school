package ru.hogwarts.school.service;

import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    @Mock
    FacultyRepository facultyRepository;

    @InjectMocks
    FacultyService facultyService;

    @Test
    public void createFaculty() {
        Faculty faculty = createFaculty(1, "1", "red");
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);

        assertThat(facultyService.createFaculty(faculty)).isEqualTo(faculty);
    }

    @Test
    public void findFaculty() {
        Faculty faculty = createFaculty(1, "1", "red");

        when(facultyRepository.findById(any()))
                .thenReturn(Optional.of(faculty));

        Faculty actual = facultyService.findFaculty(1);
        assertThat(actual).isEqualTo(faculty);
    }

    @Test
    public void findFacultyNull() {
        Faculty faculty = createFaculty(1, "1", "red");

        when(facultyRepository.findById(any()))
                .thenReturn(Optional.empty());

        Faculty actual = facultyService.findFaculty(1);
        assertThat(actual).isNull();
    }

    @Test
    public void editFaculty() {
        Faculty faculty = createFaculty(1, "1", "red");
        Faculty facultyNew = createFaculty(1, "2", "blue");

        when(facultyRepository.findById(any()))
                .thenReturn(Optional.of(faculty))
                .thenReturn(Optional.empty());

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(facultyNew);
        assertThat(facultyService.editFaculty(faculty)).isEqualTo(facultyNew);
        assertThat(facultyService.editFaculty(faculty)).isNull();
    }

    @Test
    public void deleteFaculty() {
        doNothing().when(facultyRepository).deleteById(any());
        facultyService.deleteFaculty(1);
        verify(facultyRepository, times(1)).deleteById(any());
    }

    @Test
    public void getByColor() {
        List<Faculty> faculties = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "3", "red"),
                createFaculty(5, "5", "red")
        );

        when(facultyRepository.findByColor(any()))
                .thenReturn(faculties)
                .thenReturn(Collections.emptyList());

        assertThat(facultyService.getByColor(any()))
                .filteredOn(faculty -> faculty.getColor().equals("red"))
                .hasSize(3);
        assertThat(facultyService.getByColor(any()))
                .filteredOn(faculty -> faculty.getColor().equals("blue"))
                .hasSize(0);
    }

    @Test
    public void getAll() {
        List<Faculty> faculties = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "3", "blue"),
                createFaculty(5, "5", "white")
        );

        when(facultyRepository.findAll())
                .thenReturn(faculties);

        assertThat(facultyService.getAll()).hasSize(3);
        assertThat(facultyService.getAll())
                .containsExactlyInAnyOrderElementsOf(faculties);
    }


    @Test
    public void getByFilterString() {
        List<Faculty> facultiesEqualColor = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "3", "red")
        );
        List<Faculty> facultiesEqualName = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "1", "blue")
        );

        when(facultyRepository.findNameOrColor("red"))
                .thenReturn(facultiesEqualColor);


        when(facultyRepository.findNameOrColor("1"))
                .thenReturn(facultiesEqualName);

        assertThat(facultyService.getByFilterString("red"))
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(facultiesEqualColor);
        assertThat(facultyService.getByFilterString("1"))
                .hasSize(2)
                .containsExactlyInAnyOrderElementsOf(facultiesEqualName);
    }

    @Test
    public void findStudent() {
        List<Student> students = List.of(
                createStudent(1, "1", 18),
                createStudent(3, "3", 19),
                createStudent(5, "5", 20)
        );

        when(facultyRepository.findStudents(any()))
                .thenReturn(students);

        assertThat(facultyService.findStudents(1))
                .hasSize(3)
                .containsExactlyInAnyOrderElementsOf(students);
    }

    private Faculty createFaculty(long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    private Student createStudent(long id, String name, int age) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        return student;
    }
}
