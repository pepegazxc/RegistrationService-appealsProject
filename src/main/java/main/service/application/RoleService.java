package main.service.application;

import lombok.RequiredArgsConstructor;
import main.entity.RolesEntity;
import main.exception.user.RoleNotFoundException;
import main.repository.RolesRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolesRepository rolesRepository;

    public RolesEntity findRole(String role) {
        return rolesRepository.findByRoleName(role.trim().toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException());
    }
}
