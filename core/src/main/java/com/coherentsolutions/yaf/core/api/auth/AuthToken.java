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

package com.coherentsolutions.yaf.core.api.auth;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The type Auth token.
 */
@Data
@Accessors(chain = true)
public class AuthToken {

    /**
     * The constant UTC.
     */
    public static final java.time.ZoneId UTC = ZoneId.of("UTC");
    /**
     * The Created.
     */
    LocalDateTime created;
    /**
     * The Expired.
     */
    LocalDateTime expired;
    /**
     * The Token.
     */
    String token;
    /**
     * The Ex.
     */
    @JsonIgnore
    Exception ex;
    /**
     * The User.
     */
    @JsonIgnore
    YafApiUser user;

    /**
     * Instantiates a new Auth token.
     */
    public AuthToken() {
        created = LocalDateTime.now(UTC);
    }

    /**
     * Is expired boolean.
     *
     * @return the boolean
     */
// TODO validate TimeZone
    public boolean isExpired() {
        if (expired == null) {
            return false;
        }
        return !LocalDateTime.now(UTC).minusSeconds(15).isBefore(expired);
    }
}
