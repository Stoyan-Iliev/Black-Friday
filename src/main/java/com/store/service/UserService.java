package com.store.service;

import com.store.entity.ERole;
import com.store.entity.Purchase;
import com.store.entity.Role;
import com.store.entity.User;
import com.store.exception.EmailMismatchException;
import com.store.exception.RoleNotFoundException;
import com.store.exception.UserNotFoundException;
import com.store.payload.request.UpgradeRequest;
import com.store.repository.PurchaseRepository;
import com.store.repository.RoleRepository;
import com.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PurchaseRepository purchaseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public void upgradeUser(UpgradeRequest upgradeRequest) {
        User user = getUser(upgradeRequest);
        ensureEmailsMatch(upgradeRequest, user);

        assignRoles(upgradeRequest.getRoles(), user);

        userRepository.save(user);
    }

    private User getUser(UpgradeRequest upgradeRequest) {
        return getUserByUsername(upgradeRequest.getUsername());
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("There is no user with username: " +
                        username));
    }

    private void ensureEmailsMatch(UpgradeRequest upgradeRequest, User user) {
        if(!user.getEmail().equals(upgradeRequest.getEmail())){
            throw new EmailMismatchException("Wrong username or email");
        }
    }

    private void assignRoles(Set<String> strRoles, User user) {
        Set<Role> roles = mapRoles(strRoles);

        user.setRoles(roles);
    }

    private Set<Role> mapRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (isNull(strRoles)) {
            roles.add(getRole(ERole.ROLE_CLIENT));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(getRole(ERole.ROLE_ADMIN));
                        break;
                    case "employee":
                        roles.add(getRole(ERole.ROLE_EMPLOYEE));
                        break;
                    default:
                        roles.add(getRole(ERole.ROLE_CLIENT));
                }
            });
        }
        return roles;
    }

    private boolean isNull(Set<String> strRoles) {
        return strRoles == null;
    }

    private Role getRole(ERole roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Error: Role is not found."));
    }

    public void deleteUser(String username) {
        User user = getUserByUsername(username);
        List<Purchase> purchases = purchaseRepository.findAllByUser(user);
        purchases.forEach(purchase -> purchase.setUser(null));
        purchaseRepository.saveAll(purchases);
        userRepository.deleteById(user.getId());
    }
}
