package com.paygarde.mailing.results;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResultMapper {
    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);
    //ResultDto resultToResultDTO(Result entity);
    @Mapping(target = "creationDataTime", source = "creationDataTime")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "uid", source = "uid")
    Result resultDtoToResult(ResultDto dto);
}
