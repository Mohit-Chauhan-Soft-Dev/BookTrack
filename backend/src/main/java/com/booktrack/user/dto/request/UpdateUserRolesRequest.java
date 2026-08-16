package com.booktrack.user.dto.request;

import com.booktrack.role.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private Set<RoleName> roles;
}