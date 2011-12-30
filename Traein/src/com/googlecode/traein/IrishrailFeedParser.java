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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class IrishrailFeedParser {
    public static ArrayList<Train> parse(InputStream input, String encoding) throws ParserException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(input, encoding);
            parser.require(XmlPullParser.START_DOCUMENT, null, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "rss");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "channel");
            getTagContent(parser, "title");
            getTagContent(parser, "description");
            getTagContent(parser, "link");

            ArrayList<Train> trains = new ArrayList<Train>();

            parser.nextTag();
            while (parser.getEventType() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, "item");
                String title = getTagContent(parser, "title");
                String description = getTagContent(parser, "description");
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "item");
                trains.add(parseTitleAndDescription(title, description));

                parser.nextTag();
            }
            parser.require(XmlPullParser.END_TAG, null, "channel");
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, null, "rss");
            parser.next();
            parser.require(XmlPullParser.END_DOCUMENT, null, null);
            return trains;
        } catch (XmlPullParserException e) {
            throw new ParserException(e);
        } catch (IOException e) {
            throw new ParserException(e);
        }
    }

    private static String getTagContent(XmlPullParser parser, String tag)
            throws XmlPullParserException, IOException {
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, tag);
        String content = parser.nextText();
        parser.require(XmlPullParser.END_TAG, null, tag);
        return content;
    }

    private static final String TIME = "\\d{1,2}:\\d{2}";

    private static final String TRAIN = "[0-9A-Z]{4,5}";

    private static final String STATION = ".*";

    private static final Pattern TITLE_PATTERN = Pattern.compile("^" + TIME + "  to " + STATION
            + "$");

    private static final Pattern TITLE_TERMINUS_PATTERN = Pattern.compile("^Terminates at  "
            + STATION + "$");

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^" + TRAIN
            + " - Origin : (" + STATION + ") - Destination : (" + STATION
            + ") \\| -   Expected departure at " + STATION + " :(" + TIME
            + ") \\| -   Scheduled departure at " + TIME + "$");

    private static final Pattern DESCRIPTION_TERMINUS_PATTERN = Pattern.compile("^" + TRAIN
            + " - Origin : (" + STATION + ") - Destination : (" + STATION
            + ") \\| -   Expected arrival at " + STATION + " : (" + TIME + ") -   Terminates at  "
            + STATION + "$");

    private static Train parseTitleAndDescription(String title, String description)
            throws ParserException {
        boolean terminus = title.startsWith("Terminates at  ");
        Pattern titlePattern = terminus ? TITLE_TERMINUS_PATTERN : TITLE_PATTERN;
        Pattern descriptionPattern = terminus ? DESCRIPTION_TERMINUS_PATTERN : DESCRIPTION_PATTERN;
        if (!titlePattern.matcher(title).matches()) {
            throw new ParserException("Unexpected title: " + title);
        }
        Matcher m = descriptionPattern.matcher(description);
        if (!m.matches()) {
            throw new ParserException("Unexpection description: " + description);
        }
        String origin = m.group(1);
        String destination = m.group(2);
        String time = m.group(3);
        return new Train(origin, destination, terminus, time);
    }
}
