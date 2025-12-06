package com.coursemanage.module.comment.impl;

import com.coursemanage.module.comment.mapper.CommentMapper;
import com.coursemanage.module.comment.pojo.CourseComment;
import com.coursemanage.module.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CourseComment> getCommentsByCourseId(Integer courseId) {
        return commentMapper.getCommentsByCourseId(courseId);
    }

    @Override
    public void createComment(CourseComment comment) {
        commentMapper.insertComment(comment);
    }
}
