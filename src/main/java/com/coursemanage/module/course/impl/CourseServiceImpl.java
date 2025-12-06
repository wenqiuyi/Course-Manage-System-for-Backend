package com.coursemanage.module.course.impl;

import com.coursemanage.module.course.mapper.CourseMapper;
import com.coursemanage.module.teacher.mapper.TeacherMapper;
import com.coursemanage.module.course.pojo.Course;
import com.coursemanage.module.teacher.pojo.Teacher;
import com.coursemanage.module.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Course> getCourses(String teacherNo) {

        return courseMapper.getCourses(teacherNo);
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMapper.getCourseById(id);
    }
}
