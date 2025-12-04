package com.coursemanage.module.reportposition.controller;

import com.coursemanage.module.coursereport.pojo.CourseReport;
import com.coursemanage.module.coursereport.service.impl.CourseReportServiceImpl;
import com.coursemanage.module.reportposition.pojo.ReportPosition;
import com.coursemanage.module.reportposition.service.ReportPositionService;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportposition")
@RequiredArgsConstructor
public class ReportPositionController {
    private final ReportPositionService reportPositionService;
    @GetMapping("/")
    public ResponseResult<List<ReportPosition>> getReportPositionList() {
        List<ReportPosition> list = reportPositionService.list();
        return ResponseResult.success(list);
    }
    @GetMapping("/{id}")
    public ResponseResult<ReportPosition> getReportPosition(@PathVariable Integer id) {
        ReportPosition byId = reportPositionService.getById(id);
        if(byId != null)
            return ResponseResult.success(byId);
        else
            return ResponseResult.error();
    }
    @PostMapping("/")
    public ResponseResult<Void> addReportPosition(@RequestBody ReportPosition reportPosition) {
        reportPosition.setId(null);
        boolean save = reportPositionService.save(reportPosition);
        return save ? ResponseResult.success() : ResponseResult.error();
    }
    @PutMapping("/")
    public ResponseResult<Void> updateReportPosition(@RequestBody ReportPosition reportPosition) {
        boolean b = reportPositionService.updateById(reportPosition);
        return b ? ResponseResult.success() : ResponseResult.error();
    }
    @DeleteMapping("/{id}")
    public ResponseResult<Void> deleteReportPosition(@PathVariable Integer id) {
        boolean b = reportPositionService.removeById(id);
        return b ? ResponseResult.success() : ResponseResult.error();
    }
}
