package com.coursemanage.module.student.service;

import com.coursemanage.module.student.pojo.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentByNo(String studentNo);
}
