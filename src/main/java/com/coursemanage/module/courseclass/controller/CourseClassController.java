package com.coursemanage.module.courseclass.controller;

import com.coursemanage.module.courseclass.dto.CourseClassCreateDTO;
import com.coursemanage.module.courseclass.dto.UpdateCourseClassDTO;
import com.coursemanage.module.courseclass.pojo.CourseClass;
import com.coursemanage.module.courseclass.service.CourseClassService;
import com.coursemanage.module.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course_class")
public class CourseClassController {

    @Autowired
    private CourseClassService courseClassService;

    // 创建课程班级
    @PostMapping("/create")
    public ApiResponse<Void> createCourseClass(@Valid @RequestBody CourseClassCreateDTO dto) {
        CourseClass cc = new CourseClass();
        cc.setCourseId(dto.getCourseId());
        cc.setClassName(dto.getClassName());
        cc.setTeacherNo(dto.getTeacherNo());
        courseClassService.createCourseClass(cc, dto.getStudentList());
        return ApiResponse.successMessage("班级创建成功");
    }

    // 获取课程班级列表
    @GetMapping("/list")
    public ApiResponse<List<CourseClass>> listCourseClass(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String teacherNo) {
        return ApiResponse.success(courseClassService.getCourseClasses(courseId, teacherNo));
    }

    // 查看班级详情
    @GetMapping("/{id}")
    public ApiResponse<CourseClass> getCourseClass(@PathVariable Integer id) {
        CourseClass cc = courseClassService.getCourseClassById(id);
        if (cc == null) {
            return ApiResponse.fail(404, "资源未找到");
        }
        return ApiResponse.success(cc);
    }

    // 修改班级信息
    @PutMapping("/update/{id}")
    public ApiResponse<Void> updateCourseClass(@PathVariable Integer id,
                                               @RequestBody UpdateCourseClassDTO dto) {
        CourseClass update = new CourseClass();
        update.setClassName(dto.getClassName());
        update.setTeacherNo(dto.getTeacherNo());
        courseClassService.updateCourseClass(id, update);
        return ApiResponse.successMessage("班级信息更新成功");
    }

    // 删除课程班级
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteCourseClass(@PathVariable Integer id) {
        courseClassService.deleteCourseClass(id);
        return ApiResponse.successMessage("课程班级删除成功");
    }
}
