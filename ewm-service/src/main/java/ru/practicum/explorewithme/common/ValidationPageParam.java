package ru.practicum.explorewithme.common;

import org.springframework.http.HttpStatus;
import ru.practicum.explorewithme.exeption.ValidationException;

public class ValidationPageParam {

    Integer from;
    Integer size;

    public ValidationPageParam(Integer from, Integer size) {
        this.from = from;
        this.size = size;
    }

    public void validatePageParam() {
        if (from < 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Parameter 'from' cannot be < 0.");
        }
        if (size < 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Parameter 'size' cannot be < 0.");
        }
        if (from == 0 && size == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST,
                    "Parameters 'from' and 'size' cannot be equal 0 at the same time");
        }
    }
}
