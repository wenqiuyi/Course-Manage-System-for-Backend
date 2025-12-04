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
    public List<Course> getCourses(String teacherNo, String acaYear, String semester) {
        Map<String,Object> map = new HashMap<>();
        map.put("teacherNo", teacherNo);
        map.put("acaYear", acaYear);
        map.put("semester", semester);
        return courseMapper.getCoursesByFilter(teacherNo, acaYear, semester);
    }

    @Override
    public Course getCourseById(Integer id) {
        Course c = courseMapper.getCourseById(id);
        if (c != null && c.getTeacherNo() != null) {
            Teacher t = teacherMapper.getByTeacherNo(c.getTeacherNo());
//            c.setTeacher(t);
        }
        return c;
    }
}
