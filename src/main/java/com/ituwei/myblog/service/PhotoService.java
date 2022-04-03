package com.ituwei.myblog.service;

import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.payload.request.PhotoRequest;
import com.ituwei.myblog.payload.PhotoResponse;
import com.ituwei.myblog.security.UserPrincipal;

public interface PhotoService {

    PagedResponse<PhotoResponse> getAllPhotos(int page, int size);

    PhotoResponse getPhoto(Long id);

    PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser);

    PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser);

    ApiResponse deletePhoto(Long id, UserPrincipal currentUser);

    PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size);

}
