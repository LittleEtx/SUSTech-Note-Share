package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.api.CourseRepository;
import com.example.SUSTechNote.entity.Course;
import com.example.SUSTechNote.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Override
    public int addCourse(int courseID,String courseName){
        if (checkCourse(courseID) == 0 ){
            Course course = new Course();
            course.setCourseID(courseID);
            course.setCourseName(courseName);
            courseRepository.save(course);
            return 1;
        }
        if (checkCourse(courseID) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int updateCourse(Course course){
        if (checkCourse(course.getCourseID()) == 1 ){
            courseRepository.save(course);
            return 1;
        }
        if (checkCourse(course.getCourseID()) == 400){
            return 400;
        }
        return 0;
    };

    @Override
    public int checkCourse(int courseID){
        List<Course> courses = courseRepository.findCoursesByCourseID(courseID);
        if (courses.size() == 1) {
            return 1;
        } else if (courses.size() > 1) {
            return 400;
        } else {
            return 0;
        }
    };

    @Override
    public int deleteCourse(int courseID){
        if (checkCourse(courseID) == 1){
            courseRepository.deleteById(courseID);
            return 1;
        }
        return 0;
    };

    @Override
    public List<Course> findAllCourse(){
        return courseRepository.findAll();
    };
}
