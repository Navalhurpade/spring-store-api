package com.naval.store.services;

import com.naval.store.dtos.users.RegisterUserRequest;
import com.naval.store.dtos.users.UpdateUserRequest;
import com.naval.store.entities.Role;
import com.naval.store.entities.User;
import com.naval.store.exceptions.auth.UserAlreadyExistException;
import com.naval.store.exceptions.auth.UserNotFoundException;
import com.naval.store.mappers.UserMapper;
import com.naval.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public List<User> getUsers(String sort) {
        if (!Set.of("name", "email").contains(sort)) {
            sort = "name";
        }
        return userRepository.findAll(Sort.by(sort));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public User registerUser(RegisterUserRequest request) {
        var found = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(found != null) {
            throw new UserAlreadyExistException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User updateUser(Long userId, UpdateUserRequest request) {
        var found = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userMapper.toRequestUpdate(request, found);

        return userRepository.save(found);
    }

    public void deleteUser(Long userId) {
        var found = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userRepository.delete(found);
    }

}
