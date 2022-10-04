package ru.hogwarts.school.service;

import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
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
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);
        assertThat(facultyService.editFaculty(faculty)).isEqualTo(faculty);
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


    private Faculty createFaculty(long id, String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }
}
