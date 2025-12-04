package com.coursemanage.module.coursereport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.module.coursereport.pojo.CourseReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseReportMapper extends BaseMapper<CourseReport> {
}
