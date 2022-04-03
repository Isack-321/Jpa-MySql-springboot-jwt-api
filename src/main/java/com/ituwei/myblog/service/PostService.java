package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Post;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.payload.request.PostRequest;
import com.ituwei.myblog.payload.PostResponse;
import com.ituwei.myblog.security.UserPrincipal;

public interface PostService {
    PagedResponse<Post> getAllPosts(int page, int size);

    PagedResponse<Post> getPostsByCreatedBy(String username, int page, int size);

    PagedResponse<Post> getPostsByCategory(Long id, int page, int size);

    PagedResponse<Post> getPostsByTag(Long id, int page, int size);

    Post updatePost(Long id, PostRequest newPostRequest, UserPrincipal currentUser);

    ApiResponse deletePost(Long id, UserPrincipal currentUser);

    PostResponse addPost(PostRequest postRequest, UserPrincipal currentUser);

    Post getPost(Long id);

}
