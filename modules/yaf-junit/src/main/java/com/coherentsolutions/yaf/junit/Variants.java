package com.coherentsolutions.yaf.junit;

import java.util.List;

public final class Variants {
    public static final List<String> ALL = List.of("a", "b");
    public static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private Variants() {
    }
}