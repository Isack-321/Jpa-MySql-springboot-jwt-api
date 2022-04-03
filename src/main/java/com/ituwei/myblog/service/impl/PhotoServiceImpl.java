package com.ituwei.myblog.service.impl;


import  com.ituwei.myblog.exception.ResourceNotFoundException;
import  com.ituwei.myblog.exception.UnauthorizedException;
import  com.ituwei.myblog.entity.Album;
import  com.ituwei.myblog.entity.Photo;
import  com.ituwei.myblog.entity.role.RoleName;
import  com.ituwei.myblog.payload.ApiResponse;
import  com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.payload.request.PhotoRequest;
import  com.ituwei.myblog.payload.PhotoResponse;
import  com.ituwei.myblog.repository.AlbumRepository;
import  com.ituwei.myblog.repository.PhotoRepository;
import  com.ituwei.myblog.security.UserPrincipal;
import  com.ituwei.myblog.service.PhotoService;
import  com.ituwei.myblog.utils.AppConstants;
import  com.ituwei.myblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ituwei.myblog.utils.AppConstants.ALBUM;
import static com.ituwei.myblog.utils.AppConstants.CREATED_AT;
import static  com.ituwei.myblog.utils.AppConstants.ID;
import static  com.ituwei.myblog.utils.AppConstants.PHOTO;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public PagedResponse<PhotoResponse> getAllPhotos(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);
        Page<Photo> photos = photoRepository.findAll(pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        if (photos.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), photos.getNumber(), photos.getSize(),
                    photos.getTotalElements(), photos.getTotalPages(), photos.isLast());
        }
        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());

    }

    @Override
    public PhotoResponse getPhoto(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));

        return new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                photo.getThumbnailUrl(), photo.getAlbum().getId());
    }

    @Override
    public PhotoResponse updatePhoto(Long id, PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            photo.setTitle(photoRequest.getTitle());
            photo.setThumbnailUrl(photoRequest.getThumbnailUrl());
            photo.setAlbum(album);
            Photo updatedPhoto = photoRepository.save(photo);
            return new PhotoResponse(updatedPhoto.getId(), updatedPhoto.getTitle(),
                    updatedPhoto.getUrl(), updatedPhoto.getThumbnailUrl(), updatedPhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update this photo");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PhotoResponse addPhoto(PhotoRequest photoRequest, UserPrincipal currentUser) {
        Album album = albumRepository.findById(photoRequest.getAlbumId())
                .orElseThrow(() -> new ResourceNotFoundException(ALBUM, ID, photoRequest.getAlbumId()));
        if (album.getUser().getId().equals(currentUser.getId())) {
            Photo photo = new Photo(photoRequest.getTitle(), photoRequest.getUrl(), photoRequest.getThumbnailUrl(),
                    album);
            Photo newPhoto = photoRepository.save(photo);
            return new PhotoResponse(newPhoto.getId(), newPhoto.getTitle(), newPhoto.getUrl(),
                    newPhoto.getThumbnailUrl(), newPhoto.getAlbum().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add photo in this album");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deletePhoto(Long id, UserPrincipal currentUser) {
        Photo photo = photoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PHOTO, ID, id));
        if (photo.getAlbum().getUser().getId().equals(currentUser.getId())
                || currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.toString()))) {
            photoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Photo deleted successfully");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<PhotoResponse> getAllPhotosByAlbum(Long albumId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Photo> photos = photoRepository.findByAlbumId(albumId, pageable);

        List<PhotoResponse> photoResponses = new ArrayList<>(photos.getContent().size());
        for (Photo photo : photos.getContent()) {
            photoResponses.add(new PhotoResponse(photo.getId(), photo.getTitle(), photo.getUrl(),
                    photo.getThumbnailUrl(), photo.getAlbum().getId()));
        }

        return new PagedResponse<>(photoResponses, photos.getNumber(), photos.getSize(), photos.getTotalElements(),
                photos.getTotalPages(), photos.isLast());
    }
}