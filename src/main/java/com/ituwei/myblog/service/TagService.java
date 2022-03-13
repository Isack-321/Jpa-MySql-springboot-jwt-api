package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Tag;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.security.UserPrincipal;

public interface TagService {
    PagedResponse<Tag> getAllTags(int page, int size);

    Tag getTag(Long id);

    Tag addTag(Tag tag, UserPrincipal currentUser);

    Tag updateTag(Long id, Tag newTag, UserPrincipal currentUser);

    ApiResponse deleteTag(Long id, UserPrincipal currentUser);

}
