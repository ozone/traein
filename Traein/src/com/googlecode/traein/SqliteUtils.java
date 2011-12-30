/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.traein;

public final class SqliteUtils {

    private SqliteUtils() {
        // Prevent instantiation by other classes.
    }

    public static StringBuilder escapeLikeExpr(StringBuilder builder, CharSequence toEscape,
            char escape) {
        for (int i = 0, n = toEscape.length(); i < n; i++) {
            final char ch = toEscape.charAt(i);
            if (ch == '%' || ch == '_' || ch == escape) {
                builder.append(escape);
            }
            builder.append(ch);
        }
        return builder;
    }

}
