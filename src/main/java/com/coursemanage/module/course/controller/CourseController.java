package com.coursemanage.module.course.controller;

import com.coursemanage.module.course.dto.CourseFilterDTO;
import com.coursemanage.module.course.pojo.Course;
import com.coursemanage.module.course.service.CourseService;
import com.coursemanage.module.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 获取课程列表（支持筛选）
    @GetMapping
    public ApiResponse<List<Course>> getCourses(
            @RequestParam(required = false) String teacherNo,
            @RequestParam(required = false) String acaYear,
            @RequestParam(required = false) String semester) {
        List<Course> list = courseService.getCourses(teacherNo, acaYear, semester);
        return ApiResponse.success(list);
    }

    // 获取课程详情
    @GetMapping("/{id}")
    public ApiResponse<Course> getCourse(@PathVariable Integer id) {
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return ApiResponse.fail(404, "资源未找到");
        }
        return ApiResponse.success(course);
    }
}
