package com.example.graphql.scalar.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="student")
@DynamicUpdate
public class Student {
    @Id
    @Column(name="student_id")
    private String studentId;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="student_class")
    private String studentClass;
    @Column(name="student_section")
    private String studentSection;
    @Column(name="email")
    private String email;
}
