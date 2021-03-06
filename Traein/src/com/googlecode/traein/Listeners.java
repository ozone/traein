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
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public final class Listeners {

    private Listeners() {
        // Prevent instantiation by other classes.
    }

    public static OnClickListener finishOnClick(final Activity activity) {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        };
    }

    public static final OnClickListener DISMISS_ON_CLICK = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

}
