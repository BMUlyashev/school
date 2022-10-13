package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setAllowComparingPrivateFields;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private final String url = "http://localhost:";

    @Test
    public void contextLoads() {
        assertThat(studentController).isNotNull();
    }


    @Test
    public void createStudent() throws Exception {
        Student student = createStudent(1, "test", 1);

        Student actual = restTemplate.postForObject(url + port + "/student", student, Student.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNegative();
        assertThat(actual.getName()).isEqualTo(student.getName());
        assertThat(actual.getAge()).isEqualTo(student.getAge());

        studentRepository.deleteById(actual.getId());
    }

    @Test
    public void findStudent() throws Exception {
        Student student = createStudent(1, "test", 1);
        student = createStudentInRepository(student);
        long id = student.getId();
        ResponseEntity<Student> response = restTemplate.getForEntity(url + port + "/student/{id}", Student.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(student.getName());
        assertThat(response.getBody().getAge()).isEqualTo(student.getAge());

        studentRepository.deleteById(id);
    }

    @Test
    public void findStudentNotFound() {
        long id = -1;
        ResponseEntity<Student> response = restTemplate.getForEntity(url + port + "/student/{id}", Student.class, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void editStudent() {
        Student student = createStudent(1, "test", 1);
        student = createStudentInRepository(student);
        Student studentNew = createStudent(student.getId(), "testNew", 20);

        HttpEntity<Student> entity = new HttpEntity<>(studentNew);

        ResponseEntity<Student> response = restTemplate.exchange(url + port + "/student",
                HttpMethod.PUT, entity, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAge()).isEqualTo(studentNew.getAge());
        assertThat(response.getBody().getName()).isEqualTo(studentNew.getName());
        studentRepository.deleteById(studentNew.getId());
    }

    @Test
    public void editStudentNotFound() {
        Student student = createStudent(-1, "test", 1);
        HttpEntity<Student> entity = new HttpEntity<>(student);
        ResponseEntity<Student> response = restTemplate.exchange(url + port + "/student",
                HttpMethod.PUT, entity, Student.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void deleteStudent() {
        Student student = createStudent(1, "test", 1);
        student = createStudentInRepository(student);

        ResponseEntity<Student> response = restTemplate.getForEntity(url + port + "/student/{id}", Student.class, student.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        restTemplate.delete(url + port + "/student/{id}", student.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = restTemplate.getForEntity(url + port + "/student/{id}", Student.class, student.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getStudentByAge() {
        Student student = createStudentInRepository(createStudent(1, "test1", 20));
        Student student2 = createStudentInRepository(createStudent(1, "test2", 21));
        Student student3 = createStudentInRepository(createStudent(1, "test3", 21));
        Student student4 = createStudentInRepository(createStudent(1, "test4", 22));

        addStudentsToRepository(student, student2, student3, student4);

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student?age={age}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
                , 21);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).contains(student3).contains(student2);

        deleteStudentsFromRepository(student, student2, student3, student4);
    }

    @Test
    public void getStudentByAgeMoreMin() {
        Student student = createStudentInRepository(createStudent(1, "test1", 20));
        Student student2 = createStudentInRepository(createStudent(1, "test2", 21));
        Student student3 = createStudentInRepository(createStudent(1, "test3", 21));
        Student student4 = createStudentInRepository(createStudent(1, "test4", 22));

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student?min={min}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
                , 21);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).contains(student2).contains(student3).contains(student4);

        deleteStudentsFromRepository(student, student2, student3, student4);
    }

    @Test
    public void getStudentByAgeLessMax() {
        Student student = createStudentInRepository(createStudent(1, "test1", 20));
        Student student2 = createStudentInRepository(createStudent(1, "test2", 21));
        Student student3 = createStudentInRepository(createStudent(1, "test3", 21));
        Student student4 = createStudentInRepository(createStudent(1, "test4", 22));

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student?max={max}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
                , 21);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).contains(student).contains(student2).contains(student3);

        deleteStudentsFromRepository(student, student2, student3, student4);
    }

    @Test
    public void getStudentByAgeBetween() {

        Student student = createStudentInRepository(createStudent(1, "test1", 20));
        Student student2 = createStudentInRepository(createStudent(1, "test2", 21));
        Student student3 = createStudentInRepository(createStudent(1, "test3", 21));
        Student student4 = createStudentInRepository(createStudent(1, "test4", 22));

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student?min={min}&max={max}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
                , 21, 22);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).contains(student2).contains(student3).contains(student4);
        assertThat(students).filteredOn((s) -> s.getAge() < 21 && s.getAge() > 22).hasSize(0);

        deleteStudentsFromRepository(student, student2, student3, student4);
    }

    @Test
    public void getStudentAll() {
        Student student = createStudentInRepository(createStudent(1, "test1", 20));
        Student student2 = createStudentInRepository(createStudent(1, "test2", 21));
        Student student3 = createStudentInRepository(createStudent(1, "test3", 21));
        Student student4 = createStudentInRepository(createStudent(1, "test4", 22));

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).contains(student).contains(student2).contains(student3).contains(student4);

        deleteStudentsFromRepository(student, student2, student3, student4);
    }

    @Test
    public void getStudentEmpty() {
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(url + port + "/student?age={age}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }, -5);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Collection<Student> students = response.getBody();
        assertThat(students).isEmpty();
    }

    @Test
    public void getStudentFaculty() {
        Faculty faculty = createFaculty(1, "faculty", "red");
        Student student = createStudent(1, "test", 1);
        student.setFaculty(faculty);
        student = createStudentInRepository(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(url + port + "/student/{id}/faculty", Faculty.class, student.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getColor()).isEqualTo(faculty.getColor());
        assertThat(response.getBody().getName()).isEqualTo(faculty.getName());

        studentRepository.deleteById(student.getId());
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

    private Student createStudentInRepository(Student student) {
        return studentRepository.save(student);
    }

    private void addStudentsToRepository(Student... students) {
        studentRepository.saveAll(Arrays.asList(students));
    }

    private void deleteStudentsFromRepository(Student... students) {
        Arrays.stream(students).forEach(s -> studentRepository.deleteById(s.getId()));
    }

}
