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


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * The type Cache map.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
@Data
@Accessors(chain = true)
@Deprecated
public class CacheMap<K, V> {

    /**
     * The Getter.
     */
    Function<K, V> getter;
    /**
     * The Data.
     */
    Map<K, V> data;

    /**
     * Instantiates a new Cache map.
     *
     * @param getter the getter
     */
    public CacheMap(Function<K, V> getter) {
        this.getter = getter;
        this.data = new ConcurrentHashMap<>();
    }

    /**
     * Get v.
     *
     * @param key the key
     * @return the v
     */
    public V get(K key) {
        V value = data.get(key);
        if (value == null) {
            value = getter.apply(key);
            data.put(key, value);
        }
        return value;
    }

}
