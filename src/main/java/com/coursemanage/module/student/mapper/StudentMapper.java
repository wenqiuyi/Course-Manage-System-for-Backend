package com.coursemanage.module.student.mapper;

import com.coursemanage.module.student.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> getAllStudents();
    Student getByStudentNo(String studentNo);
    void insertStudent(Student student);
}
