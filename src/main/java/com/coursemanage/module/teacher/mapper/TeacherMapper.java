package com.coursemanage.module.teacher.mapper;

import com.coursemanage.module.teacher.pojo.Teacher;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TeacherMapper {
    List<Teacher> getAllTeachers();
    Teacher getByTeacherNo(String teacherNo);
}
