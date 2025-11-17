package com.coursemanage.module.courseclass.mapper;

import com.coursemanage.module.courseclass.pojo.CourseClass;
import org.apache.ibatis.annotations.Mapper;
import com.coursemanage.module.student.pojo.Student;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseClassMapper {
    void insertCourseClass(CourseClass cc); // 插入主班级表（不含学生）
    void insertClassStudent(Map<String, Object> map); // 插入班级-学生多对多（student_no）
    List<CourseClass> getCourseClasses(Integer courseId, String teacherNo);
    CourseClass getCourseClassById(Integer id);
    void updateCourseClass(Integer id, String className, String teacherNo);
    void deleteCourseClass(Integer id);
    Integer countStudentsInClass(Integer classId);
    List<Student> getStudentsByClassId(Integer classId);
    void deleteClassStudentsByClassId(Integer classId);


}
