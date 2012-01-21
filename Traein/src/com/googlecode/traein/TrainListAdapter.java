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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrainListAdapter extends ArrayAdapter<Train> {
    private static final int RESOURCE = R.layout.train_list_item;

    private final LayoutInflater mLayoutInflater;

    public TrainListAdapter(Context context, List<Train> trains) {
        super(context, RESOURCE, trains);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return RESOURCE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(RESOURCE, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Train train = getItem(position);
        holder.setText1(renderText1(train));
        holder.setText2(renderText2(train));
        return convertView;
    }

    private String renderText1(Train train) {
        return getContext().getString(R.string.train_destination, train.getTime(),
                train.getDestination());
    }

    private String renderText2(Train train) {
        return getContext().getString(R.string.train_origin, train.getOrigin());
    }

    private static class ViewHolder {
        private final TextView mTextView1;

        private final TextView mTextView2;

        public ViewHolder(View convertView) {
            mTextView1 = (TextView)convertView.findViewById(android.R.id.text1);
            mTextView2 = (TextView)convertView.findViewById(android.R.id.text2);
        }

        public void setText1(String text) {
            mTextView1.setText(text);
        }

        public void setText2(String text) {
            mTextView2.setText(text);
        }
    }
}
