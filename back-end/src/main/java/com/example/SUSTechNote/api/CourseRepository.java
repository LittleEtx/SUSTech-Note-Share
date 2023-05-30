package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findCoursesByCourseID(String CourseID);
    void deleteCourseByCourseID(String CourseID);
}
