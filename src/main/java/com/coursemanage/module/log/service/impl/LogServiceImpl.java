package com.coursemanage.module.log.service.impl;

import com.coursemanage.module.log.mapper.LogMapper;
import com.coursemanage.module.log.pojo.LogEntity;
import com.coursemanage.module.log.service.LogService;
import com.coursemanage.module.announcement.util.ExcelGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogMapper logMapper;
    private final ExcelGenerator excelGenerator;
    private String getCurrentSchoolNum(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return authentication.getName();
    }

    @Override
    public List<LogEntity> getAllLogs() {
        return logMapper.select();
    }

    @Override
    public List<LogEntity> getLogsByModule(String module) {
        return logMapper.selectByModule(module);
    }

    @Override
    public List<LogEntity> getLogsByAction(String action) {
        return logMapper.selectByAction(action);
    }

    @Override
    public List<LogEntity> getLogsByOperator(String operatorSchoolNum) {
        return logMapper.selectByOperator(operatorSchoolNum);
    }

    @Override
    public List<LogEntity> getLogsByTimeRange(Date startTime, Date endTime) {
        return logMapper.selectByTimeRange(startTime, endTime);
    }

    @Override
    public byte[] exportLogsToExcel(String module, String action, Date startTime, Date endTime) {
        try {
            List<LogEntity> logs;
            String sheetName;

            if (module != null && action != null) {
                logs = logMapper.selectByModuleAndAction(module, action);
                sheetName = module + "_" + action + "_日志";
            } else if (module != null) {
                logs = logMapper.selectByModule(module);
                sheetName = module + "_模块日志";
            } else if (action != null) {
                logs = logMapper.selectByAction(action);
                sheetName = action + "_操作日志";
            } else if (startTime != null && endTime != null) {
                logs = logMapper.selectByTimeRange(startTime, endTime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                sheetName = sdf.format(startTime) + "_" + sdf.format(endTime) + "_日志";
            } else {
                logs = getAllLogs();
                sheetName = "全部系统日志";
            }

            log.info("导出日志Excel，数量: {}", logs.size());
            return excelGenerator.generateLogsExcel(logs, sheetName);

        } catch (Exception e) {
            log.error("导出日志Excel失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    @Override
    public List<LogEntity> showByModule(String moduleName) {
        return logMapper.selectByModule(moduleName);
    }

    @Override
    public int addOne(LogEntity logEntity) {
        return logMapper.insert(logEntity);
    }
}
