package com.example.SchoolSpring.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Course implements java.io.Serializable {
    private String name;
    private Double mark;

    public Course(String Biology, double BiologyGrade, String Physics, double PhysicsGrade, String Mathematics, double MathematicsGrade) {
    }
}
