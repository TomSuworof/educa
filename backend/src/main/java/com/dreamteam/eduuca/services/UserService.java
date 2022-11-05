package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Role;
import com.dreamteam.eduuca.entities.RoleEnum;
import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.payload.request.SignupRequest;
import com.dreamteam.eduuca.payload.request.UserDataRequest;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.UserDTO;
import com.dreamteam.eduuca.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    public User loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public User getUserFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException();
        }
        return loadUserByUsername(authentication.getName());
    }

    public UserDTO saveUser(SignupRequest signupRequest) {
        if (existsByUsername(signupRequest.getUsername())) {
            throw new IllegalArgumentException();
        }

        if (existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException();
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRoles(Set.of(RoleEnum.USER.getAsObject()));
        userRepository.save(user);

//        Executors.newSingleThreadExecutor().submit(() -> {
//            try {
//                mailService.sendRegistrationConfirm(user.getEmail(), user.getUsername());
//            } catch (EmailException e) {
//                throw new RuntimeException(e);
//            }
//        });

        return new UserDTO(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO updateUser(UUID userId, UserDataRequest userData) {
        User userFromDB = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        if (userData.getAvatar().isPresent()) {
            userFromDB.setAvatar(userData.getAvatar().get());
        }

        if (userData.getEmail().isPresent()) {
            userFromDB.setEmail(userData.getEmail().get());
        }

        if (userData.getBio().isPresent()) {
            userFromDB.setBio(userData.getBio().get());
        }

        if (userData.getPassword().isPresent()) {
            userFromDB.setPassword(passwordEncoder.encode(userData.getPassword().get()));
        }

        userRepository.save(userFromDB);
        return new UserDTO(userFromDB);
    }

    @Deprecated
    // should be cascaded
    private void deleteUser(UUID userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public UserDTO changeRole(UUID userId, Role role) {
        User userFromDB = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);

        userFromDB.setRoles(new HashSet<>(Collections.singletonList(role)));

//        Executors.newSingleThreadExecutor().submit(() -> {
//            try {
//                mailService.sendRoleChanged(userFromDB.getEmail(), role.getName());
//            } catch (EmailException e) {
//                throw new RuntimeException(e);
//            }
//        });

        userRepository.save(userFromDB);
        return new UserDTO(userFromDB);
    }

    public boolean isCurrentPasswordSameAs(UUID requiredUserId, String passwordAnother) {
        User requiredUser = this.loadUserById(requiredUserId);
        String requiredUserPassword = requiredUser.getPassword();
        return passwordEncoder.matches(passwordAnother, requiredUserPassword);
    }

    @Deprecated
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public PageResponseDTO<UserDTO> getUsersPaginated(Integer limit, Integer offset) {
        Page<User> users = getAllUsersPaginated(limit, offset);

        return new PageResponseDTO<>(
                offset > 0 && users.getTotalElements() > 0,
                (offset + limit) < users.getTotalElements(),
                users.stream().map(UserDTO::new).toList(),
                users.getTotalElements());
    }

    private Page<User> getAllUsersPaginated(Integer limit, Integer offset) {
        return userRepository.findAll(PageRequest.of(offset / limit, limit));
    }
}
