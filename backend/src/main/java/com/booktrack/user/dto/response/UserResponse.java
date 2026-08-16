package com.booktrack.user.dto.response;

import com.booktrack.role.enums.RoleName;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private boolean enabled;

    private boolean accountNonLocked;

    private boolean accountNonExpired;

    private boolean credentialsNonExpired;

    private Set<RoleName> roles;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}