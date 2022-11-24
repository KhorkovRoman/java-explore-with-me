package ru.practicum.explorewithme.services.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.dtos.user.NewUserRequest;
import ru.practicum.explorewithme.dtos.user.UserDto;
import ru.practicum.explorewithme.models.user.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User createUser(NewUserRequest newUserRequest);

    User updateUser(Long userId, UserDto userDto);

    User getUserById(Long userId);

    Collection<User> getAllUsers(List<Long> ids, PageRequest pageRequest);

    void deleteUser(Long userId);
}
