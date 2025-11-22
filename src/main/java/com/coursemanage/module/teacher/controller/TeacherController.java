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
    //根据学号获取教师
    @GetMapping("/{teacherNo}")
    public ApiResponse<Teacher> getTeacherByNo(@PathVariable String teacherNo) {
        if (teacherNo == null || teacherNo.trim().isEmpty()) {
            return ApiResponse.fail(404,"教师号不能为空");
        }

        Teacher teacher= teacherService.getTeacherByNo(teacherNo.trim());
        if (teacher != null) {
            return ApiResponse.success(teacher);
        } else {
            return ApiResponse.fail(404, "未找到教师号为[" + teacherNo + "]的教师");
        }
    }
}
