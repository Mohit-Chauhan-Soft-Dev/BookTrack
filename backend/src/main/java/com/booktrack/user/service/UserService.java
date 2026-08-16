package com.booktrack.user.service;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.role.enums.RoleName;
import com.booktrack.user.dto.response.UserResponse;
import org.springframework.security.core.Authentication;

import java.util.Set;

public interface UserService {

    PageResponse<UserResponse> getAllUsers(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    UserResponse getUserById(
        Long userId,
        Authentication authentication
);

    UserResponse updateUserRoles(
            Long userId,
            Set<RoleName> roleNames,
            Authentication authentication
    );
}