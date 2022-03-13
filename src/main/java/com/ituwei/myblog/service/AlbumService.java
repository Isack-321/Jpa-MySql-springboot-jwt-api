package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.Album;
import com.ituwei.myblog.payload.AlbumResponse;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.payload.request.AlbumRequest;
import com.ituwei.myblog.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface AlbumService {
    PagedResponse<AlbumResponse> getAllAlbums(int page, int size);
    ResponseEntity<Album> getAlbum(Long id);
    ResponseEntity<Album> addAlbum(Long id, AlbumRequest albumRequest, UserPrincipal currentUser);
    ResponseEntity<AlbumResponse> updateAlbum(Long id,AlbumRequest newAlbumRequest, UserPrincipal currentUser );

    ResponseEntity<ApiResponse> deleteAlbum(Long id, UserPrincipal currentUser);

    PagedResponse<Album> getUserAlbums(String username, int page, int size);
}
