package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Comment;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.request.CommentRequest;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.security.UserPrincipal;

public interface CommentService {

    PagedResponse<Comment> getAllComments(Long postId, int page, int size);

    Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser);

    Comment getComment(Long postId, Long id);

    Comment updateComment(Long postId, Long id, CommentRequest commentRequest, UserPrincipal currentUser);

    ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser);

}
