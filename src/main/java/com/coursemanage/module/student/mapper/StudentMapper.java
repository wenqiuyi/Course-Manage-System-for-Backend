package com.coursemanage.module.student.mapper;

import com.coursemanage.module.student.pojo.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    /* ========= 统一的结果映射 ========= */
    @Results(id = "StudentMap", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "ava_url", property = "avaUrl"),
            @Result(column = "name", property = "name"),
            @Result(column = "student_no", property = "studentNo"),
            @Result(column = "gender", property = "gender"),
            @Result(column = "role_type", property = "roleType"),
            @Result(column = "department", property = "department"),
            @Result(column = "major", property = "major"),
            @Result(column = "course_id", property = "courseId")
    })


    @Select("""
        SELECT id, ava_url, name, student_no, gender, role_type,
               department, major, course_id
        FROM student
    """)
    List<Student> getAllStudents();



    @Select("""
        SELECT id, ava_url, name, student_no, gender, role_type,
               department, major, course_id
        FROM student
        WHERE student_no = #{studentNo}
    """)
    @ResultMap("StudentMap")
    Student getByStudentNo(@Param("studentNo") String studentNo);



    @Select("""
        SELECT id, ava_url, name, student_no, gender, role_type,
               department, major, course_id
        FROM student
        WHERE course_id = #{courseId}
    """)
    @ResultMap("StudentMap")
    List<Student> getStudentsByCourseId(@Param("courseId") Integer courseId);



    @Insert("""
        INSERT INTO student
        (ava_url, name, student_no, gender, role_type,
         department, major, course_id)
        VALUES
        (#{avaUrl}, #{name}, #{studentNo}, #{gender}, #{roleType},
         #{department}, #{major}, #{courseId})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStudent(Student student);

    @Update("UPDATE student SET course_id = #{courseId} WHERE id = #{studentNo}")
    int assignStudentToCourse(@Param("studentNo") String studentNo, @Param("courseId") Integer courseId);

    // 从班级中移除学生
    @Update("UPDATE student SET course_id = NULL WHERE id = #{studentNo}")
    int removeStudentFromCourse(@Param("studentNo") String studentNo);

    @Update({
            "<script>",
            "UPDATE student",
            "SET course_id = #{courseId}",
            "WHERE student_no IN",
            "<foreach item='no' collection='studentNos' open='(' separator=',' close=')'>",
            "#{no}",
            "</foreach>",
            "</script>"
    })
    int batchAssignStudents(List<String> studentNos, Integer courseId);
}
