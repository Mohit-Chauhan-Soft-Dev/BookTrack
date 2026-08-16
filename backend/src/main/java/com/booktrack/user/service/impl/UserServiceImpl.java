package com.booktrack.user.service.impl;

import com.booktrack.exception.ForbiddenException;
import com.booktrack.role.entity.Role;
import com.booktrack.role.enums.RoleName;
import com.booktrack.security.userdetails.CustomUserDetails;
import com.booktrack.user.dto.response.UserResponse;
import com.booktrack.user.entity.User;
import com.booktrack.user.mapper.UserMapper;
import com.booktrack.user.repository.UserRepository;
import com.booktrack.user.service.UserService;
import com.booktrack.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final UserValidator userValidator;
        private final UserMapper userMapper;
        private final PageResponseMapper pageResponseMapper;

        @Override
        @Transactional(readOnly = true)
        public PageResponse<UserResponse> getAllUsers(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<User> userPage = userRepository.findAll(pageable);

                return pageResponseMapper.toPageResponse(
                                userPage,
                                userMapper::toResponse);
        }

        @Override
        @Transactional(readOnly = true)
        public UserResponse getUserById(
                        Long userId,
                        Authentication authentication) {

                User user = userValidator.validateUserExists(userId);

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                User authenticatedUser = userDetails.getUser();

                boolean isAdmin = authenticatedUser.getRoles()
                                .stream()
                                .anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN
                                                || role.getName() == RoleName.ROLE_SUPER_ADMIN);

                boolean isOwner = authenticatedUser.getId()
                                .equals(userId);

                if (!isAdmin && !isOwner) {
                        throw new ForbiddenException(
                                        "You are not authorized to view this user.");
                }

                return userMapper.toResponse(user);
        }

        @Override
        public UserResponse updateUserRoles(
                        Long userId,
                        Set<RoleName> roleNames,
                        Authentication authentication) {

                User targetUser = userValidator.validateUserExists(userId);

                boolean isSuperAdmin = hasAuthority(
                                authentication,
                                "ROLE_SUPER_ADMIN");

                boolean isAdmin = hasAuthority(
                                authentication,
                                "ROLE_ADMIN");

                if (!isSuperAdmin && !isAdmin) {
                        throw new ForbiddenException(
                                        "You are not authorized to manage user roles.");
                }

                if (isAdmin && !isSuperAdmin) {

                        if (roleNames.contains(RoleName.ROLE_SUPER_ADMIN)) {
                                throw new ForbiddenException(
                                                "Admin cannot assign ROLE_SUPER_ADMIN.");
                        }

                        boolean targetIsSuperAdmin = targetUser.getRoles()
                                        .stream()
                                        .anyMatch(role -> role.getName() == RoleName.ROLE_SUPER_ADMIN);

                        if (targetIsSuperAdmin) {
                                throw new ForbiddenException(
                                                "ADMIN cannot modify a SUPER_ADMIN.");
                        }
                }

                if (targetUser.getId().equals(
                                getAuthenticatedUserId(authentication))) {

                        throw new ForbiddenException(
                                        "You cannot modify your own roles.");
                }

                Set<Role> roles = userValidator.validateRoles(roleNames);

                targetUser.setRoles(roles);

                User savedUser = userRepository.save(targetUser);

                return userMapper.toResponse(savedUser);
        }

        private boolean hasAuthority(
                        Authentication authentication,
                        String authority) {

                return authentication.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .anyMatch(authority::equals);
        }

        private Long getAuthenticatedUserId(
                        Authentication authentication) {

                Object principal = authentication.getPrincipal();

                if (principal instanceof com.booktrack.security.userdetails.CustomUserDetails userDetails) {
                        return userDetails.getUser().getId();
                }

                throw new ForbiddenException(
                                "Unable to determine authenticated user.");
        }
}