package com.coursemanage.module.student.service;

import com.coursemanage.module.student.pojo.Student;
import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentByNo(String studentNo);
    List<Student> getStudentsByCourseId(Integer courseId);

    boolean assignStudentToCourse(String studentNo, Integer courseId);

    boolean removeStudentFromCourse(String studentNo);

    boolean batchAssignStudentsToCourse(List<String> studentNos, Integer courseId);
}
