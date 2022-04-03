package com.ituwei.myblog.service;

import com.ituwei.myblog.entity.user.User;
import com.ituwei.myblog.payload.*;
import com.ituwei.myblog.payload.request.InfoRequest;
import com.ituwei.myblog.security.UserPrincipal;

public interface UserService {

    UserSummary getCurrentUser(UserPrincipal currentUser);

    UserIdentityAvailability checkUsernameAvailability(String username);

    UserIdentityAvailability checkEmailAvailability(String email);

    UserProfile getUserProfile(String username);

    User addUser(User user);

    User updateUser(User newUser, String username, UserPrincipal currentUser);

    ApiResponse deleteUser(String username, UserPrincipal currentUser);

    ApiResponse giveAdmin(String username);

    ApiResponse removeAdmin(String username);

    UserProfile setOrUpdateInfo(UserPrincipal currentUser, InfoRequest infoRequest);

}
