package com.coursemanage.module.teacher.impl;

import com.coursemanage.module.teacher.mapper.TeacherMapper;
import com.coursemanage.module.teacher.pojo.Teacher;
import com.coursemanage.module.teacher.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherMapper.getAllTeachers();
    }
}
