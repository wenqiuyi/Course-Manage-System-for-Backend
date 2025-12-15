package com.coursemanage.module.student.impl;

import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.module.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.getAllStudents();
    }

    @Override
    public Student getStudentByNo(String studentNo) {
        return studentMapper.getByStudentNo(studentNo);
    }
    @Override
    public List<Student> getStudentsByCourseId(Integer courseId){
        return studentMapper.getStudentsByCourseId(courseId);
    }
    @Override
    public boolean assignStudentToCourse(String studentNo, Integer courseId) {
        return studentMapper.assignStudentToCourse(studentNo, courseId) > 0;
    }

    @Override
    public boolean removeStudentFromCourse(String studentNo) {
        return studentMapper.removeStudentFromCourse(studentNo) > 0;
    }
    @Override
    @Transactional
    public boolean batchAssignStudentsToCourse(List<String> studentNos, Integer courseId) {
        int updated = studentMapper.batchAssignStudents(studentNos, courseId);
        return updated == studentNos.size(); // 全部更新成功返回 true
    }
}
