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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrishrailFeedParser {
    private static final Pattern TRAIN_PATTERN = Pattern
            .compile("<td>([^<]*)</td><td>([^<]*)</td><td>[^<]*</td><td>[^<]*</td><td>([^<]*)</td><td>[^<]*</td>");

    private static final int ORIGIN_GROUP = 1;

    private static final int DESTINATION_GROUP = 2;

    private static final int TIME_GROUP = 3;

    public static ArrayList<Train> parse(InputStream input) throws ParserException {
        try {
            Matcher matcher = TRAIN_PATTERN.matcher(convertInputStreamToString(input, "UTF-8"));
            ArrayList<Train> trains = new ArrayList<Train>();
            while (matcher.find()) {
                trains.add(new Train(matcher.group(ORIGIN_GROUP), matcher.group(DESTINATION_GROUP),
                        matcher.group(TIME_GROUP)));
            }
            Collections.sort(trains, Train.BY_TIME);
            return trains;
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    private static String convertInputStreamToString(InputStream input, String encoding)
            throws UnsupportedEncodingException, IOException {
        InputStreamReader reader = new InputStreamReader(input, encoding != null ? encoding
                : System.getProperty("file.encoding"));
        StringWriter sw = new StringWriter();
        char[] buffer = new char[4096];
        int n = 0;
        while (-1 != (n = reader.read(buffer))) {
            sw.write(buffer, 0, n);
        }
        return sw.toString();
    }
}
