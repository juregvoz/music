package com.music.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;

@Configuration
public class ModelMapperConfiguration {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
