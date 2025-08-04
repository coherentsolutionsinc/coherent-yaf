package com.coherentsolutions.yaf.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

public class ExtResourceUtils {

    /**
     * Resolves a given path to a Spring {@link Resource} from any known source:
     * <ul>
     *     <li>Prefixed paths like <code>classpath:</code>, <code>file:</code>, <code>http:</code></li>
     *     <li>Unprefixed paths fallback to classpath first, then file system</li>
     * </ul>
     *
     * @param path           the path to resolve
     * @param resourceLoader the resource loader to use (usually injected from Spring context)
     * @param mustExist      if true, returns null if the resolved resource doesn't exist
     * @return a {@link Resource}, or null if not found and {@code mustExist} is true
     */
    public static Resource resolveResource(String path, ResourceLoader resourceLoader, boolean mustExist) {
        Resource resource;
        if (ResourceUtils.isUrl(path)) {
            resource = resourceLoader.getResource(path);
            if (!mustExist || resource.exists()) {
                return resource;
            }
        } else {
            Resource classpathRes = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + path);
            if (classpathRes.exists()) {
                return classpathRes;
            }
            Resource fileRes = resourceLoader.getResource(ResourceUtils.FILE_URL_PREFIX + path);
            if (fileRes.exists()) {
                return fileRes;
            }
            if (!mustExist) {
                return fileRes;
            }
        }
        return null;
    }
}