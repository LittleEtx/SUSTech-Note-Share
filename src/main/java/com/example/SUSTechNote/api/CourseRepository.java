package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
