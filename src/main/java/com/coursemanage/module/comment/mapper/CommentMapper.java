package com.coursemanage.module.comment.mapper;

import com.coursemanage.module.comment.pojo.CourseComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("""
    SELECT id,
           course_id AS courseId,
           commenter_no AS commenterNo,
           content,
           create_time AS createTime
    FROM course_comment
    WHERE course_id = #{courseId}
    """)
    List<CourseComment> getCommentsByCourseId(@Param("courseId") Integer courseId);


    @Insert("""
        INSERT INTO course_comment(course_id, commenter_no, content)
        VALUES(#{courseId}, #{commenterNo}, #{content})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertComment(CourseComment comment);

    @Delete("""
    DELETE FROM course_comment
    WHERE id = #{id}
    """)
    void deleteComment(@Param("id") Integer id);

}
