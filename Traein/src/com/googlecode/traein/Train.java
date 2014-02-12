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

    public static final Comparator<Train> BY_DUE_TIME = new Comparator<Train>() {
        public int compare(Train lhs, Train rhs) {
        	return lhs.getDueInMinutes() - rhs.getDueInMinutes();
        }
    };

    private final String mOrigin;

    private final String mDestination;

    private final int mDueInMin;

    public Train(String origin, String destination, int dueInMin) {
        mOrigin = origin;
        mDestination = destination;
        mDueInMin = dueInMin;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDestination() {
        return mDestination;
    }

    public int getDueInMinutes() {
        return mDueInMin;
    }

    @Override
    public int hashCode() {
        return mOrigin.hashCode() ^ mDestination.hashCode() ^ mDueInMin;
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
                    && mDueInMin == o.mDueInMin;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Train < mOrigin:'").append(mOrigin)
                .append("' mDestination:'").append(mDestination).append("' mDueMin:'").append(mDueInMin)
                .append(" >").toString();
    }
}
