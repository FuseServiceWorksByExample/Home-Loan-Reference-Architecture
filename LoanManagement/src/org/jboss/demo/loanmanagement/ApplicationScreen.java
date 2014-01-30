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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jboss.demo.loanmanagement.command.ProcessApplicationCommand;
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.Automobile;
import org.jboss.demo.loanmanagement.model.CashDeposit;
import org.jboss.demo.loanmanagement.widget.ApplicationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

/**
 * The loan application editor screen.
 */
public final class ApplicationScreen extends Activity implements PropertyChangeListener {

    private ApplicationAdapter adapter;
    private Application application;
    private ExpandableListView applicationEditor;

    ApplicationAdapter getAdapter() {
        return this.adapter;
    }

    /**
     * Required by the <code>onClick</code> attribute in the XML file.
     * 
     * @param item the item for the refresh action (never <code>null</code>)
     */
    public void handleProcess( final MenuItem item ) {
        Log.d(ApplicationScreen.class.getSimpleName(), "handleProcess called"); //$NON-NLS-1$

        final ProcessApplicationCommand command = new ProcessApplicationCommand(this);
        command.execute(this.application);
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application);

        this.applicationEditor = (ExpandableListView)findViewById(R.id.application_expandable_list);
        this.application = new Application();
        this.application.add(this);
        this.adapter = new ApplicationAdapter(this, this.applicationEditor, this.application);
        this.applicationEditor.setAdapter(this.adapter);

        // add up arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.application_screen_menu, optionsMenu);
        return true;
    }

    /**
     * Expands the assets and liabilities section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToAssets( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.ASSETS_INDEX);
    }

    /**
     * Expands the borrowers section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToBorrowers( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.BORROWERS_INDEX);
    }

    /**
     * Expands the housing expense section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToHousingExpense( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.HOUSING_EXPENSE_INDEX);
    }

    /**
     * Expands the loan section. Required by the <code>onClick</code> attribute of the xml file.
     * 
     * @param item the menu item (never <code>null</code>)
     */
    public void onJumpToLoan( final MenuItem item ) {
        this.applicationEditor.expandGroup(ApplicationAdapter.LOAN_INDEX);
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected( final MenuItem selectedItem ) {
        final int selectedItemId = selectedItem.getItemId();

        if (selectedItemId == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(selectedItem);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        final String propName = event.getPropertyName();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();

        runOnUiThread(new Thread(new Runnable() {

            /**
             * @see java.lang.Runnable#run()
             */
            @Override
            public void run() {
                if (Application.Properties.ASSETS_LIABILITIES.equals(propName)) {
                    if ((oldValue instanceof Automobile) || (newValue instanceof Automobile)) {
                        getAdapter().refreshAutos();
                    } else if ((oldValue instanceof Account) || (newValue instanceof Account)) {
                        getAdapter().refreshAccounts();
                    } else if ((oldValue instanceof CashDeposit) || (newValue instanceof CashDeposit)) {
                        getAdapter().refreshDeposits();
                    }
                }
            }
        }));
    }

}
