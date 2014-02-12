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
import java.util.Collections;

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
        	parser.require(XmlPullParser.START_TAG, null, "ArrayOfObjStationData");
        	parser.nextTag();
            ArrayList<Train> trains = new ArrayList<Train>();
            while (parser.getEventType() != XmlPullParser.END_TAG) {
            	parser.require(XmlPullParser.START_TAG, null, "objStationData");
            	contentOf(parser, "Servertime");
            	contentOf(parser, "Traincode");
            	contentOf(parser, "Stationfullname");
            	contentOf(parser, "Stationcode");
    			contentOf(parser, "Querytime");
    			contentOf(parser, "Traindate");
    			String origin = contentOf(parser, "Origin");
    			String destination = contentOf(parser, "Destination");
    			contentOf(parser, "Origintime");
                contentOf(parser, "Destinationtime");
                contentOf(parser, "Status");
                contentOf(parser, "Lastlocation");
                int dueIn = Integer.parseInt(contentOf(parser, "Duein"));
                contentOf(parser, "Late");
                contentOf(parser, "Exparrival");
                contentOf(parser, "Expdepart");
                contentOf(parser, "Scharrival");
                contentOf(parser, "Schdepart");
                contentOf(parser, "Direction");
                contentOf(parser, "Traintype");
                contentOf(parser, "Locationtype");
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "objStationData");
                parser.nextTag();
                trains.add(new Train(origin, destination, dueIn));
            }
            parser.require(XmlPullParser.END_TAG, null, "ArrayOfObjStationData");
            parser.next();
            parser.require(XmlPullParser.END_DOCUMENT, null, null);
            Collections.sort(trains, Train.BY_DUE_TIME);
            return trains;
        } catch (IOException e) {
            throw new ParserException(e);
        } catch (XmlPullParserException e) {
			throw new ParserException(e);
		}
    }
    
    private static String contentOf(XmlPullParser parser, String tag) throws XmlPullParserException, IOException {
    	parser.nextTag();
    	parser.require(XmlPullParser.START_TAG, null, tag);
    	String content = parser.nextText();
    	parser.require(XmlPullParser.END_TAG, null, tag);
    	return content;
    }
}
