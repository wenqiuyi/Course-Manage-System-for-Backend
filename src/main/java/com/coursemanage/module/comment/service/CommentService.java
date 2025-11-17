package com.coursemanage.module.comment.service;

import com.coursemanage.module.comment.pojo.Comment;
import java.util.List;

public interface CommentService {
    List<Comment> getCommentsByCourseId(Integer courseId);
    void createComment(Comment comment);
}
