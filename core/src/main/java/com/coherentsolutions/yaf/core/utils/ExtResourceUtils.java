/*
 * MIT License
 *
 * Copyright (c) 2021 - 2025 Coherent Solutions Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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