/*
 * Copyright 2013-2014 JBoss Inc
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

import java.util.Comparator;
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.Util.Prefs;
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

    private static final String DEFAULT_SORTER_ID = Prefs.SORT_BY_SSN;

    private Comparator<ApplicationStatus> sorter;

    /**
     * @param context the statuses screen (cannot be <code>null</code>)
     * @param appStatuses the statuses (cannot be <code>null</code> or empty)
     */
    public StatusesAdapter( final Context context,
                            final List<ApplicationStatus> appStatuses ) {
        super(context, R.layout.statuses_screen, appStatuses);

        // setup sorter
        final String sortBy = Util.getPreferences(context).getString(Util.Prefs.STATUS_SORTER, DEFAULT_SORTER_ID);

        if (Prefs.SORT_BY_NAME.equals(sortBy)) {
            this.sorter = ApplicationStatus.NAME_SORTER;
        } else if (Prefs.SORT_BY_SSN.equals(sortBy)) {
            this.sorter = ApplicationStatus.SSN_SORTER;
        } else if (Prefs.SORT_BY_RATE.equals(sortBy)) {
            this.sorter = ApplicationStatus.RATE_SORTER;
        } else {
            this.sorter = ApplicationStatus.STATUS_SORTER;
        }

        sort(this.sorter);
    }

    /**
     * @return the sorter identifier
     * @see Prefs#SORT_BY_NAME
     * @see Prefs#SORT_BY_SSN
     */
    public String getSorterId() {
        if (this.sorter == ApplicationStatus.NAME_SORTER) {
            return Prefs.SORT_BY_NAME;
        }

        if (this.sorter == ApplicationStatus.SSN_SORTER) {
            return Prefs.SORT_BY_SSN;
        }

        if (this.sorter == ApplicationStatus.RATE_SORTER) {
            return Prefs.SORT_BY_RATE;
        }

        return Prefs.SORT_BY_STATUS;
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
     * @param sorterId the identifier of the new sorter (can be <code>null</code> if default sorter should be used)
     * @see ApplicationStatus#NAME_SORTER
     * @see ApplicationStatus#RATE_SORTER
     * @see ApplicationStatus#SSN_SORTER
     * @see ApplicationStatus#STATUS_SORTER
     */
    public void setSorter( final String sorterId ) {
        String newId = sorterId;
        Comparator<ApplicationStatus> statusSorter = null;

        if (Util.isBlank(newId)) {
            newId = DEFAULT_SORTER_ID;
        }

        if (Prefs.SORT_BY_NAME.equals(newId)) {
            statusSorter = ApplicationStatus.NAME_SORTER;
        } else if (Prefs.SORT_BY_SSN.equals(newId)) {
            statusSorter = ApplicationStatus.SSN_SORTER;
        } else if (Prefs.SORT_BY_RATE.equals(newId)) {
            statusSorter = ApplicationStatus.RATE_SORTER;
        } else {
            statusSorter = ApplicationStatus.STATUS_SORTER;
        }

        if (statusSorter != null) {
            this.sorter = statusSorter;
            sort(this.sorter);

            // save new preference value
            Util.getPreferenceEditor(getContext()).putString(Prefs.STATUS_SORTER, newId).apply();
        }
    }

    /**
     * @param newStatuses the available application statuses (can be <code>null</code>)
     */
    public void setStatuses( final List<ApplicationStatus> newStatuses ) {
        clear();

        if ((newStatuses != null) && !newStatuses.isEmpty()) {
            addAll(newStatuses);
            sort(this.sorter);
        }
    }

    class ItemHolder {

        ImageButton btnStatus;
        TextView nameView;
        TextView rateView;
        TextView ssnView;
        TextView statusView;

    }

}
