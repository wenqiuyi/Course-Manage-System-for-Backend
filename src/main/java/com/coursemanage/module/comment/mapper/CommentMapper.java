package com.coursemanage.module.comment.mapper;

import com.coursemanage.module.comment.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> getCommentsByCourseId(Integer courseId);
    void insertComment(Comment comment);
}
