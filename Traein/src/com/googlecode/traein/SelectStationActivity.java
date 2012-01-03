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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SelectStationActivity extends ListActivity {
    private static final String[] PROJECTION = {
            Station._ID, Station.ENGLISH_NAME
    };

    private static final String[] FROM = {
        Station.ENGLISH_NAME
    };

    private static final int[] TO = {
        android.R.id.text1
    };

    private static final int LAYOUT = android.R.layout.simple_list_item_1;

    private static final int TIPS_DIALOG = 0;

    private static final int ABOUT_DIALOG = 1;

    private static final String PREFERENCE_TIP_COUNTER = "tip.counter";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.select_a_station);
        final Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(StationProvider.CONTENT_URI);
        }
        if (TipsHelper.shouldShowTip(this, PREFERENCE_TIP_COUNTER)) {
            showDialog(TIPS_DIALOG);
        }
        setListAdapter(createAdapter(intent));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.select_station_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showDialog(ABOUT_DIALOG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String versionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "<unknown version>";
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIPS_DIALOG:
                return TipsHelper.onCreateTipsDialog(this, PREFERENCE_TIP_COUNTER);
            case ABOUT_DIALOG:
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.about_dialog_title)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage(
                                Html.fromHtml(getString(R.string.about_dialog_message,
                                        versionName())))
                        .setNeutralButton(R.string.dismiss, Listeners.DISMISS_ON_CLICK).create();
            default:
                throw new IllegalArgumentException("Unknown dialog id: " + id);
        }
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
