package ru.hogwarts.school.controller;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AvatarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AvatarRepository avatarRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private AvatarService avatarService;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private AvatarController avatarController;

    @Test
    public void AvatarTest() throws Exception {
        Student student = createStudent(1, "1", 1);
        Avatar avatar = createAvatar(1, "1", 120, student);

        final String imageFile = "src/test/java/ru/hogwarts/school/resource/1.jpg";
        final String fileCopy = "avatar/" + student + ".jpg";
        File file = new File(imageFile);
        File newFile = new File(fileCopy);
        newFile.delete();
        FileInputStream input = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("avatar",
                file.getName(), "image/jpg", IOUtils.toByteArray(input));

        when(studentRepository.findById(any())).thenReturn(Optional.of(student));
        when(avatarRepository.findByStudentId(any())).thenReturn(Optional.of(avatar));
        when(avatarRepository.save(any())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/student/1/avatar")
                        .file(multipartFile))
                .andExpect(status().isOk());
        assertThat(newFile).exists();


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1/avatar-from-pc")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect((result -> {
                    assertThat(result.getResponse().getContentLength()).isEqualTo(newFile.length());
                }));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1/avatar-from-db")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect((result -> {
                    assertThat(result.getResponse().getContentLength()).isEqualTo(avatar.getData().length);
                }));


        newFile.delete();
    }

    private Student createStudent(long id, String name, int age) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        return student;
    }

    private Avatar createAvatar(long id, String filePath, long fileSize, Student student) {
        Avatar avatar = new Avatar();
        avatar.setId(id);
        avatar.setMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);
        avatar.setFilePath(filePath);
        avatar.setFileSize(fileSize);
        avatar.setStudent(student);
        avatar.setData(new byte[]{0, 1, 2, 3});
        return avatar;
    }
}
