package com.coursemanage.module.course.mapper;

import com.coursemanage.module.course.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getCoursesByFilter(String teacherNo, String acaYear, String semester);
    Course getCourseById(Integer id);
}
