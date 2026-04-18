package main.service.application;

import main.entity.RolesEntity;
import main.exception.user.RoleNotFoundException;
import main.repository.RolesRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RolesRepository rolesRepository;

    public RoleService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public RolesEntity findRole(String role) {
        return rolesRepository.findByRoleName(role.trim().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException());
    }
}
