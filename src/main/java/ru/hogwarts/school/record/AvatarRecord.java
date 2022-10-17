package ru.hogwarts.school.record;

public class AvatarRecord {

    private long id;

    private String mediaType;

    private String url;

    private StudentRecord student;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public StudentRecord getStudent() {
        return student;
    }

    public void setStudent(StudentRecord student) {
        this.student = student;
    }
}
