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

package com.coherentsolutions.yaf.data.store;


import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The type Files store.
 */
@Service
public class FilesStore {

    /**
     * The Root path.
     */
    Path rootPath = Paths.get("").resolve("store");

    /**
     * Gets file path to store.
     *
     * @param type     the type
     * @param fileName the file name
     * @return the file path to store
     */
    public String getFilePathToStore(String type, String fileName) {
        return getPath(type, fileName).toAbsolutePath().toString();
    }

    /**
     * Gets file.
     *
     * @param type the type
     * @param name the name
     * @return the file
     */
    public File getFile(String type, String name) {
        Path filePath = getPath(type, name);
        return filePath.toFile();
    }

    /**
     * Exists boolean.
     *
     * @param type the type
     * @param name the name
     * @return the boolean
     */
    public boolean exists(String type, String name) {
        File f = getFile(type, name);
        return f != null && f.exists();
    }

    /**
     * Gets path.
     *
     * @param type     the type
     * @param fileName the file name
     * @return the path
     */
    protected Path getPath(String type, String fileName) {
        Path storePath = rootPath;
        if (type != null) {
            storePath.resolve(type);
        }
        return storePath.resolve(fileName);
    }
}
