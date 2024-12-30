package com.stream.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="courses")
@Getter
@Setter
public class Course {

    @Id
    private String courseId;
    private String title;

    // @OneToMany(
    //     mappedBy = "course"
    // )
    // private List<Video> videos=new ArrayList<>();
}
