package com.hirekarma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hirekarma.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

}
