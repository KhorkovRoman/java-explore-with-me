package ru.practicum.explorewithme.mappers;

import lombok.Data;
import ru.practicum.explorewithme.dtos.user.NewUserRequest;
import ru.practicum.explorewithme.dtos.user.UserDto;
import ru.practicum.explorewithme.models.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserMapper {

    public static List<UserDto> toUserDtoCollection(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}


