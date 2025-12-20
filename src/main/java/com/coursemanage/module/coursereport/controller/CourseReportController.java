package com.coursemanage.module.coursereport.controller;

import com.coursemanage.module.coursereport.pojo.CourseReport;
import com.coursemanage.module.coursereport.service.impl.CourseReportServiceImpl;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coursereport")
@RequiredArgsConstructor
public class CourseReportController {
    private final CourseReportServiceImpl courseReportService;
    @GetMapping("/")
    public ResponseResult<List<CourseReport>> getCourseReportList() {
        List<CourseReport> list = courseReportService.list();
        return ResponseResult.success(list);
    }
    @GetMapping("/{id}")
    public ResponseResult<CourseReport> getCourseReport(@PathVariable Integer id) {
        CourseReport byId = courseReportService.getById(id);
        if(byId != null)
            return ResponseResult.success(byId);
        else
            return ResponseResult.error();
    }
    @PostMapping("/")
    public ResponseResult<CourseReport> addCourseReport(@RequestBody CourseReport courseReport) {
        courseReport.setId(null);
        boolean save = courseReportService.save(courseReport);
        return save ? ResponseResult.success(courseReport) : ResponseResult.error();
    }
    @PutMapping("/")
    public ResponseResult<CourseReport> updateCourseReport(@RequestBody CourseReport courseReport) {
        boolean b = courseReportService.updateById(courseReport);
        return b ? ResponseResult.success(courseReport) : ResponseResult.error();
    }
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteCourseReport(@PathVariable Integer id) {
        boolean b = courseReportService.removeById(id);
        return b ? ResponseResult.success() : ResponseResult.error();
    }
}
