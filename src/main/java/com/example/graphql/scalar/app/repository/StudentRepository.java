package com.example.graphql.scalar.app.repository;

import com.example.graphql.scalar.app.model.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {

}