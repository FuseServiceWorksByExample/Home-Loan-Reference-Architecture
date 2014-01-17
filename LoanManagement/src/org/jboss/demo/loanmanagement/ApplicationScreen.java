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

import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.widget.ApplicationAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

/**
 * The loan application editor screen.
 */
public class ApplicationScreen extends Activity {

    private ApplicationAdapter adapter;
    private ExpandableListView applicationEditor;

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application);

        this.applicationEditor = (ExpandableListView)findViewById(R.id.application_expandable_list);
        final Application loanApplication = new Application();
        this.adapter = new ApplicationAdapter(this, this.applicationEditor, loanApplication);
        this.applicationEditor.setAdapter(this.adapter);
    }

}
