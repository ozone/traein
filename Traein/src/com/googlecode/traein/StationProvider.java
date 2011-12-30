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

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class StationProvider extends ContentProvider {
    public static final String AUTHORITY = "com.googlecode.traein";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/stations");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.traein.station";

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.traein.station";

    private static final UriMatcher URI_MATCHER;

    private static final HashMap<String, String> PROJECTION_MAP;

    private static final int STATIONS = 1;

    private static final int STATION_ID = 2;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "stations", STATIONS);
        URI_MATCHER.addURI(AUTHORITY, "stations/#", STATION_ID);

        PROJECTION_MAP = new HashMap<String, String>();
        PROJECTION_MAP.put(Station._ID, Station._ID);
        PROJECTION_MAP.put(Station.CODE, Station.CODE);
        PROJECTION_MAP.put(Station.ENGLISH_NAME, Station.ENGLISH_NAME);
        PROJECTION_MAP.put(Station.GAELIC_NAME, Station.GAELIC_NAME);
    }

    private StationDbOpenHelper mOpenHelper;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case STATIONS:
                return CONTENT_TYPE;
            case STATION_ID:
                return CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new StationDbOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Station.TABLE_NAME);
        builder.setProjectionMap(PROJECTION_MAP);

        switch (URI_MATCHER.match(uri)) {
            case STATIONS:
                return builder.query(mOpenHelper.getReadableDatabase(), projection, selection,
                        selectionArgs, null, null, sortOrder);
            case STATION_ID:
                builder.appendWhere(Station._ID + "=");
                builder.appendWhereEscapeString(uri.getLastPathSegment());
                return builder.query(mOpenHelper.getReadableDatabase(), projection, selection,
                        selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
