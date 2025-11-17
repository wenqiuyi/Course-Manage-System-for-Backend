package com.coursemanage.module.announcement.service;

import com.coursemanage.module.announcement.pojo.AnnouncementEntity;
import com.coursemanage.pojo.ResponseResult;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    List<AnnouncementEntity> showAll();
    ResponseResult<Map<String, Object>> addOne(AnnouncementEntity announcementEntity);
    ResponseResult<Map<String, Object>> updateOne(AnnouncementEntity announcementEntity);
    ResponseResult<Map<String, Object>> updateStatus(Long id, String status);
}
