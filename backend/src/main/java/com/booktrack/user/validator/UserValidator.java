package com.booktrack.user.validator;

import com.booktrack.exception.ResourceNotFoundException;
import com.booktrack.role.entity.Role;
import com.booktrack.role.enums.RoleName;
import com.booktrack.role.repository.RoleRepository;
import com.booktrack.user.entity.User;
import com.booktrack.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User validateUserExists(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));
    }

    public Set<Role> validateRoles(Set<RoleName> roleNames) {

        Set<Role> roles = new HashSet<>();

        for (RoleName roleName : roleNames) {

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Role not found: " + roleName
                            ));

            roles.add(role);
        }

        return roles;
    }
}