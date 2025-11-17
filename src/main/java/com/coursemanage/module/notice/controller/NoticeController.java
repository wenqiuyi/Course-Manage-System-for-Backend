package com.coursemanage.module.notice.controller;

import com.coursemanage.pojo.dto.ApiResponse;
import com.coursemanage.module.notice.dto.NoticeCreateRequest;
import com.coursemanage.module.notice.dto.NoticeVO;
import com.coursemanage.module.notice.entity.CourseNotice;
import com.coursemanage.module.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
@Validated
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 发布课程通知
     * POST /api/notice/create
     */
    @PostMapping("/create")
    public ApiResponse<CourseNotice> createNotice(
            @Valid @RequestBody NoticeCreateRequest request,
            @RequestHeader(value = "X-Person-No", required = false) String publisherPersonNo) {
        try {
            if (publisherPersonNo == null || publisherPersonNo.isEmpty()) {
                return ApiResponse.error(401, "缺少身份信息");
            }
            CourseNotice notice = noticeService.createNotice(request, publisherPersonNo);
            return ApiResponse.success("通知发布成功", notice);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取课程通知
     * GET /api/notice/list?courseId=xxx
     */
    @GetMapping("/list")
    public ApiResponse<List<NoticeVO>> getNoticeList(@RequestParam Integer courseId) {
        try {
            List<NoticeVO> notices = noticeService.getNoticeList(courseId);
            return ApiResponse.success(notices);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}


