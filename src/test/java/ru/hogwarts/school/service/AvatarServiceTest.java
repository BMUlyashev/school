package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {

    @Mock
    AvatarRepository avatarRepository;
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
    public void uploadAvatar() {
        Student student = createStudent(1, "1", 25);
        when(studentRepository.findById(any()))
                .thenReturn(Optional.of(student));
    }

    @Test
    public void uploadAvatarNotFoundStudent() {
        Student student = createStudent(1, "1", 25);
        when(studentRepository.findById(any()))
                .thenReturn(Optional.empty());

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
