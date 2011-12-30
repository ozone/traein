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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

public class CreateShortcutActivity extends Activity {

    public static final int PICK_STATION_REQUEST = 0;

    private static final String[] PROJECTION = {
        Station.ENGLISH_NAME
    };

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent pickStation = new Intent(Intent.ACTION_PICK, StationProvider.CONTENT_URI);
        startActivityForResult(pickStation, PICK_STATION_REQUEST);
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_STATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, uri);
                Intent createShortcutIntent = new Intent();
                createShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                createShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getStationName(uri));
                Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this,
                        R.drawable.ic_launcher_traein);
                createShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
                setResult(RESULT_OK, createShortcutIntent);
            }
            finish();
        }
    }

    private String getStationName(Uri data) {
        Cursor cursor = getContentResolver().query(data, PROJECTION, null, null, null);
        try {
            cursor.moveToFirst();
            return cursor.getString(0);
        } finally {
            cursor.close();
        }
    }
}
