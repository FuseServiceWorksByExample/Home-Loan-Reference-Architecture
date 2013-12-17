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

import java.util.List;
import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.validate.EvaluationValidator;
import org.jboss.demo.loanmanagement.validate.EvaluationValidator.EvaluationError;
import org.jboss.demo.loanmanagement.widget.TextWatcherAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * The UI for editing an evaluation.
 */
public final class EvaluationDialog extends DialogFragment implements
                                                          DialogInterface.OnClickListener,
                                                          DialogInterface.OnShowListener,
                                                          View.OnClickListener {

    private AlertDialog dialog;
    private Evaluation copy;
    private Evaluation original;

    /**
     * @return the evaluation represented by the dialog
     */
    public Evaluation getEvaluation() {
        return this.copy;
    }

    /**
     * Method required by the onClick XML attribute in layout.
     * 
     * @param view the radio button
     */
    public void handleApproveSelected( final View view ) {
        this.copy.setApproved(true);
        updateState();
    }

    protected void handleExplanationChanged( final String newExplanation ) {
        this.copy.setExplanation(newExplanation);
        updateState();
    }

    protected void handleInsuranceCostChanged( final String newInsuranceCost ) {
        if (TextUtils.isEmpty(newInsuranceCost)) {
            this.copy.setInsuranceCost(Evaluation.NOT_SET);
        } else {
            this.copy.setInsuranceCost(Float.parseFloat(newInsuranceCost));
        }

        updateState();
    }

    protected void handleRateChanged( final String newRate ) {
        if (TextUtils.isEmpty(newRate)) {
            this.copy.setRate(Evaluation.NOT_SET);
        } else {
            this.copy.setRate(Float.parseFloat(newRate));
        }

        updateState();
    }

    /**
     * Method required by the onClick XML attribute in layout.
     * 
     * @param view the radio button
     */
    public void handleRejectSelected( final View view ) {
        this.copy.setApproved(false);
        updateState();
    }

    /**
     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
     */
    @Override
    public void onClick( final DialogInterface dialogInterface,
                         final int buttonId ) {
        assert (buttonId == DialogInterface.BUTTON_POSITIVE); // only have an OK button listener

        if (this.dialog != null) {
            this.dialog.dismiss();

            // TODO run submit evaluation command
        }
    }

    /**
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick( final View view ) {
        if (R.id.btnApprove == view.getId()) {
            this.copy.setApproved(true);
        } else if (R.id.btnReject == view.getId()) {
            this.copy.setApproved(false);
        }

        updateState();
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
            txtView.setText(Util.formatSsn(this.copy.getSsn()));
        }

        { // name
            final TextView txtView = (TextView)view.findViewById(R.id.txtApplicant);
            txtView.setText(this.copy.getApplicant());
        }

        { // credit score
            final TextView txtView = (TextView)view.findViewById(R.id.txtCreditScore);
            txtView.setText(Integer.toString(this.copy.getCreditScore()));
        }

        { // approval
            final boolean approved = this.copy.isApproved();

            { // approved
                final RadioButton btn = (RadioButton)view.findViewById(R.id.btnApprove);

                if (approved) {
                    btn.setChecked(true);
                }

                btn.setOnClickListener(this);
            }

            { // rejected
                final RadioButton btn = (RadioButton)view.findViewById(R.id.btnReject);

                if (!approved) {
                    btn.setChecked(true);
                }

                btn.setOnClickListener(this);
            }
        }

        { // interest rate
            final TextView txtView = (TextView)view.findViewById(R.id.editRate);
            txtView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRate ) {
                    handleRateChanged(newRate.toString());
                }
            });
        }

        { // insurance cost
            final TextView textView = (TextView)view.findViewById(R.id.editInsuranceCost);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newInsuranceCost ) {
                    handleInsuranceCostChanged(newInsuranceCost.toString());
                }
            });
        }

        { // explanation
            final TextView textView = (TextView)view.findViewById(R.id.editExplanation);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newExplanation ) {
                    handleExplanationChanged(newExplanation.toString());
                }
            });
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
        updateState();
    }

    void setEvaluation( final Evaluation edit ) {
        this.original = edit;
        this.copy = Evaluation.copy(this.original);
    }

    //
    // private void updateError( final TextView textView,
    // final EvaluationError error ) {
    // final CharSequence current = textView.getError();
    //
    // if (error == null) {
    // if (!TextUtils.isEmpty(current)) {
    // textView.setError(null);
    // }
    // } else {
    // final String errorMsg = getActivity().getString(error.getMessageId());
    //
    // if (!errorMsg.equals(current)) {
    // textView.setError(errorMsg);
    // }
    // }
    // }

    private void updateState() {
        final Button btn = this.dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (btn != null) {
            final List<EvaluationError> errors = EvaluationValidator.isValid(this.copy);
            final boolean valid = errors.isEmpty();

            if (btn.isEnabled() != valid) {
                btn.setEnabled(valid);
            }
        }
    }

}
