package com.coursemanage.module.comment.service;

import com.coursemanage.module.comment.pojo.CourseComment;
import com.coursemanage.module.course.pojo.Course;

import java.util.List;

public interface CommentService {
    List<CourseComment> getCommentsByCourseId(Integer courseId);
    void createComment(CourseComment comment);

    void deleteComment(Integer id);
}
