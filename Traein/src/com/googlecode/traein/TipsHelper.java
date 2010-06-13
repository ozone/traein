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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class TipsHelper {
    public static boolean shouldShowTip(Activity activity, String tipCounterPreference) {
        if (Intent.ACTION_MAIN.equals(activity.getIntent().getAction())) {
            int tipIndex = activity.getPreferences(Activity.MODE_PRIVATE).getInt(
                    tipCounterPreference, 0);
            String[] tipMessages = activity.getResources().getStringArray(
                    R.array.tips_dialog_messages);
            return tipIndex < tipMessages.length;
        }
        return false;
    }

    public static Dialog onCreateTipsDialog(final Activity activity,
            final String tipCounterPreference) {
        String[] tipMessages = activity.getResources().getStringArray(R.array.tips_dialog_messages);
        final int tipIndex = activity.getPreferences(Activity.MODE_PRIVATE).getInt(
                tipCounterPreference, 0)
                % tipMessages.length;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.tips_dialog_title);
        builder.setMessage(tipMessages[tipIndex]);
        builder.setPositiveButton(R.string.tips_dialog_more_button_label,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setTipCounter(activity, tipCounterPreference, tipIndex + 1);
                        startHowTo(activity, tipIndex);
                    }
                });
        builder.setNegativeButton(R.string.tips_dialog_dismiss_button_label,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setTipCounter(activity, tipCounterPreference, tipIndex + 1);
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                setTipCounter(activity, tipCounterPreference, tipIndex + 1);
            }
        });
        return builder.create();
    }

    private static void setTipCounter(Activity activity, String tipCounterPreference, int newValue) {
        activity.getPreferences(Activity.MODE_PRIVATE).edit()
                .putInt(tipCounterPreference, newValue).commit();
    }

    private static void startHowTo(Activity activity, int tipIndex) {
        String[] tipsUris = activity.getResources().getStringArray(R.array.tips_dialog_uri);
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tipsUris[tipIndex])));
    }
}