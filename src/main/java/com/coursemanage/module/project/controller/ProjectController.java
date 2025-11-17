package com.coursemanage.module.project.controller;

import com.coursemanage.pojo.dto.ApiResponse;
import com.coursemanage.module.project.dto.ProjectCreateRequest;
import com.coursemanage.module.project.dto.ProjectScoreRequest;
import com.coursemanage.module.project.dto.ProjectStatsVO;
import com.coursemanage.module.project.dto.ProjectSubmitRequest;
import com.coursemanage.module.project.dto.ProjectVO;
import com.coursemanage.module.project.entity.ProjectPractice;
import com.coursemanage.module.project.entity.ProjectSubmission;
import com.coursemanage.module.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@Validated
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 教师发布项目
     * POST /api/project/create
     */
    @PostMapping("/create")
    public ApiResponse<ProjectPractice> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @RequestHeader(value = "X-Person-No", required = false) String publisherPersonNo) {
        try {
            if (publisherPersonNo == null || publisherPersonNo.isEmpty()) {
                return ApiResponse.error(401, "缺少身份信息");
            }
            ProjectPractice project = projectService.createProject(request, publisherPersonNo);
            return ApiResponse.success("项目发布成功", project);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取项目列表
     * GET /api/project/list?courseId=xxx
     */
    @GetMapping("/list")
    public ApiResponse<List<ProjectVO>> getProjectList(@RequestParam Integer courseId) {
        try {
            List<ProjectVO> projects = projectService.getProjectList(courseId);
            return ApiResponse.success(projects);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 学生提交成果链接
     * POST /api/project/submit
     */
    @PostMapping("/submit")
    public ApiResponse<ProjectSubmission> submitProject(@Valid @RequestBody ProjectSubmitRequest request) {
        try {
            ProjectSubmission submission = projectService.submitProject(request);
            return ApiResponse.success("提交成功", submission);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 教师评分
     * PUT /api/project/score
     */
    @PutMapping("/score")
    public ApiResponse<ProjectSubmission> scoreProject(
            @Valid @RequestBody ProjectScoreRequest request,
            @RequestHeader(value = "X-Person-No", required = false) String teacherPersonNo) {
        try {
            if (teacherPersonNo == null || teacherPersonNo.isEmpty()) {
                return ApiResponse.error(401, "缺少身份信息");
            }
            ProjectSubmission submission = projectService.scoreProject(request, teacherPersonNo);
            return ApiResponse.success("评分成功", submission);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 统计提交情况
     * GET /api/project/stats?courseId=xxx
     */
    @GetMapping("/stats")
    public ApiResponse<ProjectStatsVO> getProjectStats(@RequestParam Integer courseId) {
        try {
            ProjectStatsVO stats = projectService.getProjectStats(courseId);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}


