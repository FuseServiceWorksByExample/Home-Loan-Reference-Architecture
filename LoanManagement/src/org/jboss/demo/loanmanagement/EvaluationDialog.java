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

import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.validate.EvaluationValidator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * The UI for editing an evaluation.
 */
public final class EvaluationDialog extends DialogFragment implements
                                                          DialogInterface.OnClickListener,
                                                          DialogInterface.OnShowListener {

    private int returnCode = DialogInterface.BUTTON_NEGATIVE;
    private AlertDialog dialog;
    private Evaluation evaluation;

    /**
     * Method required by the onClick XML attribute in layout.
     * 
     * @param view the radio button
     */
    public void handleApproveSelected( final View view ) {
        this.evaluation.setApproved(true);
    }

    /**
     * Method required by the onClick XML attribute in layout.
     * 
     * @param view the radio button
     */
    public void handleRejectSelected( final View view ) {
        this.evaluation.setApproved(false);
    }

    /**
     * @return <code>true</code> if the dialog was not canceled
     */
    public boolean okPressed() {
        return (this.returnCode != DialogInterface.BUTTON_NEGATIVE);
    }

    /**
     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
     */
    @Override
    public void onClick( final DialogInterface dialogInterface,
                         final int buttonId ) {
        if (this.dialog != null) {
            this.returnCode = buttonId;
            this.dialog.dismiss();
        }
    }

    /**
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog( final Bundle savedInstanceState ) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.evaluate_dialog, null);

        { // SSN
            final TextView txtView = (TextView)view.findViewById(R.id.txtSsn);
            txtView.setText(Util.formatSsn(this.evaluation.getSsn()));
        }

        { // name
            final TextView txtView = (TextView)view.findViewById(R.id.txtApplicant);
            txtView.setText(this.evaluation.getApplicant());
        }

        { // credit score
            final TextView txtView = (TextView)view.findViewById(R.id.txtCreditScore);
            txtView.setText(Integer.toString(this.evaluation.getCreditScore()));
        }

        { // approval
            final boolean approved = this.evaluation.isApproved();

            if (approved) {
                final RadioButton btn = (RadioButton)view.findViewById(R.id.btnApprove);
                btn.setChecked(true);
            } else {
                final RadioButton btn = (RadioButton)view.findViewById(R.id.btnReject);
                btn.setChecked(true);
            }
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.dialog =
                        builder.setView(view).setTitle(R.string.evaluate_dialog_title).setIcon(R.drawable.ic_home)
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
        // disable OK button if evaluation is not valid
        if (!EvaluationValidator.isValid(this.evaluation)) {
            this.dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    void setEvaluation( final Evaluation edit ) {
        this.evaluation = edit;
    }

}
