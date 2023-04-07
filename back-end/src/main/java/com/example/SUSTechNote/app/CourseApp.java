package com.example.SUSTechNote.app;

import com.example.SUSTechNote.entity.Course;

import com.example.SUSTechNote.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseApp {

    @Autowired
    CourseService courseService;

    @PostMapping("/addCourse")
    public int addCourse(String courseID,String courseName){
        return courseService.addCourse( courseID, courseName);
    }
    @PostMapping("/updateCourse")
    public int updateCourse(Course course){
        return courseService.updateCourse(course);
    }

    @GetMapping("/deleteCourse")
    public int deleteCourse(String courseID){
        return courseService.deleteCourse(courseID);
    }

    @GetMapping("/findAllCourse")
    public List<Course> findAllCourse(){
        return courseService.findAllCourse();
    }
}
