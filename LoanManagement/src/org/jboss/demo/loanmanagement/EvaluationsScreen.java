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
import org.jboss.demo.loanmanagement.Util.Prefs;
import org.jboss.demo.loanmanagement.command.GetEvaluationsCommand;
import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.model.EvaluationParcelable;
import org.jboss.demo.loanmanagement.widget.EvaluationsAdapter;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Loan Management available evaluations screen.
 */
public final class EvaluationsScreen extends Activity implements OnItemClickListener {

    private EvaluationsAdapter adapter;
    private ListView listView;

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the refresh action (never <code>null</code>)
     */
    public void handleRefresh( final MenuItem item ) {
        Log.d(EvaluationsScreen.class.getSimpleName(), "Application evaluations refresh selected"); //$NON-NLS-1$

        final GetEvaluationsCommand command = new GetEvaluationsCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetEvaluationsCommand#process(org.jboss.demo.loanmanagement.model.Evaluation[])
             */
            @Override
            protected void process( final Evaluation[] result ) {
                setEvaluations(result);
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
        setContentView(R.layout.evaluations_screen);

        this.listView = (ListView)findViewById(R.id.evaluations_screen_list);

        { // view when there are no available evaluations
            final View emptyListView = findViewById(R.id.empty_evaluations_item);
            this.listView.setEmptyView(emptyListView);
        }

        // load evaluations
        final ArrayList<EvaluationParcelable> data =
                        getIntent().getExtras().getParcelableArrayList(EvaluationParcelable.EVALUATIONS);
        Evaluation[] evaluations;

        if ((data == null) || data.isEmpty()) {
            evaluations = Evaluation.NO_EVALUATIONS;
        } else {
            evaluations = new Evaluation[data.size()];
            int i = 0;

            for (final EvaluationParcelable parcelable : data) {
                evaluations[i++] = parcelable.getEvaluation();
            }
        }

        // set number of evaluations in status bar
        updateStatusMessage(evaluations.length);

        this.adapter = new EvaluationsAdapter(this, evaluations);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(this);

        // add up arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.evaluations_screen_menu, optionsMenu);

        final MenuItem sortItem = optionsMenu.findItem(R.id.menu_sort);
        final SubMenu sortMenu = sortItem.getSubMenu();

        // find out which sort to turn on
        final String sorterId = this.adapter.getSorterId();

        if (Prefs.SORT_BY_NAME.equals(sorterId)) {
            final MenuItem sortByNameItem = sortMenu.findItem(R.id.action_sort_by_name);
            sortByNameItem.setChecked(true);
        } else {
            final MenuItem sortBySsnItem = sortMenu.findItem(R.id.action_sort_by_ssn);
            sortBySsnItem.setChecked(true);
        }

        return true; // show menu and action bar items
    }

    /**
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View,
     *      int, long)
     */
    @Override
    public void onItemClick( final AdapterView<?> parent,
                             final View view,
                             final int position,
                             final long id ) {
        final Evaluation selected = this.adapter.getEvaluation(position);
        final FragmentManager fragMgr = getFragmentManager();

        final EvaluationDialog dialog = new EvaluationDialog();
        dialog.setEvaluation(selected);
        dialog.show(fragMgr, EvaluationDialog.class.getSimpleName());
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

        if (selectedItemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(selectedItem);
    }

    protected void setEvaluations( final Evaluation[] newEvaluations ) {
        final Evaluation[] evaluations = ((newEvaluations == null) ? Evaluation.NO_EVALUATIONS : newEvaluations);
        this.adapter = new EvaluationsAdapter(this, evaluations);
        updateStatusMessage(evaluations.length);
    }

    private void updateStatusMessage( final int numEvaluations ) {
        final TextView msgView = (TextView)findViewById(R.id.status_bar_message);
        msgView.setText(getString(R.string.number_of_evaluations, numEvaluations));
    }

}
