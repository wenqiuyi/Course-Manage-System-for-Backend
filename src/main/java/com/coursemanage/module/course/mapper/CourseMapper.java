package com.coursemanage.module.course.mapper;

import com.coursemanage.module.course.pojo.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("SELECT id, name, description, " +
            "comment_area AS commentArea, " +
            "aca_year AS acaYear, " +
            "semester, teacher_no AS teacherNo " +
            "FROM course WHERE teacher_no = #{teacherNo}")
    List<Course> getCourses(@Param("teacherNo") String teacherNo);
    @Select("SELECT id, name, description, " +
            "comment_area AS commentArea, " +
            "aca_year AS acaYear, " +
            "semester, teacher_no AS teacherNo " +
            "FROM course")
    List<Course> getAllCourses();

    @Select("SELECT id, name, description, " +
            "comment_area AS commentArea, " +
            "aca_year AS acaYear, " +
            "semester, teacher_no AS teacherNo " +
            "FROM course WHERE id = #{id}")
    Course getCourseById(@Param("id") Integer id);

    @Update("""
        UPDATE course
        SET
        name = #{name},
        description = #{description},
        comment_area = #{commentArea},
        aca_year = #{acaYear},
        semester = #{semester},
        teacher_no = #{teacherNo}
        WHERE id = #{id}
        """)
    int updateById(Course course);
}
