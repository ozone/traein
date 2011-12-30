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

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

public class CreateShortcutActivityTest extends ActivityUnitTestCase<CreateShortcutActivity> {
    public CreateShortcutActivityTest(Class<CreateShortcutActivity> activityClass) {
        super(activityClass);
    }

    public void testOnCreate_startsPickStationActivity() {
        startActivity(null, null, null);

        Intent started = getStartedActivityIntent();
        int requestCode = getStartedActivityRequest();

        assertNotNull(started);
        assertEquals(Intent.ACTION_PICK, started.getAction());
        assertEquals(StationProvider.CONTENT_URI, started.getData());
        assertEquals(CreateShortcutActivity.PICK_STATION_REQUEST, requestCode);
    }

    public void testOnActivityResult_doesNothingOnUnknownRequest() {
        int requestCode = CreateShortcutActivity.PICK_STATION_REQUEST + 1;
        CreateShortcutActivity activity = getActivity();
        activity.onActivityResult(requestCode, Activity.RESULT_OK, null);
        assertFalse(isFinishCalled());
        activity.onActivityResult(requestCode, Activity.RESULT_CANCELED, null);
        assertFalse(isFinishCalled());
        activity.onActivityResult(requestCode, Activity.RESULT_FIRST_USER, null);
        assertFalse(isFinishCalled());
    }

    public void testOnActivityResult_finishesWhenPickStationActivityWasCancelled() {
        int requestCode = CreateShortcutActivity.PICK_STATION_REQUEST;
        getActivity().onActivityResult(requestCode, Activity.RESULT_CANCELED, null);
        assertTrue(isFinishCalled());
    }
    // public void testOnActivityResult_
}
