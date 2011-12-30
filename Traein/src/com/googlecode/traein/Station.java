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

public final class Station {
    public static final String TABLE_NAME = "stations";

    public static final String _ID = "_id";

    public static final String CODE = "code";

    public static final String ENGLISH_NAME = "english_name";

    public static final String GAELIC_NAME = "gaelic_name";

    public static final String SORT_BY_NAME = ENGLISH_NAME + " ASC";

    private Station() {
    }
}
