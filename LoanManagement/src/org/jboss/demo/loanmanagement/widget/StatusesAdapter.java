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

import java.util.Arrays;
import java.util.Comparator;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.ApplicationStatus;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * The available application statuses screen list adapter.
 */
public final class StatusesAdapter extends ArrayAdapter<ApplicationStatus> {

    private Comparator<ApplicationStatus> sorter = ApplicationStatus.NAME_SORTER;
    private ApplicationStatus[] statuses;

    /**
     * @param screen the statuses screen (cannot be <code>null</code>)
     * @param appStatuses the statuses (cannot be <code>null</code> or empty)
     */
    public StatusesAdapter( final Context screen,
                            final ApplicationStatus[] appStatuses ) {
        super(screen, R.layout.statuses_screen, appStatuses);
        this.statuses = ((appStatuses == null) ? ApplicationStatus.NO_STATUSES : appStatuses);
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
            itemView = inflater.inflate(R.layout.status_item, parent, false);
            holder = new ItemHolder();
            holder.nameView = (TextView)itemView.findViewById(R.id.app_status_name);
            holder.rateView = (TextView)itemView.findViewById(R.id.app_status_rate);
            holder.ssnView = (TextView)itemView.findViewById(R.id.app_status_ssn);
            holder.statusView = (TextView)itemView.findViewById(R.id.app_status);
            holder.btnStatus = (ImageButton)itemView.findViewById(R.id.img_app_status);
            itemView.setTag(holder);
        } else {
            holder = (ItemHolder)view.getTag();
        }

        final ApplicationStatus appStatus = getItem(position);
        holder.nameView.setText(appStatus.getApplicant());

        { // status
            int imageId;
            String status = null;

            if (appStatus.isApproved()) {
                imageId = R.drawable.ic_approved;
                status = getContext().getString(R.string.app_status_approved);
            } else if (appStatus.isRejected()) {
                imageId = R.drawable.ic_rejected;
                status = getContext().getString(R.string.app_status_rejected);
            } else {
                imageId = R.drawable.ic_pending;
                status = getContext().getString(R.string.app_status_pending);
            }

            holder.btnStatus.setImageResource(imageId);
            holder.statusView.setText(status);
        }

        { // ssn
            final int ssn = appStatus.getSsn();

            if (ApplicationStatus.SSN_NOT_SET != ssn) {
                holder.ssnView.setText(Util.formatSsnWithMask(ssn));
            }
        }

        { // rate
            final double rate = appStatus.getRate();

            if (ApplicationStatus.RATE_NOT_SET != rate) {
                holder.rateView.setText(getContext().getString(R.string.loan_rate, Double.toString(appStatus.getRate())));
            }
        }

        return itemView;
    }

    /**
     * @see android.widget.BaseAdapter#isEnabled(int)
     */
    @Override
    public boolean isEnabled( final int newPosition ) {
        return false;
    }

    /**
     * @param statusSorter the new sorter (can be <code>null</code>)
     */
    public void setSorter( final Comparator<ApplicationStatus> statusSorter ) {
        if (this.sorter != statusSorter) {
            if (statusSorter == null) {
                this.sorter = ApplicationStatus.NAME_SORTER;
            } else {
                this.sorter = statusSorter;
            }

            sort();
        }
    }

    /**
     * @param newStatuses the available application statuses (can be <code>null</code>)
     */
    public void setStatuses( final ApplicationStatus[] newStatuses ) {
        this.statuses = ((newStatuses == null) ? ApplicationStatus.NO_STATUSES : newStatuses);
        sort();
    }

    private void sort() {
        Arrays.sort(this.statuses, this.sorter);
        notifyDataSetChanged();
    }

    class ItemHolder {

        ImageButton btnStatus;
        TextView nameView;
        TextView rateView;
        TextView ssnView;
        TextView statusView;

    }

}
