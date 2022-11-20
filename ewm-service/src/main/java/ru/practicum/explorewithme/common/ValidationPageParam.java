package ru.practicum.explorewithme.common;

import ru.practicum.explorewithme.exeption.BadRequestException;

public class ValidationPageParam {

    Integer from;
    Integer size;

    public ValidationPageParam(Integer from, Integer size) {
        this.from = from;
        this.size = size;
    }

    public void validatePageParam() {
        if (from < 0) {
            throw new BadRequestException(
                    "Parameter 'from' cannot be < 0.");
        }
        if (size < 0) {
            throw new BadRequestException(
                    "Parameter 'size' cannot be < 0.");
        }
        if (from == 0 && size == 0) {
            throw new BadRequestException(
                    "Parameters 'from' and 'size' cannot be equal 0 at the same time");
        }
    }
}
