package ru.practicum.explorewithme.users.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exeption.BadRequestException;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.users.dto.NewUserRequest;
import ru.practicum.explorewithme.users.dto.UserDto;
import ru.practicum.explorewithme.users.mapper.UserMapper;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.toUser(newUserRequest);
        validateUser(user);
        User userFromDataBase = userRepository.save(user);
        log.info("User id " + userFromDataBase.getId() + " has successfully created.");
        return userFromDataBase;
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(userId);

        validateUser(userId);
        if (user.getEmail() == null) {
            user.setEmail(getUserById(userId).getEmail());
        }
        validateUser(user);

        if (user.getName() == null) {
            user.setName(getUserById(userId).getName());
        }

        log.info("User id " + user.getId() + " has successfully updated.");
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("In DB has not found user id " + userId));
        log.info("User id " + userId + " has found in DB.");
        return user;
    }

    @Override
    public Collection<User> getAllUsers(@NotNull List<Long> ids, PageRequest pageRequest) {
        if (ids.isEmpty()) {
            return userRepository.getAllUsersByPage(pageRequest).stream()
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAllById(ids);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        validateUser(userId);
        log.info("User id " + userId + " has found in DB.");
        userRepository.deleteById(userId);
    }

    public void validateUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("In DB has not found user id " + userId));
    }

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new BadRequestException("Name could not be empty.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new BadRequestException("E-mail could not be empty and must to be symbol @.");
        }
        if (userRepository.findAll().contains(user.getEmail())) {
                throw new BadRequestException(
                        "User with e-mail " + user.getEmail() + " has already found in DB .");
        }
    }
}
