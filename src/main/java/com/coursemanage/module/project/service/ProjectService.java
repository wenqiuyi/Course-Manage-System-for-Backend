package com.coursemanage.module.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.project.dto.ProjectCreateRequest;
import com.coursemanage.module.project.dto.ProjectScoreRequest;
import com.coursemanage.module.project.dto.ProjectStatsVO;
import com.coursemanage.module.project.dto.ProjectSubmitRequest;
import com.coursemanage.module.project.dto.ProjectUpdateRequest;
import com.coursemanage.module.project.dto.ProjectVO;
import com.coursemanage.module.project.dto.StudentProjectSubmissionVO;
import com.coursemanage.module.project.entity.ProjectPractice;
import com.coursemanage.module.project.entity.ProjectSubmission;
import com.coursemanage.module.project.mapper.ProjectPracticeMapper;
import com.coursemanage.module.project.mapper.ProjectSubmissionMapper;
import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.pojo.entity.Course;
import com.coursemanage.pojo.mapper.CourseEntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectPracticeMapper projectMapper;

    @Autowired
    private ProjectSubmissionMapper submissionMapper;

    @Autowired
    private CourseEntityMapper courseMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 教师发布项目
     */
    @Transactional
    public ProjectPractice createProject(ProjectCreateRequest request, String teacherPersonNo) {
        // 验证课程是否存在
        Course course = getCourseById(request.getCourseId());
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
        project.setIsGroupProject(request.getIsGroupProject());
        project.setStartTime(request.getStartTime());
        project.setEndTime(request.getEndTime());

        projectMapper.insert(project);
        return project;
    }

    /**
     * 教师修改项目
     */
    @Transactional
    public ProjectPractice updateProject(ProjectUpdateRequest request, String teacherPersonNo) {
        // 验证项目是否存在
        ProjectPractice project = projectMapper.selectById(request.getId());
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }

        // 验证教师权限
        Course course = getCourseById(project.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        if (course.getTeacherNo() == null || !course.getTeacherNo().equals(teacherPersonNo)) {
            throw new RuntimeException("无权限操作此项目");
        }

        // 验证时间
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("开始时间不能晚于结束时间");
        }

        // 更新项目信息
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setIsGroupProject(request.getIsGroupProject());
        project.setStartTime(request.getStartTime());
        project.setEndTime(request.getEndTime());

        projectMapper.updateById(project);
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
     * 查询学生已提交的项目列表
     */
    public List<StudentProjectSubmissionVO> getStudentSubmittedProjects(String studentNo) {
        QueryWrapper<ProjectSubmission> submissionWrapper = new QueryWrapper<>();
        submissionWrapper.eq("student_no", studentNo).orderByDesc("submit_time");
        List<ProjectSubmission> submissions = submissionMapper.selectList(submissionWrapper);
        if (submissions.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Integer> practiceIds = submissions.stream()
                .map(ProjectSubmission::getPracticeId)
                .collect(Collectors.toSet());
        Map<Integer, ProjectPractice> projectMap = projectMapper.selectBatchIds(practiceIds).stream()
                .collect(Collectors.toMap(ProjectPractice::getId, practice -> practice));

        return submissions.stream().map(submission -> {
            StudentProjectSubmissionVO vo = new StudentProjectSubmissionVO();
            vo.setSubmissionId(submission.getId());
            vo.setPracticeId(submission.getPracticeId());
            vo.setFileUrl(submission.getFileUrl());
            vo.setSubmitTime(submission.getSubmitTime());
            vo.setScore(submission.getScore());
            vo.setTeacherComment(submission.getTeacherComment());

            ProjectPractice practice = projectMap.get(submission.getPracticeId());
            if (practice != null) {
                vo.setCourseId(practice.getCourseId());
                vo.setTitle(practice.getTitle());
                vo.setDescription(practice.getDescription());
                vo.setStartTime(practice.getStartTime());
                vo.setEndTime(practice.getEndTime());
            }
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
        Course course = getCourseById(project.getCourseId());
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

        // 更新分数与评语
        submission.setScore(request.getScore());
        submission.setTeacherComment(request.getTeacherComment());
        submissionMapper.updateById(submission);

        return submission;
    }

    /**
     * 统计提交情况
     */
    public ProjectStatsVO getProjectStats(Integer practiceId) {
        // 验证项目是否存在
        ProjectPractice project = projectMapper.selectById(practiceId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }

        // 获取项目所属课程
        Course course = getCourseById(project.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        // 通过课程ID获取关联的学生
        List<Student> students = studentMapper.getStudentsByCourseId(course.getId());
        long totalStudents = students == null ? 0 : students.size();

        // 获取该项目的所有提交记录
        QueryWrapper<ProjectSubmission> submissionWrapper = new QueryWrapper<>();
        submissionWrapper.eq("practice_id", practiceId);
        List<ProjectSubmission> submissions = submissionMapper.selectList(submissionWrapper);

        // 统计已提交学生数
        long submittedStudents = submissions.size();

        // 统计已评分学生数（提交记录中有分数的）
        long gradedStudents = submissions.stream()
                .filter(s -> s.getScore() != null)
                .count();

        // 待提交学生数
        long pendingStudents = totalStudents - submittedStudents;

        ProjectStatsVO stats = new ProjectStatsVO();
        stats.setTotalStudents(totalStudents);
        stats.setSubmittedStudents(submittedStudents);
        stats.setGradedStudents(gradedStudents);
        stats.setPendingStudents(pendingStudents);

        return stats;
    }

    /**
     * 根据项目ID获取所有提交记录
     */
    public List<ProjectSubmission> getSubmissionsByPracticeId(Integer practiceId) {
        if (practiceId == null) {
            throw new RuntimeException("项目ID不能为空");
        }

        // 验证项目是否存在
        ProjectPractice project = projectMapper.selectById(practiceId);
        if (project == null) {
            throw new RuntimeException("项目不存在");
        }

        QueryWrapper<ProjectSubmission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("practice_id", practiceId)
                .orderByDesc("submit_time");
        List<ProjectSubmission> submissions = submissionMapper.selectList(queryWrapper);
        
        if (submissions == null || submissions.isEmpty()) {
            return Collections.emptyList();
        }
        
        return submissions;
    }

    /**
     * 仅选择现有字段，避免访问不存在的 class_id 列
     */
    private Course getCourseById(Integer courseId) {
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "description", "comment_area", "aca_year", "semester", "teacher_no")
                .eq("id", courseId);
        return courseMapper.selectOne(wrapper);
    }
}

