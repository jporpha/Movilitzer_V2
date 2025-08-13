package com.movilitzer.v2.mapper;
import com.movilitzer.v2.dto.TrackDto;
import com.movilitzer.v2.entity.TrackSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {
    @Mapping(target = "trackId", source = "trackId")
    TrackDto toDto(TrackSnapshot entity);
    List<TrackDto> toDtos(List<TrackSnapshot> entities);
}
