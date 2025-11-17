package com.coursemanage.module.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanage.module.project.dto.ProjectCreateRequest;
import com.coursemanage.module.project.dto.ProjectScoreRequest;
import com.coursemanage.module.project.dto.ProjectStatsVO;
import com.coursemanage.module.project.dto.ProjectSubmitRequest;
import com.coursemanage.module.project.dto.ProjectVO;
import com.coursemanage.module.project.entity.ProjectPractice;
import com.coursemanage.module.project.entity.ProjectSubmission;
import com.coursemanage.module.project.mapper.ProjectPracticeMapper;
import com.coursemanage.module.project.mapper.ProjectSubmissionMapper;
import com.coursemanage.pojo.entity.Course;
import com.coursemanage.pojo.mapper.CourseEntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectPracticeMapper projectMapper;

    @Autowired
    private ProjectSubmissionMapper submissionMapper;

    @Autowired
    private CourseEntityMapper courseMapper;

    /**
     * 教师发布项目
     */
    @Transactional
    public ProjectPractice createProject(ProjectCreateRequest request, String teacherPersonNo) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        // 验证教师是否有权限
        if (course.getTeacherNo() == null || !course.getTeacherNo().equals(teacherPersonNo)) {
            throw new RuntimeException("无权限操作此课程");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }

        ProjectPractice project = new ProjectPractice();
        project.setCourseId(request.getCourseId());
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStartTime(request.getStartTime());
        project.setEndTime(request.getEndTime());

        projectMapper.insert(project);
        return project;
    }

    /**
     * 获取项目列表
     */
    public List<ProjectVO> getProjectList(Integer courseId) {
        QueryWrapper<ProjectPractice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).orderByDesc("start_time");
        List<ProjectPractice> projects = projectMapper.selectList(queryWrapper);

        return projects.stream().map(project -> {
            ProjectVO vo = new ProjectVO();
            BeanUtils.copyProperties(project, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 学生提交项目
     */
    @Transactional
    public ProjectSubmission submitProject(ProjectSubmitRequest request) {
        // 验证项目是否存在
        ProjectPractice project = projectMapper.selectById(request.getPracticeId());
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }

        // 验证提交时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(project.getStartTime())) {
            throw new RuntimeException("项目尚未开始");
        }
        if (now.isAfter(project.getEndTime())) {
            throw new RuntimeException("已超过提交截止时间");
        }

        // 检查是否已经提交过
        QueryWrapper<ProjectSubmission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("practice_id", request.getPracticeId())
                .eq("student_no", request.getStudentNo());
        ProjectSubmission submission = submissionMapper.selectOne(queryWrapper);

        if (submission == null) {
            submission = new ProjectSubmission();
            submission.setPracticeId(request.getPracticeId());
            submission.setStudentNo(request.getStudentNo());
        }

        submission.setFileUrl(request.getFileUrl());
        submission.setSubmitTime(now);

        if (submission.getId() == null) {
            submissionMapper.insert(submission);
        } else {
            submissionMapper.updateById(submission);
        }
        return submission;
    }

    /**
     * 教师评分
     */
    @Transactional
    public ProjectSubmission scoreProject(ProjectScoreRequest request, String teacherPersonNo) {
        // 验证项目是否存在
        ProjectPractice project = projectMapper.selectById(request.getPracticeId());
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }

        // 验证教师权限
        Course course = courseMapper.selectById(project.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        if (course.getTeacherNo() == null || !course.getTeacherNo().equals(teacherPersonNo)) {
            throw new RuntimeException("无权限操作此项目");
        }

        // 查找学生的提交记录
        QueryWrapper<ProjectSubmission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("practice_id", request.getPracticeId())
                .eq("student_no", request.getStudentNo());
        ProjectSubmission submission = submissionMapper.selectOne(queryWrapper);
        if (submission == null) {
            throw new RuntimeException("该学生尚未提交项目");
        }

        // 更新分数
        submission.setScore(request.getScore());
        submissionMapper.updateById(submission);

        return submission;
    }

    /**
     * 统计提交情况
     */
    public ProjectStatsVO getProjectStats(Integer courseId) {
        QueryWrapper<ProjectPractice> projectWrapper = new QueryWrapper<>();
        projectWrapper.eq("course_id", courseId).orderByDesc("start_time");
        List<ProjectPractice> projects = projectMapper.selectList(projectWrapper);

        ProjectStatsVO stats = new ProjectStatsVO();
        stats.setTotalProjects((long) projects.size());

        // 统计所有项目的提交数量
        long totalSubmissions = 0;
        long gradedSubmissions = 0;

        for (ProjectPractice project : projects) {
            QueryWrapper<ProjectSubmission> submissionWrapper = new QueryWrapper<>();
            submissionWrapper.eq("practice_id", project.getId());
            List<ProjectSubmission> submissions = submissionMapper.selectList(submissionWrapper);

            long submissionCount = submissions.size();
            totalSubmissions += submissionCount;

            long gradedCount = submissions.stream()
                    .filter(s -> s.getScore() != null)
                    .count();
            gradedSubmissions += gradedCount;
        }

        stats.setSubmittedProjects(totalSubmissions);
        stats.setGradedProjects(gradedSubmissions);
        stats.setPendingProjects(stats.getTotalProjects() - totalSubmissions);

        return stats;
    }
}

