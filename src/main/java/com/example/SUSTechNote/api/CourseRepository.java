package com.example.SUSTechNote.api;

import com.example.SUSTechNote.entity.Course;
import com.example.SUSTechNote.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    public List<Course> findCoursesByCourseID(int CourseID);
}
