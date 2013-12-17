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

import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.validate.ApplicationValidator;
import org.jboss.demo.loanmanagement.widget.ApplicationAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

/**
 * The UI for creating a loan application.
 */
public final class ApplicationDialog extends DialogFragment implements
                                                           DialogInterface.OnClickListener,
                                                           DialogInterface.OnShowListener {

    private ApplicationAdapter adapter;
    private AlertDialog dialog;
    private Application copy;
    private Application original;
    private ExpandableListView listView;

    /**
     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
     */
    @Override
    public void onClick( final DialogInterface dialog,
                         final int buttonId ) {
        assert (buttonId == DialogInterface.BUTTON_POSITIVE); // only have an OK button listener

        if (this.dialog != null) {
            this.dialog.dismiss();

            // TODO run submit application command
        }
    }

    /**
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog( final Bundle newSavedInstanceState ) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.application_dialog, null);

        this.listView = (ExpandableListView)view.findViewById(R.id.applicationList);
        this.adapter = new ApplicationAdapter(getActivity());
        this.listView.setAdapter(this.adapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.dialog =
                        builder.setView(view).setTitle(R.string.application_dialog_title).setIcon(R.drawable.ic_home)
                               .setNegativeButton(android.R.string.cancel, null)
                               .setPositiveButton(android.R.string.ok, this).create();
        this.dialog.setOnShowListener(this);

        return this.dialog;
    }

    /**
     * @see android.content.DialogInterface.OnShowListener#onShow(android.content.DialogInterface)
     */
    @Override
    public void onShow( final DialogInterface dialogInterface ) {
        updateState();
    }

    void setApplication( final Application edit ) {
        this.original = edit;
        this.copy = Application.copy(this.original);
    }

    private void updateState() {
        final Button btn = this.dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (btn != null) {
            final boolean valid = ApplicationValidator.isValid(this.copy);

            if (btn.isEnabled() != valid) {
                btn.setEnabled(valid);
            }
        }
    }

}
