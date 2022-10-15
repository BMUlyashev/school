package ru.hogwarts.school.service;

import jdk.jfr.ContentType;


import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {

    @Mock
    AvatarRepository avatarRepository;
    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    AvatarService avatarService;


    @Test
    public void findAvatar() {
        Student student = createStudent(1, "1", 1);
        Avatar avatar = createAvatar(2, "home", 4, student);
        when(avatarRepository.findByStudentId(any()))
                .thenReturn(Optional.of(avatar))
                .thenReturn(Optional.empty());

        assertThat(avatarService.findAvatar(1)).isEqualTo(avatar);
        assertThat(avatarService.findAvatar(2)).isNull();
    }

    @Test
    public void uploadAvatarNotFoundStudent() {
        Student student = createStudent(1, "1", 25);
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile(
                "test",
                "test.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                "some".getBytes());

        assertThatThrownBy(() -> avatarService.uploadAvatar(1, file))
                .isInstanceOf(StudentNotFoundException.class);

    }

    @Test
    public void testUpload() throws Exception {
        final String imageFile = "src/test/java/ru/hogwarts/school/resource/1.jpg";
        final String imagePathNew = "src/test/java/ru/hogwarts/school/resource/copy";

        File file = new File(imageFile);
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/jpg", IOUtils.toByteArray(input));

        Student student = createStudent(1, "1", 1);
        Avatar avatar = createAvatar(1, "1", 120, student);

        ReflectionTestUtils.setField(avatarService, "avatarsDir", imagePathNew);

        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(student));

        when(avatarRepository.findByStudentId(any()))
                .thenReturn(Optional.of(avatar));

        when(avatarRepository.save(any()))
                .thenReturn(null);

        avatarService.uploadAvatar(1, multipartFile);

        File actualFile = new File("src/test/java/ru/hogwarts/school/resource/1.jpg");
        File expectedFile = new File(imagePathNew + "/" + student + ".jpg");

        assertThat(actualFile).hasSameBinaryContentAs(expectedFile);
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

    private Student createStudent(long id, String name, int age) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        return student;
    }

}
