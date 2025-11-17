package com.coursemanage.module.student.impl;

import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.module.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.getAllStudents();
    }
}
