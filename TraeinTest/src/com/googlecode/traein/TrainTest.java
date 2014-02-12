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

import android.test.AndroidTestCase;

public class TrainTest extends AndroidTestCase {

    public void testTrainByTimeComparator() {
        Train a = new Train("A", "B", 0);
        Train b = new Train("B", "C", 10);
        Train c = new Train("C", "D", 15);

        assertTrue(Train.BY_DUE_TIME.compare(a, a) == 0);
        assertTrue(Train.BY_DUE_TIME.compare(a, b) < 0);
        assertTrue(Train.BY_DUE_TIME.compare(b, c) < 0);
        assertTrue(Train.BY_DUE_TIME.compare(a, c) < 0);
        assertTrue(Train.BY_DUE_TIME.compare(b, a) > 0);
        assertTrue(Train.BY_DUE_TIME.compare(c, b) > 0);
        assertTrue(Train.BY_DUE_TIME.compare(c, a) > 0);
    }
}
