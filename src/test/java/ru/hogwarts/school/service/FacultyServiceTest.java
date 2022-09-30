package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FacultyServiceTest {

    private final FacultyService facultyService = new FacultyService();

    @Test
    public void createFaculty() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        Faculty facultyTwo = new Faculty(1L, "two", "blue");

        assertThat(facultyService.createFaculty(facultyOne)).isEqualTo(facultyOne);
        assertThat(facultyService.createFaculty(facultyTwo)).isEqualTo(facultyTwo);

        assertThat(facultyService.findFaculty(1)).isEqualTo(facultyOne);
        assertThat(facultyService.findFaculty(2)).isEqualTo(facultyTwo);
    }

    @Test
    public void findFaculty() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        assertThat(facultyService.createFaculty(facultyOne)).isEqualTo(facultyOne);

        assertThat(facultyService.findFaculty(1)).isEqualTo(facultyOne);
        assertThat(facultyService.findFaculty(2)).isNull();
    }

    @Test
    public void editFacultyPositive() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        Faculty facultyTwo = new Faculty(1L, "two", "blue");
        facultyService.createFaculty(facultyOne);

        assertThat(facultyService.findFaculty(1)).isEqualTo(facultyOne);
        assertThat(facultyService.editFaculty(facultyTwo)).isEqualTo(facultyTwo);
        assertThat(facultyService.findFaculty(1)).isEqualTo(facultyTwo);
    }

    @Test
    public void editFacultyNull() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        Faculty facultyTwo = new Faculty(2L, "two", "blue");
        facultyService.createFaculty(facultyOne);

        assertThat(facultyService.editFaculty(facultyTwo)).isNull();
    }

    @Test
    public void deleteFaculty() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        facultyService.createFaculty(facultyOne);

        assertThat(facultyService.findFaculty(1)).isEqualTo(facultyOne);
        assertThat(facultyService.deleteFaculty(1)).isEqualTo(facultyOne);
        assertThat(facultyService.findFaculty(1)).isNull();
    }

    @Test
    public void getFacultyByColor() {
        Faculty facultyOne = new Faculty(1L, "one", "red");
        Faculty facultyTwo = new Faculty(1L, "two", "blue");
        Faculty facultyThree = new Faculty(1L, "three", "black");
        Faculty facultyFour = new Faculty(1L, "four", "blue");

        List<Faculty> faculties = List.of(
                facultyTwo,
                facultyFour
        );

        facultyService.createFaculty(facultyOne);
        facultyService.createFaculty(facultyTwo);
        facultyService.createFaculty(facultyThree);
        facultyService.createFaculty(facultyFour);

        assertThat(facultyService.getFacultyByColor("blue"))
                .hasSize(2)
                .containsExactlyElementsOf(faculties);
        assertThat(facultyService.getFacultyByColor("green"))
                .hasSize(0);
    }
}
