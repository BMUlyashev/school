package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private final String url = "http://localhost:";

    @Test
    public void contextLoads() {
        assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudent() throws Exception {
        Student student = createStudent(1, "1", 1);

        JSONObject json = restTemplate.postForObject(url + port, student, JSONObject.class);

        assertThat(json).isNotNull();
        assertThat(json.getInt("age")).isEqualTo(1);

    }

    private Student createStudent(long id, String name, int age) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        return student;
    }
}
