package com.coursemanage.module.course.service;

import com.coursemanage.module.course.dto.CourseUpdateDTO;
import com.coursemanage.module.course.pojo.Course;
import java.util.List;

public interface CourseService {
    List<Course> getCourses(String teacherNo);
    List<Course> getAllCourses();
    Course getCourseById(Integer id);

    boolean updateCourse(Integer id, CourseUpdateDTO dto);
    
    // 添加课程
    boolean addCourse(Course course);
    
    // 删除课程
    boolean deleteCourse(Integer id);
}