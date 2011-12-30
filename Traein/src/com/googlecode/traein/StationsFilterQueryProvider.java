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
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.FilterQueryProvider;

public class StationsFilterQueryProvider implements FilterQueryProvider {
    private static final char ESCAPE = '/';

    private static final String SELECTION = Station.ENGLISH_NAME + " LIKE ? ESCAPE '" + ESCAPE
            + "'";

    private final Activity mActivity;

    private final String[] mProjection;

    private final String mSortOrder;

    public StationsFilterQueryProvider(Activity activity, String[] projection, String sortOrder) {
        mActivity = activity;
        mProjection = projection;
        mSortOrder = sortOrder;
    }

    public Cursor runQuery(CharSequence constraint) {
        final Uri uri = mActivity.getIntent().getData();
        if (TextUtils.isEmpty(constraint)) {
            return mActivity.managedQuery(uri, mProjection, null, null, mSortOrder);
        } else {
            final StringBuilder builder = new StringBuilder();
            builder.append('%');
            SqliteUtils.escapeLikeExpr(builder, constraint, ESCAPE);
            builder.append('%');
            final String[] selectionArgs = new String[] {
                builder.toString()
            };
            return mActivity.managedQuery(uri, mProjection, SELECTION, selectionArgs, mSortOrder);
        }
    }
}
