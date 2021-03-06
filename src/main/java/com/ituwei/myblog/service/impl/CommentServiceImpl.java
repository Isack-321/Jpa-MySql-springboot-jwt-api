package com.ituwei.myblog.service.impl;

import com.ituwei.myblog.exception.ResourceNotFoundException;
import com.ituwei.myblog.exception.BlogapiException;
import com.ituwei.myblog.entity.Comment;
import com.ituwei.myblog.entity.Post;
import com.ituwei.myblog.entity.role.RoleName;
import com.ituwei.myblog.entity.user.User;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.request.CommentRequest;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.repository.CommentRepository;
import com.ituwei.myblog.repository.PostRepository;
import com.ituwei.myblog.repository.UserRepository;
import com.ituwei.myblog.security.UserPrincipal;
import com.ituwei.myblog.service.CommentService;
import com.ituwei.myblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    private static final String THIS_COMMENT = " this comment";

    private static final String YOU_DON_T_HAVE_PERMISSION_TO = "You don't have permission to ";

    private static final String ID_STR = "id";

    private static final String COMMENT_STR = "Comment";

    private static final String POST_STR = "Post";

    private static final String COMMENT_DOES_NOT_BELONG_TO_POST = "Comment does not belong to post";


    private CommentRepository commentRepository;


    private PostRepository postRepository;

    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PagedResponse<Comment> getAllComments(Long postId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");

        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);

        return new PagedResponse<>(comments.getContent(), comments.getNumber(), comments.getSize(),
                comments.getTotalElements(), comments.getTotalPages(), comments.isLast());
    }

    @Override
    public Comment addComment(CommentRequest commentRequest, Long postId, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
        User user = userRepository.getUser(currentUser);
        Comment comment = new Comment(commentRequest.getBody());
        comment.setUser(user);
        comment.setPost(post);
        comment.setName(currentUser.getUsername());
        comment.setEmail(currentUser.getEmail());
        return commentRepository.save(comment);
    }

    @Override
    public Comment getComment(Long postId, Long id) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));
        if (comment.getPost().getId().equals(post.getId())) {
            return comment;
        }

        throw new BlogapiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
    }

    @Override
    public Comment updateComment(Long postId, Long id, CommentRequest commentRequest,
                                 UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogapiException(HttpStatus.BAD_REQUEST, COMMENT_DOES_NOT_BELONG_TO_POST);
        }

        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            comment.setBody(commentRequest.getBody());
            return commentRepository.save(comment);
        }

        throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "update" + THIS_COMMENT);
    }

    @Override
    public ApiResponse deleteComment(Long postId, Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(POST_STR, ID_STR, postId));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_STR, ID_STR, id));

        if (!comment.getPost().getId().equals(post.getId())) {
            return new ApiResponse(Boolean.FALSE, COMMENT_DOES_NOT_BELONG_TO_POST);
        }

        if (comment.getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            commentRepository.deleteById(comment.getId());
            return new ApiResponse(Boolean.TRUE, "You successfully deleted comment");
        }

        throw new BlogapiException(HttpStatus.UNAUTHORIZED, YOU_DON_T_HAVE_PERMISSION_TO + "delete" + THIS_COMMENT);
    }
}