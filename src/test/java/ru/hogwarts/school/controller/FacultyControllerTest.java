package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private AvatarRepository avatarRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private AvatarService avatarService;

    @InjectMocks
    private FacultyController facultyController;


    @Test
    public void contentLoads() {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void createFaculty() throws Exception {
        Faculty faculty = createFaculty(1, "test", "black");

        JSONObject json = new JSONObject();
        json.put("name", faculty.getName());
        json.put("color", faculty.getColor());

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    public void getFaculty() throws Exception {
        Faculty faculty = createFaculty(1, "test", "black");

        when(facultyRepository.findById(any()))
                .thenReturn(Optional.of(faculty))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(faculty.getId()))
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editFaculty() throws Exception {
        Faculty faculty = createFaculty(1, "test", "black");
        Faculty facultyNew = createFaculty(1, "test2", "red");

        JSONObject json = new JSONObject();
        json.put("name", facultyNew.getName());
        json.put("color", facultyNew.getColor());

        when(facultyRepository.findById(any()))
                .thenReturn(Optional.of(faculty))
                .thenReturn(Optional.empty());
        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(facultyNew);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyNew.getId()))
                .andExpect(jsonPath("$.name").value(facultyNew.getName()))
                .andExpect(jsonPath("$.color").value(facultyNew.getColor()));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deleteFaculty() throws Exception {
        doNothing().when(facultyRepository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getFacultyAll() throws  Exception {
        List<Faculty> faculties = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "3", "blue"),
                createFaculty(5, "5", "white")
        );

        when(facultyRepository.findAll())
                .thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("1", "3", "5")));
    }

    @Test
    public void getFacultyByFilter() throws Exception {
        List<Faculty> faculties = List.of(
                createFaculty(1, "1", "red"),
                createFaculty(3, "3", "red")
        );

        when(facultyRepository.findNameOrColor(any()))
                .thenReturn(faculties)
                .thenReturn(Collections.emptyList());


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?filterString=red")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].color").value(containsInAnyOrder("red", "red")));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty?filterString=")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void findStudent() throws Exception {
               List<Student> students = List.of(
                createStudent(1, "1", 17),
                createStudent(3, "2", 18)
        );

        when(facultyRepository.findStudent(any())).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/{id}/students", 2)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("1", "2")));

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
