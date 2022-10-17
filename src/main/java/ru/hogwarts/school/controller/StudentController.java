package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentRecord createStudent(@RequestBody StudentRecord studentRecord) {
        return studentService.createStudent(studentRecord);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> findStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editStudent = studentService.editStudent(student);
        if (editStudent == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getStudentByAge(@RequestParam(required = false) Integer age,
                                                               @RequestParam(required = false) Integer min,
                                                               @RequestParam(required = false) Integer max) {
        if (age == null && min == null && max == null) {
            return ResponseEntity.ok(studentService.getAll());
        } else if (age == null && min == null && max != null && max > 0) {
            return ResponseEntity.ok(studentService.findByAgeLessMax(max));
        } else if (age == null && min != null && min > 0 && max == null) {
            return ResponseEntity.ok(studentService.findByAgeGreaterMin(min));
        } else if (age == null && min != null && min > 0 && max != null && max > 0) {
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        } else if (age != null && age > 0 && min == null && max == null) {
            return ResponseEntity.ok(studentService.getByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> findStudentFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
