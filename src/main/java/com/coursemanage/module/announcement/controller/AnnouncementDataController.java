package com.coursemanage.module.announcement.controller;

import com.coursemanage.module.announcement.dto.AnnouncementStatusUpdateRequest;
import com.coursemanage.module.announcement.pojo.AnnouncementEntity;
import com.coursemanage.pojo.ResponseResult;
import com.coursemanage.module.announcement.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcement")
@RequiredArgsConstructor
public class AnnouncementDataController {
    private final AnnouncementService announcementService;
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('manager', 'teacher')")
    public ResponseResult<Map<String, Object>> announcementCreate(@RequestBody AnnouncementEntity announcementEntity){
        return announcementService.addOne(announcementEntity);
    }
    @PutMapping("/modify")
    @PreAuthorize("hasAnyAuthority('manager', 'teacher')")
    public ResponseResult<Map<String, Object>> announcementModify(@RequestBody AnnouncementEntity announcementEntity){
        return announcementService.updateOne(announcementEntity);
    }
    @PutMapping("/status")
    @PreAuthorize("hasAnyAuthority('manager', 'teacher')")
    public ResponseResult<Map<String, Object>> announcementModifyStatus(@RequestBody AnnouncementStatusUpdateRequest request){
        return announcementService.updateStatus(request.getId(), request.getStatus());
    }
    @GetMapping("/list")
    public List<AnnouncementEntity> announcementList(){
        return announcementService.showAll();
    }
    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('manager', 'teacher')")
    public ResponseResult<Void> announcementDelete(@RequestBody Long id){
        return announcementService.deleteOne(id);
    }
}
