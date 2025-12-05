package com.coursemanage.module.notice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanage.module.notice.dto.NoticeCreateRequest;
import com.coursemanage.module.notice.dto.NoticeVO;
import com.coursemanage.module.notice.entity.CourseNotice;
import com.coursemanage.module.notice.mapper.CourseNoticeMapper;
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
public class NoticeService {

    @Autowired
    private CourseNoticeMapper noticeMapper;

    @Autowired
    private CourseEntityMapper courseMapper;

    /**
     * 发布课程通知
     */
    @Transactional
    public CourseNotice createNotice(NoticeCreateRequest request, String teacherPersonNo) {
        // 验证课程是否存在
        Course course = getCourseById(request.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        // 验证教师权限
        if (course.getTeacherNo() == null || !course.getTeacherNo().equals(teacherPersonNo)) {
            throw new RuntimeException("无权限操作此课程");
        }

        CourseNotice notice = new CourseNotice();
        notice.setCourseId(request.getCourseId());
        notice.setTeacherNo(teacherPersonNo);
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setPublishTime(LocalDateTime.now());

        noticeMapper.insert(notice);
        return notice;
    }

    /**
     * 获取课程通知列表
     */
    public List<NoticeVO> getNoticeList(Integer courseId) {
        QueryWrapper<CourseNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                .orderByDesc("publish_time");
        List<CourseNotice> notices = noticeMapper.selectList(queryWrapper);

        return notices.stream().map(notice -> {
            NoticeVO vo = new NoticeVO();
            BeanUtils.copyProperties(notice, vo);

            return vo;
        }).collect(Collectors.toList());
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

