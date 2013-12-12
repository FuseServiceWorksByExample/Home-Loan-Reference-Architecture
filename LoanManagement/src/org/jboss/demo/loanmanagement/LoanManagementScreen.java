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
import org.jboss.demo.loanmanagement.command.GetEvaluationsCommand;
import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.model.EvaluationParcelable;
import org.jboss.demo.loanmanagement.widget.LoanManagementAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The Loan Management main screen.
 */
public final class LoanManagementScreen extends Activity implements OnItemClickListener {

    Context getContext() {
        return this;
    }

    private void handleEvaluationsSelected() {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Application Evaluations selected"); //$NON-NLS-1$

        final GetEvaluationsCommand command = new GetEvaluationsCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetEvaluationsCommand#process(org.jboss.demo.loanmanagement.model.Evaluation[])
             */
            @Override
            protected void process( final Evaluation[] evaluations ) {
                // pass result to evaluations screen
                ArrayList<EvaluationParcelable> data = null;

                if (evaluations.length == 0) {
                    data = Evaluation.NO_EVALUATIONS_LIST;
                } else {
                    data = new ArrayList<EvaluationParcelable>(evaluations.length);

                    for (final Evaluation eval : evaluations) {
                        final EvaluationParcelable parcelable = new EvaluationParcelable(eval);
                        data.add(parcelable);
                    }
                }

                final Intent intent = new Intent(getContext(), EvaluationsScreen.class);
                intent.putParcelableArrayListExtra(EvaluationParcelable.EVALUATIONS, data);

                startActivity(intent);
            }
        };

        command.execute((Void[])null);
    }

    private void handleHelpSelected() {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Help selected"); //$NON-NLS-1$
        // TODO Auto-generated method stub
    }

    private void handleNewApplicationSelected() {
        Log.d(LoanManagementScreen.class.getSimpleName(), "New Application selected"); //$NON-NLS-1$
        // TODO Auto-generated method stub
    }

    private void handleStatusesSelected() {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Application Statuses selected"); //$NON-NLS-1$
        // TODO Auto-generated method stub
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_management_screen);

        // add items to list. make sure order is same as in item click listener
        final String[][] items =
                        new String[][] {
                                        {getString(R.string.item_new_application),
                                         getString(R.string.item_new_application_description)},
                                        {getString(R.string.item_application_statuses),
                                         getString(R.string.item_application_statuses_description)},
                                        {getString(R.string.item_application_evaluations),
                                         getString(R.string.item_application_evaluations_description)},
                                        {getString(R.string.item_help), getString(R.string.item_help_description)}};
        final ListView listView = (ListView)findViewById(R.id.mainScreenList);
        listView.setAdapter(new LoanManagementAdapter(this, items));
        listView.setOnItemClickListener(this);

        // set version in status bar
        final TextView msg = (TextView)findViewById(R.id.statusBarMessage);
        final PackageManager pkgMgr = getPackageManager();
        final ApplicationInfo appInfo = getApplicationInfo();

        try {
            final PackageInfo pkgInfo = pkgMgr.getPackageInfo(getPackageName(), 0);
            msg.setText(pkgMgr.getApplicationLabel(appInfo) + " " + pkgInfo.versionName); //$NON-NLS-1$
        } catch (final NameNotFoundException e) {
            Log.e(LoanManagementScreen.class.getSimpleName(), e.getLocalizedMessage(), e);
            finish();
        }
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu menu ) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
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
        switch (position) {
            case 0:
                handleNewApplicationSelected();
                break;
            case 1:
                handleStatusesSelected();
                break;
            case 2:
                handleEvaluationsSelected();
                break;
            case 3:
                handleHelpSelected();
                break;
            default:
                // TODO handle this
        }
    }

}
