package com.coursemanage.module.course.mapper;

import com.coursemanage.module.course.pojo.Course;
import org.apache.ibatis.annotations.*;

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
    
    // 添加课程
    @Insert("""
        INSERT INTO course(name, description, comment_area, aca_year, semester, teacher_no)
        VALUES(#{name}, #{description}, #{commentArea}, #{acaYear}, #{semester}, #{teacherNo})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);
    
    // 删除课程
    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(@Param("id") Integer id);
}