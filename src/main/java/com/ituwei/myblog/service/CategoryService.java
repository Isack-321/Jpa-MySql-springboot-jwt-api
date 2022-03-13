package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Category;
import com.ituwei.myblog.exception.UnauthorizedException;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    PagedResponse<Category> getAllCategories(int page, int size);

    ResponseEntity<Category> getCategory(Long id);

    ResponseEntity<Category> addCategory(Category category, UserPrincipal currentUser);

    ResponseEntity<Category> updateCategory(Long id, Category newCategory, UserPrincipal currentUser)
            throws UnauthorizedException;

    ResponseEntity<ApiResponse> deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException;

}
