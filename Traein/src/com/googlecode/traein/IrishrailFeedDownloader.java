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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class IrishrailFeedDownloader {
    private static final String TAG = IrishrailFeedDownloader.class.getSimpleName();

    private static final String IRISHRAIL_FEED_URL = "http://www.irishrail.ie/realtime/station_details.jsp?ref=";

    private static final int FETCH_TIMEOUT_MS = 60 * 1000;

    public static ArrayList<Train> download(String stationCode) throws ParserException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpConnectionParams.setConnectionTimeout(client.getParams(), FETCH_TIMEOUT_MS);
            HttpGet request = new HttpGet(IRISHRAIL_FEED_URL + URLEncoder.encode(stationCode));
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                return parseSuccessfulResponse(response);
            } else {
                Log.e(TAG,
                        "Unexpected status while fetching irishrail RSS feed: "
                                + response.getStatusLine());
                throw new IOException();
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    private static ArrayList<Train> parseSuccessfulResponse(HttpResponse response)
            throws ParserException, IOException {
        HttpEntity entity = response.getEntity();
        try {
            return IrishrailFeedParser.parse(entity.getContent());
        } finally {
            entity.consumeContent();
        }
    }
}
