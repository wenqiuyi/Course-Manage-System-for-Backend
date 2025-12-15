package com.coursemanage.module.course.service;

import com.coursemanage.module.course.dto.CourseUpdateDTO;
import com.coursemanage.module.course.pojo.Course;
import java.util.List;

public interface CourseService {
    List<Course> getCourses(String teacherNo);
    List<Course> getAllCourses();
    Course getCourseById(Integer id);

    boolean updateCourse(Integer id, CourseUpdateDTO dto);
}
