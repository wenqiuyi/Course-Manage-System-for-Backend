package com.coursemanage.module.announcement.service.impl;

import com.coursemanage.module.announcement.mapper.AnnouncementMapper;
import com.coursemanage.module.log.mapper.LogMapper;
import com.coursemanage.module.announcement.pojo.AnnouncementEntity;
import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.announcement.service.AnnouncementService;
import com.coursemanage.pojo.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementMapper announcementMapper;
    private final LogMapper logMapper;
    private String getCurrentSchoolNum(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }
    @Override
    public List<AnnouncementEntity> showAll() {
        return announcementMapper.select();
    }

    @Override
    @Transactional
    public ResponseResult<Map<String, Object>> addOne(AnnouncementEntity announcementEntity) {
        int result = announcementMapper.insert(announcementEntity);
        if(result == 1){
            String operatorSchoolNum = getCurrentSchoolNum();
            LogEntity logEntity = LogEntity.builder()
                    .operatorSchoolNum(operatorSchoolNum)
                    .detailInfo("添加了一条公告")
                    .operationModule("announcement")
                    .operationAction("create")
                    .operationTime(new Date())
                    .build();
            logMapper.insert(logEntity);
            return ResponseResult.success();
        }else{
            return ResponseResult.error();
        }
    }

    @Override
    @Transactional
    public ResponseResult<Map<String, Object>> updateOne(AnnouncementEntity announcementEntity) {
        int result = announcementMapper.updateById(announcementEntity);
        if(result == 1){
            String operatorSchoolNum = getCurrentSchoolNum();
            LogEntity logEntity = LogEntity.builder()
                    .operatorSchoolNum(operatorSchoolNum)
                    .detailInfo("修改了一条公告")
                    .operationModule("announcement")
                    .operationAction("update")
                    .operationTime(new Date())
                    .build();
            logMapper.insert(logEntity);
            return ResponseResult.success();
        }else{
            return ResponseResult.error();
        }
    }

    @Override
    public ResponseResult<Map<String, Object>> updateStatus(Long id, String status) {
        AnnouncementEntity announcementEntity = announcementMapper.selectById(id);
        if(announcementEntity != null){
            announcementEntity.setStatus(status);
            return updateOne(announcementEntity);
        }
        return ResponseResult.error();
    }
}
