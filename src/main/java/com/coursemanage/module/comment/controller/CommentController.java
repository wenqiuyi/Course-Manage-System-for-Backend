package com.coursemanage.module.comment.controller;

import com.coursemanage.module.comment.dto.CommentCreateDTO;
import com.coursemanage.module.comment.pojo.Comment;
import com.coursemanage.module.comment.service.CommentService;
import com.coursemanage.module.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 获取课程评论
    @GetMapping("/comments/{courseId}")
    public ApiResponse<List<Comment>> getComments(@PathVariable("courseId") Integer courseId) {
        return ApiResponse.success(commentService.getCommentsByCourseId(courseId));
    }

    // 学生提交课程评论
    @PostMapping("/comment")
    public ApiResponse<Void> postComment(@Valid @RequestBody CommentCreateDTO dto) {
        Comment comment = new Comment();
        comment.setCourseId(dto.getCourseId());
        comment.setCommenterId(dto.getCommenterId());
        comment.setContent(dto.getContent());
        commentService.createComment(comment);
        return ApiResponse.successMessage("评论提交成功");
    }
}
