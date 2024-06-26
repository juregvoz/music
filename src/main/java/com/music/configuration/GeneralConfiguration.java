package com.music.configuration;

import com.music.dto.ReleasePostRequest;
import com.music.entity.Release;
import com.music.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;

@Configuration
public class GeneralConfiguration {

  @Bean
  public static ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper
        .emptyTypeMap(ReleasePostRequest.class, Release.class)
        .addMappings(m -> m.skip(Release::setId))
        .addMapping(ReleasePostRequest::getName, Release::setName)
        .addMapping(ReleasePostRequest::getDescription, Release::setDescription);
    return modelMapper;
  }

  @Bean
  public Utils utils() {
    return new Utils();
  }
}
