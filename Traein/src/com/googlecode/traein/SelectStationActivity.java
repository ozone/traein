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

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SelectStationActivity extends ListActivity {
    private static final String[] PROJECTION = { Station._ID, Station.ENGLISH_NAME };
    private static final String[] FROM = { Station.ENGLISH_NAME };
    private static final int[] TO = { android.R.id.text1 };
    private static final int LAYOUT = android.R.layout.simple_list_item_1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.select_a_station);
        final Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(StationProvider.CONTENT_URI);
        }
        setListAdapter(createAdapter(intent));
    }

    private ListAdapter createAdapter(final Intent intent) {
        Cursor cursor = managedQuery(intent.getData(), PROJECTION, null, null, Station.SORT_BY_NAME);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, LAYOUT, cursor, FROM, TO);
        adapter.setFilterQueryProvider(new StationsFilterQueryProvider(this, PROJECTION,
                Station.SORT_BY_NAME));
        return adapter;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}