package com.coursemanage.module.student.controller;

import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.module.student.service.StudentService;
import com.coursemanage.module.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 获取所有学生
    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(studentService.getAllStudents());
    }

    //根据学号获取学生
    @GetMapping("/num/{studentNo}")
    public ApiResponse<Student> getStudentByNo(@PathVariable String studentNo) {
        if (studentNo == null || studentNo.trim().isEmpty()) {
            return ApiResponse.fail(404,"学号不能为空");
        }
        Student student = studentService.getStudentByNo(studentNo.trim());
        if (student != null) {
            return ApiResponse.success(student);
        } else {
            return ApiResponse.fail(404, "未找到学号为[" + studentNo + "]的学生");
        }
    }
    @GetMapping("/course/{courseId}")
    public ApiResponse<List<Student>> getStudentsByCourseId(@PathVariable Integer courseId) {
        return ApiResponse.success(studentService.getStudentsByCourseId(courseId));
    }
}
