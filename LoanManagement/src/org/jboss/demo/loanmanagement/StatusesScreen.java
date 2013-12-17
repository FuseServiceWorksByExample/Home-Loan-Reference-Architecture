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
package org.jboss.demo.loanmanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jboss.demo.loanmanagement.command.GetStatusesCommand;
import org.jboss.demo.loanmanagement.model.ApplicationStatus;
import org.jboss.demo.loanmanagement.model.ApplicationStatusParcelable;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * The Loan Management available statuses screen.
 */
public final class StatusesScreen extends Activity {

    private Comparator<ApplicationStatus> sorter = ApplicationStatus.SSN_SORTER;
    private ApplicationStatus[] statuses;

    private void buildTable() {
        Log.d(StatusesScreen.class.getSimpleName(), "Building statuses table"); //$NON-NLS-1$
        Arrays.sort(this.statuses, this.sorter);

        // create view
        final TableLayout statusTable = (TableLayout)findViewById(R.id.status_table);
        statusTable.removeAllViews();

        final List<TextView> ssns = new ArrayList<TextView>(this.statuses.length);
        final List<TextView> names = new ArrayList<TextView>(this.statuses.length);
        final List<TextView> rates = new ArrayList<TextView>(this.statuses.length);
        final List<TextView> appStatuses = new ArrayList<TextView>(this.statuses.length);

        int ssnWidth = 0;
        int nameWidth = 0;
        int rateWidth = 0;
        int statusWidth = 0;

        // create table rows
        for (final ApplicationStatus applicationStatus : this.statuses) {
            final TableRow row = new TableRow(this);
            final TableRow.LayoutParams params =
                            new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 8;
            params.rightMargin = 8;
            params.bottomMargin = 18;
            params.topMargin = 18;
            row.setLayoutParams(params);
            row.setPadding(14, 14, 14, 14);

            { // SSN
                final TextView txt = new TextView(this);
                txt.setText(Util.formatSsn(applicationStatus.getSsn()));
                txt.setPadding(4, 4, 4, 4);
                txt.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(txt);

                txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                if (txt.getMeasuredWidth() > ssnWidth) {
                    ssnWidth = txt.getMeasuredWidth();
                }

                ssns.add(txt);
            }

            { // name
                final TextView txt = new TextView(this);
                txt.setText(applicationStatus.getApplicant());
                txt.setPadding(4, 4, 4, 4);
                txt.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(txt);

                txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                if (txt.getMeasuredWidth() > nameWidth) {
                    nameWidth = txt.getMeasuredWidth();
                }

                names.add(txt);
            }

            { // status
                final TextView txt = new TextView(this);
                txt.setText(applicationStatus.getStatus());
                txt.setPadding(4, 4, 4, 4);
                txt.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(txt);

                txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                if (txt.getMeasuredWidth() > statusWidth) {
                    statusWidth = txt.getMeasuredWidth();
                }

                appStatuses.add(txt);
            }

            { // rate
                final TextView txt = new TextView(this);
                txt.setText(Float.toString(applicationStatus.getRate()));
                txt.setPadding(4, 4, 4, 4);
                txt.setBackgroundResource(R.drawable.table_cell_border);
                row.addView(txt);

                txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

                if (txt.getMeasuredWidth() > rateWidth) {
                    rateWidth = txt.getMeasuredWidth();
                }

                rates.add(txt);
            }

            statusTable.addView(row);
        }

        { // add blank row at the bottom
            final TableRow emptyRow = new TableRow(this);
            final TableRow.LayoutParams params =
                            new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 8;
            params.rightMargin = 8;
            params.bottomMargin = 18;
            params.topMargin = 18;
            emptyRow.setLayoutParams(params);
            emptyRow.setPadding(14, 14, 14, 14);
            emptyRow.addView(new TextView(this));
            statusTable.addView(emptyRow);
        }

        { // SSN header
            final TextView txt = (TextView)findViewById(R.id.ssn_header);
            txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            if (txt.getMeasuredWidth() > ssnWidth) {
                ssnWidth = txt.getMeasuredWidth();
            }

            ssns.add(txt);
        }

        { // name header
            final TextView txt = (TextView)findViewById(R.id.applicant_header);
            txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            if (txt.getMeasuredWidth() > nameWidth) {
                nameWidth = txt.getMeasuredWidth();
            }

            names.add(txt);
        }

        { // status header
            final TextView txt = (TextView)findViewById(R.id.status_header);
            txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            if (txt.getMeasuredWidth() > statusWidth) {
                statusWidth = txt.getMeasuredWidth();
            }

            appStatuses.add(txt);
        }

        { // rate header
            final TextView txt = (TextView)findViewById(R.id.rate_header);
            txt.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

            if (txt.getMeasuredWidth() > rateWidth) {
                rateWidth = txt.getMeasuredWidth();
            }

            rates.add(txt);
        }

        for (final TextView txt : ssns) {
            txt.setWidth(ssnWidth);
        }

        for (final TextView txt : names) {
            txt.setWidth(nameWidth);
        }

        for (final TextView txt : appStatuses) {
            txt.setWidth(statusWidth);
        }

        for (final TextView txt : rates) {
            txt.setWidth(rateWidth);
        }
    }

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the refresh action (never <code>null</code>)
     */
    public void handleRefresh( final MenuItem item ) {
        Log.d(StatusesScreen.class.getSimpleName(), "Application Statuses refresh selected"); //$NON-NLS-1$

        final GetStatusesCommand command = new GetStatusesCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetStatusesCommand#process(org.jboss.demo.loanmanagement.model.ApplicationStatus[])
             */
            @Override
            protected void process( final ApplicationStatus[] result ) {
                setStatuses(result);
            }
        };

