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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * Required to support shortcuts created in version 1.0rc1.
 * 
 * @deprecated
 */
@Deprecated
public class DeprecatedStationProvider extends ContentProvider {
    public static final String AUTHORITY = "com.googlecode.traein.stationprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.traein.station";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.traein.station";

    private static final UriMatcher URI_MATCHER;

    private static final int STATIONS = 1;
    private static final int STATION_ID = 2;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "#", STATION_ID);
        URI_MATCHER.addURI(AUTHORITY, "/", STATIONS);
    }

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
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        switch (URI_MATCHER.match(uri)) {
        case STATIONS:
            return getContext().getContentResolver().query(StationProvider.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);
        case STATION_ID:
            long id = ContentUris.parseId(uri);
            Uri newUri = ContentUris.withAppendedId(StationProvider.CONTENT_URI, id);
            return getContext().getContentResolver().query(newUri, projection, selection,
                    selectionArgs, sortOrder);
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
