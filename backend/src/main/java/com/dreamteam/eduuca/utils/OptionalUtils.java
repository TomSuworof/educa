package com.dreamteam.eduuca.utils;

import java.util.Optional;
import java.util.function.Supplier;

public class OptionalUtils {
    public static <T> Optional<T> optTry(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
