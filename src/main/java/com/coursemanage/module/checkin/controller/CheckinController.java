package com.coursemanage.module.checkin.controller;

import com.coursemanage.pojo.dto.ApiResponse;
import com.coursemanage.module.checkin.dto.CheckinRecordVO;
import com.coursemanage.module.checkin.dto.CheckinStartRequest;
import com.coursemanage.module.checkin.dto.CheckinSubmitRequest;
import com.coursemanage.module.checkin.entity.Checkin;
import com.coursemanage.module.checkin.entity.CheckinRecord;
import com.coursemanage.module.checkin.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sign")
@Validated
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    /**
     * 教师发起签到
     * POST /api/sign/start
     */
    @PostMapping("/start")
    public ApiResponse<Checkin> startCheckin(
            @Valid @RequestBody CheckinStartRequest request,
            @RequestHeader(value = "X-Person-No", required = false) String teacherPersonNo) {
        try {
            if (teacherPersonNo == null || teacherPersonNo.isEmpty()) {
                return ApiResponse.error(401, "缺少身份信息");
            }
            Checkin checkin = checkinService.startCheckin(request, teacherPersonNo);
            return ApiResponse.success("签到发起成功", checkin);
        } catch (Exception e) {
            e.printStackTrace(); // 打印堆栈跟踪以便调试
            String errorMsg = e.getMessage();
            if (errorMsg == null || errorMsg.isEmpty()) {
                errorMsg = e.getClass().getSimpleName() + ": " + e.toString();
            }
            return ApiResponse.error(500, errorMsg);
        }
    }

    /**
     * 学生提交签到
     * POST /api/sign/submit
     */
    @PostMapping("/submit")
    public ApiResponse<CheckinRecord> submitCheckin(@Valid @RequestBody CheckinSubmitRequest request) {
        try {
            CheckinRecord record = checkinService.submitCheckin(request);
            return ApiResponse.success("签到成功", record);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取签到记录
     * GET /api/sign/records?checkinId=xxx
     */
    @GetMapping("/records")
    public ApiResponse<List<CheckinRecordVO>> getCheckinRecords(
            @RequestParam Integer checkinId) {
        try {
            List<CheckinRecordVO> records = checkinService.getCheckinRecords(checkinId);
            return ApiResponse.success(records);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}


