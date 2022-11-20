package ru.practicum.explorewithme.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.common.ApiError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError handleBadRequestException(BadRequestException ex) {
        log.info("BadRequestException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("Request contains wrong data.")
                .status(StatusError.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiError handleForbiddenException(ForbiddenException ex) {
        log.info("ForbiddenException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("There are no rights, access has forbidden.")
                .status(StatusError.FORBIDDEN.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNoSuchElementFoundException(NotFoundException ex) {
        log.info("NotFoundException: {}", ex.getMessage());
        return ApiError.builder()
                .message(ex.getMessage())
                .reason("There are no object has requested.")
                .status(StatusError.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.info("ConflictException: {}", ex.getMessage());
        return ApiError.builder()
                .message(Objects.requireNonNull(ex.getMessage()).split(";")[0])
                .reason("Request contains invalid values.")
                .status(StatusError.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors =  ex.getBindingResult().getFieldErrors().stream()
                .peek(e -> log.info("NotValidException: {}", e.getDefaultMessage()))
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ApiError.builder()
                .errors(errors)
                .message("Validation has failed.")
                .reason("Request contains wrong data.")
                .status(StatusError.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
    }
}
