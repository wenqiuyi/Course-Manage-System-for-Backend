package com.coursemanage.module.log.controller;

import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogDataController {
    private final LogService logService;
    private String generateFilename(String module, String action,
                                    Date startTime, Date endTime) {
        StringBuilder filename = new StringBuilder("系统日志");

        if (module != null) {
            filename.append("_").append(module);
        }
        if (action != null) {
            filename.append("_").append(action);
        }
        if (startTime != null && endTime != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
            filename.append("_").append(sdf.format(startTime))
                    .append("_").append(sdf.format(endTime));
        }

        filename.append("_").append(System.currentTimeMillis()).append(".xlsx");
        return filename.toString();
    }
    @GetMapping({"", "/"})
    @PreAuthorize("hasAuthority('manager')")
    public List<LogEntity> log(){
        return logService.getAllLogs();
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLogsExcel(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        try {
            byte[] excelContent = logService.exportLogsToExcel(module, action, startTime, endTime);

            // 生成文件名
            String filename = generateFilename(module, action, startTime, endTime);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE,
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelContent);

        } catch (Exception e) {
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }
    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('manager')")
    public List<LogEntity> logsByModuleName(@RequestParam("module") String moduleName){
        return logService.showByModule(moduleName);
    }
}
