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

import java.io.InputStream;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

public class IrishrailFeedParserTest extends InstrumentationTestCase {

    public void testParserTrainsAtTerminus() throws Exception {
        ArrayList<Train> expected = new ArrayList<Train>();
        expected.add(new Train("Bray", "Howth", true, "12:40"));
        expected.add(new Train("Howth", "Bray", false, "12:45"));
        expected.add(new Train("Greystones", "Howth", true, "12:50"));
        expected.add(new Train("Howth", "Greystones", false, "13:00"));
        expected.add(new Train("Bray", "Howth", true, "13:05"));
        expected.add(new Train("Howth", "Bray", false, "13:15"));

        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/HOWTH-20100403-122700.xml");
        try {
            ArrayList<Train> actual = IrishrailFeedParser.parse(in, "UTF-8");
            assertEquals(expected, actual);
        } finally {
            in.close();
        }
    }

    public void testParserNoTrains() throws Exception {
        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/PDOWN-20100403-131333.xml");
        try {
            IrishrailFeedParser.parse(in, "UTF-8");
            fail("should have thrown a ParserException");
        } catch (ParserException e) {
            // expected
        } finally {
            in.close();
        }
    }
}
