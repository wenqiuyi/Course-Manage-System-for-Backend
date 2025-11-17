package com.coursemanage.module.courseclass.impl;

import com.coursemanage.module.courseclass.mapper.CourseClassMapper;
import com.coursemanage.module.student.mapper.StudentMapper;
import com.coursemanage.module.courseclass.pojo.CourseClass;
import com.coursemanage.module.student.pojo.Student;
import com.coursemanage.module.courseclass.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseClassServiceImpl implements CourseClassService {
    @Autowired
    private CourseClassMapper courseClassMapper;
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void createCourseClass(CourseClass cc, List<String> studentList) {
        courseClassMapper.insertCourseClass(cc); // useGeneratedKeys 会自动回填 id
        Integer classId = cc.getId();

        if (studentList != null && !studentList.isEmpty()) {
            for (String studentNo : studentList) {
                // 检查学生是否存在
                Student s = studentMapper.getByStudentNo(studentNo);
                if (s == null) {
                    // 如果学生不存在，则插入一个默认学生记录
                    Student newStudent = new Student();
                    newStudent.setStudentNo(studentNo);
                    newStudent.setName("未命名学生"); // 默认名，可按需改
                    newStudent.setGender("U");        // Unknown
                    newStudent.setDepartment("未分配");
                    newStudent.setMajor("未分配");
                    newStudent.setRoleType("学生");

                    studentMapper.insertStudent(newStudent);
                    s = newStudent; // 更新引用
                }

                // 插入班级-学生关联
                Map<String, Object> map = new HashMap<>();
                map.put("classId", classId);
                map.put("studentNo", s.getStudentNo());
                courseClassMapper.insertClassStudent(map);
            }
        }
    }


    @Override
    public List<CourseClass> getCourseClasses(Integer courseId, String teacherNo) {
        return courseClassMapper.getCourseClasses(courseId, teacherNo);
    }

    @Override
    public CourseClass getCourseClassById(Integer id) {
        CourseClass cc = courseClassMapper.getCourseClassById(id);
        if (cc != null) {
            // 查询该班级的所有学生并填充
            List<Student> students = courseClassMapper.getStudentsByClassId(id);
            cc.setStudents(students);
        }
        return cc;
    }


    @Override
    public void updateCourseClass(Integer id, CourseClass update) {
        courseClassMapper.updateCourseClass(id, update.getClassName(), update.getTeacherNo());
    }


    @Transactional
    @Override
    public void deleteCourseClass(Integer id) {
        // 先删除该班级的学生关联
        courseClassMapper.deleteClassStudentsByClassId(id);

        // 再删除班级本身
        courseClassMapper.deleteCourseClass(id);
    }

}
