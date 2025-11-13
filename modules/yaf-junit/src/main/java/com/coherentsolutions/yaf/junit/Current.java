package com.coherentsolutions.yaf.junit;

/**
 * ThreadLocal holder for the current envName during test execution.
 * <p>
 * This class provides access to the current envName when a test
 * is being executed. It's automatically managed by the YafEngine.
 */
public final class Current {
    private static final ThreadLocal<String> ENV_NAME = new ThreadLocal<>();

    private Current() {
    }

    public static String getEnvName() {
        return ENV_NAME.get();
    }


    public static void set(String envName) {
        ENV_NAME.set(envName);
    }

    public static void clear() {
        ENV_NAME.remove();
    }
}

