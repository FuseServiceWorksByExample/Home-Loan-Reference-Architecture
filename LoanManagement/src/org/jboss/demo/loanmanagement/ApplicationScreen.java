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

import org.jboss.demo.loanmanagement.command.ProcessApplicationCommand;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.widget.ApplicationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

/**
 * The loan application editor screen.
 */
public final class ApplicationScreen extends Activity {

    private ApplicationAdapter adapter;
    private Application application;
    private ExpandableListView applicationEditor;

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
        this.adapter = new ApplicationAdapter(this, this.applicationEditor, this.application);
        this.applicationEditor.setAdapter(this.adapter);
    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( final Menu optionsMenu ) {
        getMenuInflater().inflate(R.menu.application_screen_menu, optionsMenu);
        return true;
    }

}
