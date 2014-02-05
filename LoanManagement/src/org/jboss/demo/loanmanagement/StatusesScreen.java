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
package org.jboss.demo.loanmanagement;

import java.util.ArrayList;
import java.util.List;
import org.jboss.demo.loanmanagement.Util.Prefs;
import org.jboss.demo.loanmanagement.command.GetStatusesCommand;
import org.jboss.demo.loanmanagement.model.ApplicationStatus;
import org.jboss.demo.loanmanagement.model.ApplicationStatusParcelable;
import org.jboss.demo.loanmanagement.widget.StatusesAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Loan Management available statuses screen.
 */
public final class StatusesScreen extends Activity {

    private StatusesAdapter adapter;
    private ListView listView;

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the refresh action (never <code>null</code>)
     */
    public void handleRefresh( final MenuItem item ) {
        Log.d(StatusesScreen.class.getSimpleName(), "Application statuses refresh selected"); //$NON-NLS-1$

        final GetStatusesCommand command = new GetStatusesCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetStatusesCommand#process(java.util.List)
             */
            @Override
            protected void process( final List<ApplicationStatus> statuses ) {
                setStatuses(statuses);
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

        this.listView = (ListView)findViewById(R.id.statuses_list);

        { // view when there are no available statuses
            final View emptyListView = findViewById(R.id.empty_status_item);
            this.listView.setEmptyView(emptyListView);
        }

        // load statuses
        final ArrayList<ApplicationStatusParcelable> data =
                        getIntent().getExtras().getParcelableArrayList(ApplicationStatusParcelable.STATUSES);
        List<ApplicationStatus> statuses;

        if ((data == null) || data.isEmpty()) {
            statuses = ApplicationStatus.NO_STATUSES;
        } else {
            statuses = new ArrayList<ApplicationStatus>(data.size());

            for (final ApplicationStatusParcelable parcelable : data) {
                statuses.add(parcelable.getStatus());
            }
        }

        this.adapter = new StatusesAdapter(this, statuses);
        this.listView.setAdapter(this.adapter);

        // set number of statuses in status bar
        updateStatusMessage();

        // add up arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.statuses_screen_menu, optionsMenu);

        final MenuItem sortItem = optionsMenu.findItem(R.id.menu_sort);
        final SubMenu sortMenu = sortItem.getSubMenu();

        // find out which sort to turn on
        final String sorterId = this.adapter.getSorterId();

        if (Prefs.SORT_BY_NAME.equals(sorterId)) {
            final MenuItem item = sortMenu.findItem(R.id.action_sort_by_name);
            item.setChecked(true);
        } else if (Prefs.SORT_BY_SSN.equals(sorterId)) {
            final MenuItem item = sortMenu.findItem(R.id.action_sort_by_ssn);
            item.setChecked(true);
        } else if (Prefs.SORT_BY_RATE.equals(sorterId)) {
            final MenuItem item = sortMenu.findItem(R.id.action_sort_by_rate);
            item.setChecked(true);
        } else {
            final MenuItem item = sortMenu.findItem(R.id.action_sort_by_status);
            item.setChecked(true);
        }

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
            this.adapter.setSorter(Prefs.SORT_BY_NAME);
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_ssn) {
            selectedItem.setChecked(true);
            this.adapter.setSorter(Prefs.SORT_BY_SSN);
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_rate) {
            selectedItem.setChecked(true);
            this.adapter.setSorter(Prefs.SORT_BY_RATE);
            return true;
        }

        if (selectedItemId == R.id.action_sort_by_status) {
            selectedItem.setChecked(true);
            this.adapter.setSorter(Prefs.SORT_BY_STATUS);
            return true;
        }

        if (selectedItemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(selectedItem);
    }

    protected void setStatuses( final List<ApplicationStatus> newStatuses ) {
        this.adapter.setStatuses(newStatuses);
        updateStatusMessage();
    }

    private void updateStatusMessage() {
        final TextView msgView = (TextView)findViewById(R.id.status_bar_message);
        msgView.setText(getString(R.string.number_of_statuses, this.adapter.getCount()));
    }

}
