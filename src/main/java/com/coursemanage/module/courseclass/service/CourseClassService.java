package com.coursemanage.module.courseclass.service;

import com.coursemanage.module.courseclass.pojo.CourseClass;
import java.util.List;

public interface CourseClassService {
    void createCourseClass(CourseClass cc, java.util.List<String> studentList);
    List<CourseClass> getCourseClasses(Integer courseId, String teacherNo);
    CourseClass getCourseClassById(Integer id);
    void updateCourseClass(Integer id, CourseClass update);
    void deleteCourseClass(Integer id);

}
