package com.coursemanage.module.course.impl;

import com.coursemanage.module.course.dto.CourseUpdateDTO;
import com.coursemanage.module.course.mapper.CourseMapper;
import com.coursemanage.module.teacher.mapper.TeacherMapper;
import com.coursemanage.module.course.pojo.Course;
import com.coursemanage.module.teacher.pojo.Teacher;
import com.coursemanage.module.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
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
    public List<Course> getAllCourses() {

        return courseMapper.getAllCourses();
    }

    @Override
    public Course getCourseById(Integer id) {
        return courseMapper.getCourseById(id);
    }

    @Override
    @Transactional
    public boolean updateCourse(Integer id, CourseUpdateDTO dto){
        Course course = courseMapper.getCourseById(id);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        if (dto.getName() != null) course.setName(dto.getName());
        if (dto.getDescription() != null) course.setDescription(dto.getDescription());
        if (dto.getCommentArea() != null) course.setCommentArea(dto.getCommentArea());
        if (dto.getAcaYear() != null) course.setAcaYear(dto.getAcaYear());
        if (dto.getSemester() != null) course.setSemester(dto.getSemester());
        if (dto.getTeacherNo() != null) course.setTeacherNo(dto.getTeacherNo());

        return courseMapper.updateById(course) > 0;
    }
}
