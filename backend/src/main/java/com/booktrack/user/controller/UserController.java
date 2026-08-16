package com.booktrack.user.controller;

import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.user.dto.request.UpdateUserRolesRequest;
import com.booktrack.user.dto.response.UserResponse;
import com.booktrack.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @GetMapping
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
        public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDirection) {

                return ResponseEntity.ok(
                                userService.getAllUsers(
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @GetMapping("/{userId}")
        @PreAuthorize("isAuthenticated()")
        public ResponseEntity<UserResponse> getUserById(
                        @PathVariable Long userId,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                userService.getUserById(
                                                userId,
                                                authentication));
        }

        @PutMapping("/{userId}/roles")
        @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
        public ResponseEntity<UserResponse> updateUserRoles(
                        @PathVariable Long userId,
                        @Valid @RequestBody UpdateUserRolesRequest request,
                        Authentication authentication) {

                return ResponseEntity.ok(
                                userService.updateUserRoles(
                                                userId,
                                                request.getRoles(),
                                                authentication));
        }
}