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

import android.content.res.AssetManager;
import android.test.InstrumentationTestCase;

import java.io.InputStream;
import java.util.ArrayList;

public class IrishrailFeedParserTest extends InstrumentationTestCase {

    public void testParserTrains() throws Exception {
        ArrayList<Train> expected = new ArrayList<Train>();
        expected.add(new Train("Malahide", "Greystones", 1));
        expected.add(new Train("Drogheda", "Dublin Pearse", 3));
        expected.add(new Train("Greystones", "Malahide", 19));
        expected.add(new Train("Dundalk", "Dublin Pearse", 20));
        expected.add(new Train("Malahide", "Greystones", 29));
        expected.add(new Train("Greystones", "Malahide", 47));
        expected.add(new Train("Dublin Pearse", "Drogheda", 53));
        expected.add(new Train("Malahide", "Greystones", 61));
        expected.add(new Train("Drogheda", "Dublin Pearse", 74));
        expected.add(new Train("Greystones", "Malahide", 79));
        expected.add(new Train("Malahide", "Greystones", 89));

        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/MHIDE-20140212-130153.xml");
        try {
            ArrayList<Train> actual = IrishrailFeedParser.parse(in, "utf-8");
            assertEquals(expected, actual);
        } finally {
            in.close();
        }
    }

    public void testParserNoTrains() throws Exception {
        ArrayList<Train> expected = new ArrayList<Train>();
        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/WLOW-20140212-130516.xml");
        try {
            ArrayList<Train> actual = IrishrailFeedParser.parse(in, "utf-8");
            assertEquals(expected, actual);
        } catch (ParserException e) {
            // expected
        } finally {
            in.close();
        }
    }
}
