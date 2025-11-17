package com.coursemanage.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coursemanage.pojo.entity.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseEntityMapper extends BaseMapper<Course> {
}


