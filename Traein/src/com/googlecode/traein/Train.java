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

import java.util.Comparator;

public class Train {

    public static final Comparator<Train> BY_TIME = new Comparator<Train>() {
        public int compare(Train lhs, Train rhs) {
            if (lhs.getTime().startsWith("00:") && rhs.getTime().startsWith("23:")) {
                return 1;
            } else if (lhs.getTime().startsWith("23:") && rhs.getTime().startsWith("00:")) {
                return -1;
            } else {
                return lhs.getTime().compareTo(rhs.getTime());
            }
        }
    };

    private final String mOrigin;

    private final String mDestination;

    private final String mTime;

    public Train(String origin, String destination, String time) {
        mOrigin = origin;
        mDestination = destination;
        mTime = time;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDestination() {
        return mDestination;
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
            return mOrigin.equals(o.mOrigin) && mDestination.equals(o.mDestination)
                    && mTime.equals(o.mTime);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Train < mOrigin:'").append(mOrigin)
                .append("' mDestination:'").append(mDestination).append("' mTime:'").append(mTime)
                .append(" >").toString();
    }
}
