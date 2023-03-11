package com.example.SUSTechNote.service;

import com.example.SUSTechNote.entity.Course;
import com.example.SUSTechNote.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {

    public int addCourse(int courseID,String courseName);

    public int updateCourse(Course course);

    public int checkCourse(int courseID);

    public int deleteCourse(int courseID);

    public List<Course> findAllCourse();
}
