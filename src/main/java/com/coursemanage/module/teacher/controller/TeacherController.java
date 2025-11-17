package com.coursemanage.module.teacher.controller;

import com.coursemanage.module.teacher.pojo.Teacher;
import com.coursemanage.module.teacher.service.TeacherService;
import com.coursemanage.module.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // 获取所有教师
    @GetMapping
    public ApiResponse<List<Teacher>> getAllTeachers() {
        return ApiResponse.success(teacherService.getAllTeachers());
    }
}
