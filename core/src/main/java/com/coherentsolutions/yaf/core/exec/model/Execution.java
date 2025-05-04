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

package com.coherentsolutions.yaf.core.exec.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * The type Execution.
 */
@Data
@Accessors(chain = true)
public class Execution {

    /**
     * The Name.
     */
    String name;
    /**
     * The Farms names.
     */
    List<String> farmsNames;

    /**
     * The Envs.
     */
    Map<String, Environment> envs;
    /**
     * The Envs names.
     */
    List<String> envsNames;

    /**
     * The Config names.
     */
    List<String> configNames;
    /**
     * The Configs.
     */
    List<Config> configs;

    /**
     * The Setup.
     */
    RunParallelSetup setup = new RunParallelSetup();

    /**
     * The Data.
     */
// todo refactor, may be not a proper place
    @JsonIgnore
    Map<String, Map<String, Object>> data;
}
