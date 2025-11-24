package com.coursemanage.module.checkin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coursemanage.module.checkin.dto.CheckinRecordVO;
import com.coursemanage.module.checkin.dto.CheckinStartRequest;
import com.coursemanage.module.checkin.dto.CheckinSubmitRequest;
import com.coursemanage.module.checkin.entity.Checkin;
import com.coursemanage.module.checkin.entity.CheckinRecord;
import com.coursemanage.module.checkin.mapper.CheckinMapper;
import com.coursemanage.module.checkin.mapper.CheckinRecordMapper;
import com.coursemanage.pojo.entity.Course;
import com.coursemanage.pojo.mapper.CourseEntityMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinService {

    @Autowired
    private CheckinMapper checkinMapper;

    @Autowired
    private CheckinRecordMapper checkinRecordMapper;

    @Autowired
    private CourseEntityMapper courseMapper;

    /**
     * 教师发起签到
     */
    @Transactional
    public Checkin startCheckin(CheckinStartRequest request, String teacherPersonNo) {
        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        // 调试日志
        System.out.println("DEBUG: course.getTeacherNo() = " + course.getTeacherNo());
        System.out.println("DEBUG: teacherPersonNo = " + teacherPersonNo);
        System.out.println("DEBUG: equals check = " + (course.getTeacherNo() != null && course.getTeacherNo().equals(teacherPersonNo)));

        if (course.getTeacherNo() == null || !course.getTeacherNo().equals(teacherPersonNo)) {
            throw new RuntimeException("无权限操作此课程: course.teacherNo=" + course.getTeacherNo() + ", header.teacherPersonNo=" + teacherPersonNo);
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("签到开始时间不能晚于结束时间");
        }

        Checkin checkin = new Checkin();
        checkin.setCourseId(request.getCourseId());
        checkin.setTitle(request.getTitle());
        checkin.setDescription(request.getDescription());
        checkin.setStartTime(request.getStartTime());
        checkin.setEndTime(request.getEndTime());

        checkinMapper.insert(checkin);
        return checkin;
    }

    /**
     * 学生提交签到
     */
    @Transactional
    public CheckinRecord submitCheckin(CheckinSubmitRequest request) {
        Checkin checkin = checkinMapper.selectById(request.getCheckinId());
        if (checkin == null) {
            throw new RuntimeException("签到不存在");
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(checkin.getStartTime())) {
            throw new RuntimeException("签到尚未开始");
        }
        if (now.isAfter(checkin.getEndTime())) {
            throw new RuntimeException("签到已结束");
        }

        QueryWrapper<CheckinRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkin_id", request.getCheckinId())
                .eq("student_no", request.getStudentNo());
        CheckinRecord existingRecord = checkinRecordMapper.selectOne(queryWrapper);
        if (existingRecord != null) {
            throw new RuntimeException("您已经签到过了");
        }

        CheckinRecord record = new CheckinRecord();
        record.setCheckinId(request.getCheckinId());
        record.setStudentNo(request.getStudentNo());
        record.setCheckinTime(now);
        record.setStatus(true); // 1=已签到

        checkinRecordMapper.insert(record);
        return record;
    }

    /**
     * 获取签到记录
     */
    public List<CheckinRecordVO> getCheckinRecords(Integer checkinId) {
        QueryWrapper<CheckinRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkin_id", checkinId);
        List<CheckinRecord> records = checkinRecordMapper.selectList(queryWrapper);
        return records.stream()
                .map(this::buildRecordVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据课程ID获取所有签到记录
     */
    public List<CheckinRecordVO> getCheckinRecordsByCourseId(Integer courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        QueryWrapper<Checkin> checkinQuery = new QueryWrapper<>();
        checkinQuery.eq("course_id", courseId);
        List<Checkin> checkins = checkinMapper.selectList(checkinQuery);
        if (checkins == null || checkins.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> checkinIds = checkins.stream()
                .map(Checkin::getId)
                .collect(Collectors.toList());

        QueryWrapper<CheckinRecord> recordQuery = new QueryWrapper<>();
        recordQuery.in("checkin_id", checkinIds);
        List<CheckinRecord> records = checkinRecordMapper.selectList(recordQuery);
        return records.stream()
                .map(this::buildRecordVO)
                .collect(Collectors.toList());
    }

    private CheckinRecordVO buildRecordVO(CheckinRecord record) {
        CheckinRecordVO vo = new CheckinRecordVO();
        BeanUtils.copyProperties(record, vo);
        // TODO: replace with actual student name lookup when available
        vo.setStudentName(record.getStudentNo());
        return vo;
    }
}
