package com.ituwei.myblog.controller;

import com.ituwei.myblog.exception.ResponseEntityErrorException;
import com.ituwei.myblog.entity.Album;
import com.ituwei.myblog.payload.AlbumResponse;
import com.ituwei.myblog.payload.ApiResponse;
import com.ituwei.myblog.payload.PagedResponse;
import com.ituwei.myblog.payload.PhotoResponse;
import com.ituwei.myblog.payload.request.AlbumRequest;
import com.ituwei.myblog.security.CurrentUser;
import com.ituwei.myblog.security.UserPrincipal;
import com.ituwei.myblog.service.AlbumService;
import com.ituwei.myblog.service.PhotoService;
import com.ituwei.myblog.utils.AppConstants;
import com.ituwei.myblog.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    @ExceptionHandler(ResponseEntityErrorException.class)
    public ResponseEntity<ApiResponse> handleExceptions(ResponseEntityErrorException exception) {
        return exception.getApiResponse();
    }

    @GetMapping
    public PagedResponse<AlbumResponse> getAllAlbums(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
        AppUtils.validatePageNumberAndSize(page, size);

        return albumService.getAllAlbums(page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Album> addAlbum(Long id,@Valid @RequestBody AlbumRequest albumRequest, @CurrentUser UserPrincipal currentUser) {
        return albumService.addAlbum(id,albumRequest, currentUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable(name = "id") Long id) {
        return albumService.getAlbum(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AlbumResponse> updateAlbum(@PathVariable(name = "id") Long id, @Valid @RequestBody AlbumRequest newAlbum,
                                                     @CurrentUser UserPrincipal currentUser) {
        return albumService.updateAlbum(id, newAlbum, currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteAlbum(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        return albumService.deleteAlbum(id, currentUser);
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<PagedResponse<PhotoResponse>> getAllPhotosByAlbum(@PathVariable(name = "id") Long id,
                                                                            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                                            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<PhotoResponse> response = photoService.getAllPhotosByAlbum(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}