        command.execute((Void[])null);
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statuses_screen);

        // load statuses
        final ArrayList<ApplicationStatusParcelable> data =
                        getIntent().getExtras().getParcelableArrayList(ApplicationStatusParcelable.STATUSES);

        if ((data == null) || data.isEmpty()) {
            this.statuses = ApplicationStatus.NO_STATUSES;
        } else {
            this.statuses = new ApplicationStatus[data.size()];
            int i = 0;

            for (final ApplicationStatusParcelable parcelable : data) {
                this.statuses[i++] = parcelable.getStatus();
            }

            Arrays.sort(this.statuses, ApplicationStatus.NAME_SORTER);
        }

        updateStatusMessage(this.statuses.length);
        buildTable();
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.statuses_screen_menu, optionsMenu);

        final MenuItem sortItem = optionsMenu.findItem(R.id.menu_sort);
        final SubMenu sortMenu = sortItem.getSubMenu();

        // TODO this needs to be a preference
        final MenuItem sortBySsnItem = sortMenu.findItem(R.id.action_sort_by_ssn);
        sortBySsnItem.setChecked(true);

        return true; // show menu and action bar items
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected( final MenuItem selectedItem ) {
        final int selectedItemId = selectedItem.getItemId();

        if (selectedItemId == R.id.action_sort_by_name) {
            selectedItem.setChecked(true);
            this.sorter = ApplicationStatus.NAME_SORTER;
            buildTable();
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_ssn) {
            selectedItem.setChecked(true);
            this.sorter = ApplicationStatus.SSN_SORTER;
            buildTable();
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_rate) {
            selectedItem.setChecked(true);
            this.sorter = ApplicationStatus.RATE_SORTER;
            buildTable();
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_status) {
            selectedItem.setChecked(true);
            this.sorter = ApplicationStatus.STATUS_SORTER;
            buildTable();
            return true;
        }

        return super.onOptionsItemSelected(selectedItem);
    }

    protected void setStatuses( final ApplicationStatus[] newStatuses ) {
        this.statuses = newStatuses;
        buildTable();
    }

    private void updateStatusMessage( final int numStatuses ) {
        final TextView msgView = (TextView)findViewById(R.id.status_bar_message);
        msgView.setText(getString(R.string.number_of_statuses, numStatuses));
    }

}
