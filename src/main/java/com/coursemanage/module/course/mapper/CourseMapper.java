package com.coursemanage.module.course.mapper;

import com.coursemanage.module.course.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;


@Mapper
public interface CourseMapper {
   @Select("SELECT id, name, description, " +
        "comment_area AS commentArea, " +
        "aca_year AS acaYear, " +
        "semester, teacher_no AS teacherNo " +
        "FROM course " +
        "WHERE teacher_no = #{teacherNo} " +
        "AND aca_year = #{acaYear} " +
        "AND semester = #{semester}")
List<Course> getCourses(@Param("teacherNo") String teacherNo,
                        @Param("acaYear") String acaYear,
                        @Param("semester") String semester);


    @Select("SELECT id, name, description, " +
            "comment_area AS commentArea, " +
            "aca_year AS acaYear, " +
            "semester, teacher_no AS teacherNo " +
            "FROM course WHERE id = #{id}")
    Course getCourseById(Integer id);
}
