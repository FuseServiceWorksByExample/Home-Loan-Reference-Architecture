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
import org.jboss.demo.loanmanagement.command.GetEvaluationsCommand;
import org.jboss.demo.loanmanagement.command.GetStatusesCommand;
import org.jboss.demo.loanmanagement.model.ApplicationStatus;
import org.jboss.demo.loanmanagement.model.ApplicationStatusParcelable;
import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.model.EvaluationParcelable;
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
import android.widget.TextView;

/**
 * The Loan Management main screen.
 */
public final class LoanManagementScreen extends Activity {

    Context getContext() {
        return this;
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_management_screen);

        // set version in status bar
        final TextView msg = (TextView)findViewById(R.id.status_bar_message);
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
     * Method required by the <code>onClick</code> XML attribute.
     * 
     * @param view the view that was selected
     */
    @SuppressWarnings( "static-method" )
    public void onHelpSelected( final View view ) {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Help selected"); //$NON-NLS-1$
    }

    /**
     * Method required by the <code>onClick</code> XML attribute.
     * 
     * @param view the view that was selected
     */
    public void onNewApplicationSelected( final View view ) {
        Log.d(LoanManagementScreen.class.getSimpleName(), "New Application selected"); //$NON-NLS-1$

        final Intent intent = new Intent(getContext(), ApplicationScreen.class);
        startActivity(intent);
    }

    /**
     * Method required by the <code>onClick</code> XML attribute.
     * 
     * @param view the view that was selected
     */
    public void onViewApplicationEvaluations( final View view ) {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Application Evaluations selected"); //$NON-NLS-1$

        final GetEvaluationsCommand command = new GetEvaluationsCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetEvaluationsCommand#process(java.util.List)
             */
            @Override
            protected void process( final List<Evaluation> evaluations ) {
                // pass result to evaluations screen
                ArrayList<EvaluationParcelable> data = null;

                if ((evaluations == null) || evaluations.isEmpty()) {
                    data = Evaluation.NO_EVALUATIONS_LIST;
                } else {
                    data = new ArrayList<EvaluationParcelable>(evaluations.size());

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

    /**
     * Method required by the <code>onClick</code> XML attribute.
     * 
     * @param view the view that was selected
     */
    public void onViewApplicationStatusesSelected( final View view ) {
        Log.d(LoanManagementScreen.class.getSimpleName(), "Application Statuses selected"); //$NON-NLS-1$

        final GetStatusesCommand command = new GetStatusesCommand(this) {

            /**
             * @see org.jboss.demo.loanmanagement.command.GetStatusesCommand#process(java.util.List)
             */
            @Override
            protected void process( final List<ApplicationStatus> statuses ) {
                // pass result to statuses screen
                ArrayList<ApplicationStatusParcelable> data = null;

                if ((statuses == null) || statuses.isEmpty()) {
                    data = ApplicationStatus.NO_STATUSES_LIST;
                } else {
                    data = new ArrayList<ApplicationStatusParcelable>(statuses.size());

                    for (final ApplicationStatus status : statuses) {
                        final ApplicationStatusParcelable parcelable = new ApplicationStatusParcelable(status);
                        data.add(parcelable);
                    }
                }

                final Intent intent = new Intent(getContext(), StatusesScreen.class);
                intent.putParcelableArrayListExtra(ApplicationStatusParcelable.STATUSES, data);

                startActivity(intent);
            }
        };

        command.execute((Void[])null);
    }

}
