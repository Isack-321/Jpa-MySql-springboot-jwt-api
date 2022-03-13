package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Todo;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.security.UserPrincipal;

public interface TodoService {
    Todo completeTodo(Long id, UserPrincipal currentUser);

    Todo unCompleteTodo(Long id, UserPrincipal currentUser);

    PagedResponse<Todo> getAllTodos(UserPrincipal currentUser, int page, int size);
    Todo getTodo(Long id, UserPrincipal user);
    Todo addTodo(Todo todo, UserPrincipal user);
    Todo updateTodo(Long id, Todo newTodo, UserPrincipal currentUser);

    ApiResponse deleteTodo(Long id, UserPrincipal currentUser);
}
