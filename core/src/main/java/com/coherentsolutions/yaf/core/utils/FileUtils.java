/*
 * MIT License
 *
 * Copyright (c) 2021 - 2024 Coherent Solutions Inc.
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


import com.coherentsolutions.yaf.core.consts.Consts;
import com.coherentsolutions.yaf.core.exception.GeneralYafException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * The type File utils.
 */
public class FileUtils {

    /**
     * The constant mapper.
     */
    protected static ObjectMapper mapper;

    /**
     * Normalize file name string.
     *
     * @param name the name
     * @param ext  the ext
     * @return the string
     */
    public static String normalizeFileName(String name, String ext) {
        if (!name.endsWith(ext)) {
            if (!ext.startsWith(".")) {
                ext = "." + ext;
            }
            name += ext;
        }
        return name;
    }

    /**
     * Gets resource json file.
     *
     * @param name           the name
     * @param resourceFolder the resource folder
     * @return the resource json file
     * @throws GeneralYafException the general yaf exception
     */
    public static File getResourceJsonFile(String name, String resourceFolder) throws GeneralYafException {
        return getResourceFile(name, Consts.JSON, resourceFolder, true, false);
    }

    /**
     * Gets resource json 5 file.
     *
     * @param name           the name
     * @param resourceFolder the resource folder
     * @return the resource json 5 file
     * @throws GeneralYafException the general yaf exception
     */
    public static File getResourceJson5File(String name, String resourceFolder) throws GeneralYafException {
        try {
            return getResourceFile(name, Consts.JSON5, resourceFolder, true, false);
        } catch (GeneralYafException e) {
            return getResourceJsonFile(name, resourceFolder);
        }
    }

    /**
     * Gets resource file.
     *
     * @param name              the name
     * @param ext               the ext
     * @param resourceFolder    the resource folder
     * @param normalize         the normalize
     * @param tryToLoadExternal the try to load external
     * @return the resource file
     * @throws GeneralYafException the general yaf exception
     */
    public static File getResourceFile(String name, String ext, String resourceFolder, boolean normalize,
                                       boolean tryToLoadExternal) throws GeneralYafException {
        if (normalize) {
            name = FileUtils.normalizeFileName(name, ext);
        }
        String classpathPath = ResourceUtils.CLASSPATH_URL_PREFIX + (resourceFolder != null ? resourceFolder + "/" : "")
                + name;
        try {
            File f = ResourceUtils.getFile(classpathPath);
            if (f.exists()) {
                return f;
            }
        } catch (FileNotFoundException e) {
            // no need
        }
        if (tryToLoadExternal) {
            try {
                File f = ResourceUtils.getFile("." + File.separator + name);
                if (f.exists()) {
                    return f;
                }
            } catch (FileNotFoundException fileNotFoundException) {
                // no need
            }
        }
        throw new GeneralYafException("File " + name + " not found!");
    }

    /**
     * Gets object mapper.
     *
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
            mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        }
        return mapper;
    }

}
