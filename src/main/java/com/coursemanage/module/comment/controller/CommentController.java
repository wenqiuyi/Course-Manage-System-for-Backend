package com.coursemanage.module.comment.controller;

import com.coursemanage.module.comment.dto.CommentCreateDTO;
import com.coursemanage.module.comment.pojo.CourseComment;
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
    public ApiResponse<List<CourseComment>> getComments(@PathVariable("courseId") Integer courseId) {
        if (courseId == null || courseId <= 0) {
            return ApiResponse.fail(400, "课程id不能为空");
        }
        return ApiResponse.success(commentService.getCommentsByCourseId(courseId));
    }

    // 学生提交课程评论
    @PostMapping("/comment")
    public ApiResponse<Void> postComment(@Valid @RequestBody CommentCreateDTO dto) {
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ApiResponse.fail(404, "评论不能为空");
        }
        CourseComment comment = new CourseComment();
        comment.setCourseId(dto.getCourseId());
        comment.setCommenterNo(dto.getCommenterNo());
        comment.setContent(dto.getContent());
        commentService.createComment(comment);
        return ApiResponse.successMessage("评论提交成功");
    }

    @DeleteMapping("/comment/{id}")
    public ApiResponse<String> deleteComment(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            return ApiResponse.fail(400, "评论id不能为空");
        }
        commentService.deleteComment(id);
        return ApiResponse.success("评论删除成功");
    }
}
