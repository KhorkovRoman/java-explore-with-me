package ru.practicum.explorewithme.events.mapper;

import lombok.Data;
import ru.practicum.explorewithme.events.dto.Location;
import ru.practicum.explorewithme.events.model.LocationModel;

@Data
public class LocationMapper {

    public static LocationModel toLocationModel(Location location) {
        return LocationModel.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

}
