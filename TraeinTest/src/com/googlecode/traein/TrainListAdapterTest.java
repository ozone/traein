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

import android.database.DataSetObserver;
import android.test.AndroidTestCase;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class TrainListAdapterTest extends AndroidTestCase {

    private static final class MockDataSetObserver extends DataSetObserver {
        int onChangedCallCount = 0;

        int onInvalidatedCallCount = 0;

        @Override
        public void onChanged() {
            onChangedCallCount++;
        }

        @Override
        public void onInvalidated() {
            onInvalidatedCallCount++;
        }
    }

    private ArrayList<Train> mTrains;

    private TrainListAdapter mAdapter;

    private TrainListAdapter mEmptyAdapter;

    @Override
    protected void setUp() {
        mTrains = new ArrayList<Train>();
        mTrains.add(new Train("Bray", "Howth", "12:40"));
        mTrains.add(new Train("Howth", "Bray", "12:45"));
        mTrains.add(new Train("Greystones", "Howth", "12:50"));
        mAdapter = new TrainListAdapter(getContext(), mTrains);
        mEmptyAdapter = new TrainListAdapter(getContext(), new ArrayList<Train>());
    }

    public void testGetCount() {
        assertEquals(mTrains.size(), mAdapter.getCount());
        assertEquals(0, mEmptyAdapter.getCount());
    }

    public void testGetItem() {
        for (int i = 0; i < mTrains.size(); i++) {
            assertEquals(mTrains.get(i), mAdapter.getItem(i));
        }
    }

    public void testGetItemId() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            assertEquals(i, mAdapter.getItemId(i));
        }
    }

    public void testGetItemViewType() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            assertEquals(R.layout.train_list_item, mAdapter.getItemViewType(i));
        }
    }

    public void testGetView() {
        mTrains.add(new Train("Bray", "Howth", "12:40"));
        mTrains.add(new Train("Howth", "Bray", "12:45"));
        mTrains.add(new Train("Greystones", "Howth", "12:50"));

        View view = mAdapter.getView(0, null, null);
        TextView text1 = (TextView)view.findViewById(android.R.id.text1);
        TextView text2 = (TextView)view.findViewById(android.R.id.text2);
        assertEquals("12:40 Howth", text1.getText());
        assertEquals("From Bray", text2.getText());

        view = mAdapter.getView(1, view, null);
        assertEquals("12:45 Bray", text1.getText());
        assertEquals("From Howth", text2.getText());

        view = mAdapter.getView(2, view, null);
        assertEquals("12:50 Howth", text1.getText());
        assertEquals("From Greystones", text2.getText());
    }

    public void testGetViewTypeCount() {
        assertEquals(1, mAdapter.getViewTypeCount());
    }

    public void testHasStableIds() {
        assertFalse(mAdapter.hasStableIds());
    }

    public void testIsEmpty() {
        assertFalse(mAdapter.isEmpty());
        assertTrue(mEmptyAdapter.isEmpty());
    }

    public void registerDataSetObserver() {
        MockDataSetObserver observer = new MockDataSetObserver();
        mAdapter.registerDataSetObserver(observer);
        assertEquals(0, observer.onChangedCallCount);
        assertEquals(0, observer.onInvalidatedCallCount);
        mAdapter.notifyDataSetChanged();
        assertEquals(1, observer.onChangedCallCount);
        assertEquals(0, observer.onInvalidatedCallCount);
        mAdapter.notifyDataSetInvalidated();
        assertEquals(1, observer.onChangedCallCount);
        assertEquals(1, observer.onInvalidatedCallCount);
    }

    public void unregisterDataSetObserver() {
        MockDataSetObserver observer = new MockDataSetObserver();
        mAdapter.registerDataSetObserver(observer);
        mAdapter.unregisterDataSetObserver(observer);
        assertEquals(0, observer.onChangedCallCount);
        assertEquals(0, observer.onInvalidatedCallCount);
        mAdapter.notifyDataSetChanged();
        assertEquals(0, observer.onChangedCallCount);
        assertEquals(0, observer.onInvalidatedCallCount);
        mAdapter.notifyDataSetInvalidated();
        assertEquals(0, observer.onChangedCallCount);
        assertEquals(0, observer.onInvalidatedCallCount);
    }

    public void testAreAllItemsEnabled() {
        assertTrue(mAdapter.areAllItemsEnabled());
    }

    public void isEnabled(int position) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            assertTrue(mAdapter.isEnabled(i));
        }
    }
}
