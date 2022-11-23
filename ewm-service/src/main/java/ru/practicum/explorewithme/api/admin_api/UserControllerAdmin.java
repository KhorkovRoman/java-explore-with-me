package ru.practicum.explorewithme.api.admin_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.dtos.user.UserDto;
import ru.practicum.explorewithme.services.user.UserService;
import ru.practicum.explorewithme.dtos.user.NewUserRequest;
import ru.practicum.explorewithme.mappers.UserMapper;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
public class UserControllerAdmin {

    private final UserService userService;

    @Autowired
    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/admin/users?ids={}from={}size={}", ids, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return UserMapper.toUserDtoCollection(userService.getAllUsers(ids, pageRequest));
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Has received request to endpoint GET/admin/users/{}", userId);
        return UserMapper.toUserDto(userService.getUserById(userId));
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Has received request to endpoint POST/admin/users");
        return UserMapper.toUserDto(userService.createUser(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Has received request to endpoint DELETE/admin/users/{}", userId);
        userService.deleteUser(userId);
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}