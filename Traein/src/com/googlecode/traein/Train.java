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

public class Train {
    private final String mOrigin;

    private final String mDestination;

    private final boolean mTerminus;

    private final String mTime;

    public Train(String origin, String destination, boolean terminus, String time) {
        mOrigin = origin;
        mDestination = destination;
        mTerminus = terminus;
        mTime = time;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDestination() {
        return mDestination;
    }

    public boolean isTerminus() {
        return mTerminus;
    }

    public String getTime() {
        return mTime;
    }

    @Override
    public int hashCode() {
        return mOrigin.hashCode() ^ mDestination.hashCode() ^ mTime.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Train)) {
            return false;
        } else {
            Train o = (Train)other;
            return mTerminus == o.mTerminus && mOrigin.equals(o.mOrigin)
                    && mDestination.equals(o.mDestination) && mTime.equals(o.mTime);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Train < mOrigin:'");
        sb.append(mOrigin);
        sb.append("' mDestination:'");
        sb.append(mDestination);
        sb.append("' mTime:'");
        sb.append(mTime);
        sb.append("' mTerminus:");
        sb.append(mTerminus);
        sb.append(" >");
        return sb.toString();
    }
}
