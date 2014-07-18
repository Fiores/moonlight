package com.cab404.moonlight.util.logging;

/**
 * @author cab404
 */
public class SystemOutLogger implements Logger {
    private static SystemOutLogger singleton;

    public static SystemOutLogger getInstance() {
        return singleton == null ? singleton = new SystemOutLogger() : singleton;
    }

    private SystemOutLogger() {}

    @Override
    public void error(String str) {
        System.err.println(str);
    }
    @Override public void verbose(String str) {
        System.out.println(str);
    }
}
