package ru.hogwarts.school.record;

import java.util.Objects;

public class StudentRecord {

    private long id;

    private String name;

    private int age;

    private FacultyRecord faculty;
    private AvatarRecord avatar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public FacultyRecord getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyRecord faculty) {
        this.faculty = faculty;
    }

    public AvatarRecord getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarRecord avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return id == that.id && age == that.age && Objects.equals(name, that.name) && Objects.equals(faculty, that.faculty) && Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, faculty, avatar);
    }
}
