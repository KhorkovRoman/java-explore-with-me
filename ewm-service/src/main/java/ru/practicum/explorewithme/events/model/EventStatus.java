package ru.practicum.explorewithme.events.model;

import ru.practicum.explorewithme.exeption.UnknownStateException;

import java.util.Objects;

public enum EventStatus {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static State findState(String stateText) {
        boolean checkState = false;
        for (State state: State.values()) {
            if (Objects.equals(state.toString(), stateText)) {
                checkState = true;
                break;
            }
        }
        if (!checkState) {
            throw new UnknownStateException("Unknown state: " + stateText);
        }
        return State.valueOf(stateText);
    }
}
