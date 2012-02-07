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
        expected.add(new Train("Malahide", "Dun Laoghaire", "08:05"));
        expected.add(new Train("Dublin Pearse", "Maynooth", "08:10"));
        expected.add(new Train("Howth", "Bray", "08:11"));
        expected.add(new Train("Bray", "Howth", "08:14"));
        expected.add(new Train("Drogheda", "Bray", "08:17"));
        expected.add(new Train("Longford", "Dublin Pearse", "08:17"));
        expected.add(new Train("Dundalk", "Dublin Connolly", "08:20"));
        expected.add(new Train("Dublin Pearse", "Maynooth", "08:27"));
        expected.add(new Train("Greystones", "Malahide", "08:27"));
        expected.add(new Train("Howth", "Greystones", "08:27"));
        expected.add(new Train("Maynooth", "Dublin Connolly", "08:29"));
        expected.add(new Train("Malahide", "Dun Laoghaire", "08:31"));
        expected.add(new Train("Bray", "Dublin Connolly", "08:33"));
        expected.add(new Train("Newry", "Bray", "08:36"));
        expected.add(new Train("Bray", "Howth", "08:39"));
        expected.add(new Train("Maynooth", "Bray", "08:39"));
        expected.add(new Train("Rosslare Europort", "Dundalk", "08:48"));
        expected.add(new Train("Greystones", "Malahide", "08:55"));
        expected.add(new Train("Dun Laoghaire", "Dublin Connolly", "09:02"));
        expected.add(new Train("Dublin Connolly", "Sligo", "09:05"));

        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/CNLLY-20120120-0806.html");
        try {
            ArrayList<Train> actual = IrishrailFeedParser.parse(in);
            assertEquals(expected, actual);
        } finally {
            in.close();
        }
    }

    public void testParserNoTrains() throws Exception {
        ArrayList<Train> expected = new ArrayList<Train>();
        AssetManager assets = getInstrumentation().getContext().getAssets();
        InputStream in = assets.open("testdata/CTARF-20120120-0109.html");
        try {
            ArrayList<Train> actual = IrishrailFeedParser.parse(in);
            assertEquals(expected, actual);
        } catch (ParserException e) {
            // expected
        } finally {
            in.close();
        }
    }
}
