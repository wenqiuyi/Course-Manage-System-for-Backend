package com.coursemanage.module.log.mapper;

import com.coursemanage.module.log.pojo.LogEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface LogMapper {
    @Select("select * from log")
    List<LogEntity> select();
    @Select("select * from log where id=#{id}")
    LogEntity selectById(Long id);
    @Select("select * from log where operation_module=#{moduleName}")
    List<LogEntity> selectByModule(String moduleName);
    @Select("select * from log where operator_school_num= #{operatorSchoolNum}")
    List<LogEntity> selectByOperator(String operatorSchoolNum);
    @Select("select * from log where target_school_num= #{targetSchoolNum}")
    List<LogEntity> selectByTarget(String targetSchoolNum);
    @Select("select * from log where operation_action= #{operationAction}")
    List<LogEntity> selectByAction(String operationAction);
    @Select("select * from log where operation_time between #{startTime} and #{endTime}")
    List<LogEntity> selectByTimeRange(Date startTime, Date endTime);
    @Select("select * from log where operation_module= #{moduleName} and operation_action= #{operationAction}")
    List<LogEntity> selectByModuleAndAction(String moduleName, String operationAction);
    @Insert("insert into log(operation_time, operator_school_num, target_school_num, detail_info, operation_module, operation_action)" +
            "values(#{operationTime},#{operatorSchoolNum},#{targetSchoolNum},#{detailInfo}, #{operationModule}, #{operationAction})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(LogEntity logEntity);
}
