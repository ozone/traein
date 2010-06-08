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

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StationDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = StationDbOpenHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "traein.db";
    private static final int DATABASE_VERSION = 6;

    private final Context mContext;

    StationDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        createDb(db);
        parseStations(new InsertStationCallback(db));
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int oldVersion, int newVersion) {
        parseStations(new UpdateStationCallback(db));
    }

    private static interface StationCallback {
        void onStation(String code, String englishName, String gaelicName);
    }

    private static class InsertStationCallback implements StationCallback {
        private final SQLiteDatabase mDb;

        public InsertStationCallback(SQLiteDatabase db) {
            mDb = db;
        }

        public void onStation(String code, String englishName, String gaelicName) {
            ContentValues values = new ContentValues();
            values.put(Station.CODE, code);
            values.put(Station.ENGLISH_NAME, englishName);
            values.put(Station.GAELIC_NAME, gaelicName);
            mDb.insert(Station.TABLE_NAME, Station.CODE, values);
        }
    }

    private static class UpdateStationCallback implements StationCallback {
        private final SQLiteDatabase mDb;

        public UpdateStationCallback(SQLiteDatabase db) {
            mDb = db;
        }

        public void onStation(String code, String englishName, String gaelicName) {
            ContentValues values = new ContentValues();
            values.put(Station.CODE, code);
            values.put(Station.ENGLISH_NAME, englishName);
            values.put(Station.GAELIC_NAME, gaelicName);
            mDb.update(Station.TABLE_NAME, values, Station.CODE + "=?", new String[] { code });
        }
    }

    private void createDb(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(Station.TABLE_NAME).append(" (");
        sb.append(Station._ID).append(" INTEGER PRIMARY KEY,");
        sb.append(Station.CODE).append(" TEXT UNIQUE,");
        sb.append(Station.ENGLISH_NAME).append(" TEXT NOT NULL,");
        sb.append(Station.GAELIC_NAME).append(" TEXT)");
        String createDb = sb.toString();
        Log.i(TAG, createDb);
        db.execSQL(createDb);
    }

    private void parseStations(StationCallback callback) {
        // Retrieve list of stations from local cache
        XmlResourceParser parser = mContext.getResources().getXml(R.xml.stations);
        try {
            parser.next(); // This should not be required.
            parser.require(XmlPullParser.START_DOCUMENT, null, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "stations");
            parser.nextTag();
            while (parser.getEventType() != XmlPullParser.END_TAG) {
                parser.require(XmlPullParser.START_TAG, null, "station");
                String code = parser.getAttributeValue(null, "code");
                String englishName = parser.getAttributeValue(null, "name_en");
                String gaelicName = parser.getAttributeValue(null, "name_ga");
                callback.onStation(code, englishName, gaelicName);
                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "station");
                parser.nextTag();
            }
            parser.require(XmlPullParser.END_TAG, null, "stations");
            parser.next();
            parser.require(XmlPullParser.END_DOCUMENT, null, null);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Could not create db.", e);
            throw new IllegalStateException(e);
        } catch (IOException e) {
            Log.e(TAG, "Could not create db.", e);
            throw new IllegalStateException(e);
        }
    }
}