package ru.practicum.explorewithme.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.users.dto.UserDto;
import ru.practicum.explorewithme.users.service.UserService;
import ru.practicum.explorewithme.users.dto.NewUserRequest;
import ru.practicum.explorewithme.users.mapper.UserMapper;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен Get запрос к эндпоинту GET/admin/users?ids={}from={}size={}", ids, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return UserMapper.toUserDtoCollection(userService.getAllUsers(ids, pageRequest));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userService.createUser(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}