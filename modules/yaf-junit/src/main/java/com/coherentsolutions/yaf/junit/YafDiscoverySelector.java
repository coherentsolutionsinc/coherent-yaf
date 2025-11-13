package com.coherentsolutions.yaf.junit;

import lombok.AllArgsConstructor;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.MethodSelector;

@AllArgsConstructor
public class YafDiscoverySelector {

    ClassSelector classSelector;
    MethodSelector methodSelector;

    public String getMethodName() {
        return methodSelector != null ? methodSelector.getMethodName() : null;
    }

    public Class getJavaClass() {
        return methodSelector != null ? methodSelector.getJavaClass() : classSelector.getJavaClass();
    }

}
