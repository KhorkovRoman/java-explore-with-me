package ru.practicum.explorewithme.users.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.users.dto.NewUserRequest;
import ru.practicum.explorewithme.users.dto.UserDto;
import ru.practicum.explorewithme.users.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User createUser(NewUserRequest newUserRequest);

    User updateUser(Long userId, UserDto userDto);

    User getUserById(Long userId);

    Collection<User> getAllUsers(List<Long> ids, PageRequest pageRequest);

    void deleteUser(Long userId);
}
