package com.coursemanage.module.log.service;


import com.coursemanage.module.log.pojo.LogEntity;

import java.util.Date;
import java.util.List;

public interface LogService {
    List<LogEntity> getAllLogs();
    List<LogEntity> getLogsByModule(String module);
    List<LogEntity> getLogsByAction(String action);
    List<LogEntity> getLogsByOperator(String operatorSchoolNum);
    List<LogEntity> getLogsByTimeRange(Date startTime, Date endTime);
    byte[] exportLogsToExcel(String module, String action, Date startTime, Date endTime);

    List<LogEntity> showByModule(String moduleName);
    int addOne(LogEntity logEntity);
}
