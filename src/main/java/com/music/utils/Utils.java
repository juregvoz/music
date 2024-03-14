package com.music.utils;

import java.util.Optional;
import java.util.function.Consumer;

public class Utils {
  public static <T> void updateIfPresent(T value, Consumer<T> consumer) {
    Optional.ofNullable(value).ifPresent(consumer);
  }
}
