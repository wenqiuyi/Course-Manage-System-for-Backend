package com.coursemanage.module.course.controller;

import com.coursemanage.module.course.dto.CourseUpdateDTO;
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


    @GetMapping("/class/{teacherNo}")
    public ApiResponse<List<Course>> getCourses(@PathVariable String teacherNo) {

        if (teacherNo == null || teacherNo.trim().isEmpty()) {
            return ApiResponse.fail(400, "教师编号不能为空");
        }
        return ApiResponse.success(courseService.getCourses(teacherNo));
    }

    @GetMapping("/class")
    public ApiResponse<List<Course>> getAllCourses() {
        return ApiResponse.success(courseService.getAllCourses());
    }



    @GetMapping("/{id}")
    public ApiResponse<Course> getCourse(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ApiResponse.fail(400, "课程ID必须为正整数");
        }
        Course course = courseService.getCourseById(id);
        if (course == null) {
            return ApiResponse.fail(404, "资源未找到");
        }
        return ApiResponse.success(course);
    }
    

    @PostMapping("/update/{id}")
    public ApiResponse<? extends Object> updateCourse(
            @PathVariable Integer id,
            @RequestBody CourseUpdateDTO dto) {

        boolean result = courseService.updateCourse(id, dto);
        return result ? ApiResponse.success("更新信息成功")
                : ApiResponse.fail(500, "课程更新失败");
    }





}
