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

package com.coherentsolutions.yaf.data.store.db;


import com.coherentsolutions.yaf.core.consts.Consts;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * The type Yaf db repository.
 */
@Slf4j
@Repository
@ConditionalOnProperty(name = Consts.FRAMEWORK_NAME + ".tempdb.enabled", havingValue = "true")
public class YafDbRepository implements KVRepository<String, Object>, InitializingBean, DisposableBean {

    /**
     * The File name.
     */
    @Value("${yaf.tempdb.name:tmpdb}")
    String FILE_NAME;
    /**
     * The Base dir.
     */
    File baseDir;
    /**
     * The Db.
     */
    RocksDB db;

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    /**
     * Initialize.
     */
// execute after the application starts.
    void initialize() {
        RocksDB.loadLibrary();
        final Options options = new Options();
        options.setCreateIfMissing(true);
        baseDir = new File("./", FILE_NAME);
        try {
            Files.createDirectories(baseDir.getParentFile().toPath());
            Files.createDirectories(baseDir.getAbsoluteFile().toPath());
            db = RocksDB.open(options, baseDir.getAbsolutePath());
            log.info("YafDB initialized");
        } catch (IOException | RocksDBException e) {
            log.error("Error initializng YafDB. Exception: '{}', message: '{}'", e.getCause(), e.getMessage(), e);
        }
    }

    public void destroy() {
        db.close();
    }

    @Override
    public synchronized boolean save(String key, Object value) {
        log.info("saving value '{}' with key '{}'", value, key);
        try {
            db.put(key.getBytes(), SerializationUtils.serialize(value));
        } catch (RocksDBException e) {
            log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public synchronized Optional<Object> find(String key) {
        Object value = null;
        try {
            byte[] bytes = db.get(key.getBytes());
            if (bytes != null)
                value = SerializationUtils.deserialize(bytes);
        } catch (RocksDBException e) {
            log.error("Error retrieving the entry with key: {}, cause: {}, message: {}", key, e.getCause(),
                    e.getMessage());
        }
        log.info("finding key '{}' returns '{}'", key, value);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    @Override
    public synchronized boolean delete(String key) {
        log.info("deleting key '{}'", key);
        try {
            db.delete(key.getBytes());
        } catch (RocksDBException e) {
            log.error("Error deleting entry, cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }
}
