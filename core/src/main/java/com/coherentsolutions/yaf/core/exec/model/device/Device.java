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

package com.coherentsolutions.yaf.core.exec.model.device;


import com.coherentsolutions.yaf.core.enums.DeviceType;
import com.coherentsolutions.yaf.core.enums.DriverScope;
import com.coherentsolutions.yaf.core.exec.model.farm.Farm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * The type Device.
 */
@Data
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, visible = true, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = BrowserDevice.class, name = "web"),
        @JsonSubTypes.Type(value = MobileDevice.class, name = "mobile"),
        @JsonSubTypes.Type(value = DesktopDevice.class, name = "desktop")})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Device {

    /**
     * The Name.
     */
    String name;
    /**
     * The Type.
     */
    DeviceType type;
    /**
     * The Scope.
     */
    DriverScope scope = DriverScope.EXECUTION;
    /**
     * The Capabilities.
     */
    Map<String, String> capabilities;
    /**
     * The Params.
     */
    Map<String, Object> params;

    /**
     * The Farm.
     */
    @JsonIgnore
    Farm farm;
    /**
     * The Screen width.
     */
    @JsonIgnore
    Integer screenWidth; //TODO 112 populate
    /**
     * The Screen height.
     */
    @JsonIgnore
    Integer screenHeight;

    /**
     * Is boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public boolean is(DeviceType type) {
        return this.getType().equals(type);
    }
}
