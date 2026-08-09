package com.booktrack.config;

import com.booktrack.role.entity.Role;
import java.util.Objects;
import com.booktrack.role.enums.RoleName;
import com.booktrack.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        initializeRole(RoleName.ROLE_SUPER_ADMIN);
        initializeRole(RoleName.ROLE_ADMIN);
        initializeRole(RoleName.ROLE_LIBRARIAN);
        initializeRole(RoleName.ROLE_ASSISTANT_LIBRARIAN);
        initializeRole(RoleName.ROLE_FACULTY);
        initializeRole(RoleName.ROLE_STUDENT);

        System.out.println("========================================");
        System.out.println(" Default roles initialized successfully ");
        System.out.println("========================================");
    }

    private void initializeRole(RoleName roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {

            Role role = Role.builder()
                    .name(roleName)
                    .build();

            roleRepository.save(Objects.requireNonNull(role, "role must not be null"));

            System.out.println("Created Role : " + roleName);
        }
    }
}