/*
 * Copyright 2013 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.jboss.demo.loanmanagement.widget;

import org.jboss.demo.loanmanagement.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The main screen list adapter.
 */
public final class LoanManagementAdapter extends ArrayAdapter<String[]> {

    /**
     * @param screen the main screen (cannot be <code>null</code>)
     * @param items the main menu items and their short description (cannot be <code>null</code> or empty)
     */
    public LoanManagementAdapter( final Context screen,
                                  final String[][] items ) {
        super(screen, R.layout.main_screen_list_row, items);
    }

    /**
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView( final int position,
                         final View view,
                         final ViewGroup parent ) {
        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = view;
        ItemHolder holder;

        if (null == view) {
            itemView = inflater.inflate(R.layout.main_screen_list_row, parent, false);
            holder = new ItemHolder();
            holder.nameView = (TextView)itemView.findViewById(R.id.mainScreenListItem);
            holder.descriptionView = (TextView)itemView.findViewById(R.id.mainScreenListItemDescription);
            itemView.setTag(holder);
        } else {
            holder = (ItemHolder)view.getTag();
        }

        final String[] item = getItem(position);
        holder.nameView.setText(item[0]);
        holder.descriptionView.setText(item[1]);

        return itemView;
    }

    class ItemHolder {

        TextView nameView;
        TextView descriptionView;

    }

}
