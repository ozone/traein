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
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class ViewStationActivity extends ListActivity {
    private static final String[] PROJECTION = {
            Station._ID, Station.CODE, Station.ENGLISH_NAME, Station.GAELIC_NAME
    };

    private static final int NETWORK_ERROR_DIALOG = 0;

    private static final int PARSER_ERROR_DIALOG = 1;

    private static final int TIMEOUT_ERROR_DIALOG = 2;

    private String mCode;

    private TextView mEmpty;

    private AsyncTask<String, Void, AsyncTaskResult> mFetchTrainsTask;

    private class ActionBarHelper {
        public void setHomeButtonEnabled(boolean enabled) {
            getActionBar().setHomeButtonEnabled(enabled);
        }

        public void setDisplayHomeAsUpEnabled(boolean enabled) {
            getActionBar().setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);
        Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);
        cursor.moveToFirst();
        mCode = cursor.getString(1);
        setContentView(R.layout.station_view);
        ((TextView)findViewById(R.id.station_name_en)).setText(cursor.getString(2));
        ((TextView)findViewById(R.id.station_name_ga)).setText(cursor.getString(3));
        mEmpty = (TextView)findViewById(android.R.id.empty);

        // Use the action bar instead of the option menu on Honeycomb and over.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBarHelper helper = new ActionBarHelper();
            helper.setDisplayHomeAsUpEnabled(true);
            // Enable the home button on ICS (it is enabled by default in Honeycomb.)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                helper.setHomeButtonEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.view_station_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                cancelFetchTrainTask();
                startFetchTrainTask();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, SelectStationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startFetchTrainTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelFetchTrainTask();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case NETWORK_ERROR_DIALOG:
                return createErrorDialog(R.string.network_error, R.string.network_error_details);
            case PARSER_ERROR_DIALOG:
                return createErrorDialog(R.string.parser_error, R.string.parser_error_details);
            case TIMEOUT_ERROR_DIALOG:
                return createErrorDialog(R.string.timeout_error, R.string.timeout_error_details);
            default:
                throw new IllegalStateException("Unknown dialog.");
        }
    }

    private void startFetchTrainTask() {
        setProgressBarIndeterminateVisibility(true);
        mFetchTrainsTask = new FetchTrainsTask();
        mFetchTrainsTask.execute(mCode);
    }

    private void cancelFetchTrainTask() {
        if (mFetchTrainsTask != null && mFetchTrainsTask.getStatus() != AsyncTask.Status.RUNNING) {
            mFetchTrainsTask.cancel(true);
            mFetchTrainsTask = null;
        }
    }

    private Dialog createErrorDialog(int titleRes, int detailsRes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle(titleRes);
        builder.setMessage(detailsRes);
        builder.setPositiveButton(android.R.string.ok, Listeners.finishOnClick(this));
        return builder.create();
    }

    private void onFetchError(int dialog) {
        setProgressBarIndeterminateVisibility(false);
        showDialog(dialog);
    }

    private void onFetchSuccess(ArrayList<Train> trains) {
        setProgressBarIndeterminateVisibility(false);
        setListAdapter(new TrainListAdapter(this, trains));
        if (trains.isEmpty()) {
            mEmpty.setText(R.string.no_trains);
        } else {
            mEmpty.setText("");
        }
    }

    private interface AsyncTaskResult {
        void report();
    }

    private class Success implements AsyncTaskResult {
        private final ArrayList<Train> mTrains;

        public Success(ArrayList<Train> trains) {
            mTrains = trains;
        }

        public void report() {
            onFetchSuccess(mTrains);
        }
    }

    private class Failed implements AsyncTaskResult {
        private final int mDialog;

        public Failed(int dialog) {
            mDialog = dialog;
        }

        public void report() {
            onFetchError(mDialog);
        }
    }

    private class FetchTrainsTask extends AsyncTask<String, Void, AsyncTaskResult> {
        @Override
        protected AsyncTaskResult doInBackground(String... params) {
            if (params.length != 1) {
                throw new IllegalArgumentException("Can only handle 1 parameter.");
            }
            try {
                return new Success(IrishrailFeedDownloader.download(params[0]));
            } catch (ParserException e) {
                return new Failed(PARSER_ERROR_DIALOG);
            } catch (SocketTimeoutException e) {
                return new Failed(TIMEOUT_ERROR_DIALOG);
            } catch (IOException e) {
                return new Failed(NETWORK_ERROR_DIALOG);
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskResult result) {
            if (!isFinishing()) {
                result.report();
            }
        }
    }
}
