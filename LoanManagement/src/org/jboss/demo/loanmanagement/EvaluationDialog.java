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

import java.util.List;
import org.jboss.demo.loanmanagement.model.Evaluation;
import org.jboss.demo.loanmanagement.validate.EvaluationValidator;
import org.jboss.demo.loanmanagement.validate.ValidationError;
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
                                                          DialogInterface.OnShowListener,
                                                          View.OnClickListener {

    private Evaluation copy;
    private AlertDialog dialog;
    private TextView lblInsuraceCost;
    private TextView lblRate;
    private DialogInterface.OnClickListener listener;
    private Evaluation original;
    private TextView txtInsuraceCost;
    private TextView txtRate;

    /**
     * @return the evaluation represented by the dialog
     */
    public Evaluation getEvaluation() {
        this.original.update(this.copy);
        return this.original;
    }

    private void handleApproveSelected() {
        this.copy.setApproved(true);

        this.lblRate.setVisibility(View.VISIBLE);
        this.txtRate.setText(Double.toString(this.copy.getInsuranceCost()));
        this.txtRate.setVisibility(View.VISIBLE);

        this.lblInsuraceCost.setVisibility(View.VISIBLE);
        this.txtInsuraceCost.setText(Double.toString(this.copy.getInsuranceCost()));
        this.txtInsuraceCost.setVisibility(View.VISIBLE);

        updateState();
    }

    protected void handleExplanationChanged( final String newExplanation ) {
        this.copy.setExplanation(newExplanation);
        updateState();
    }

    protected void handleInsuranceCostChanged( final String newInsuranceCost ) {
        if (TextUtils.isEmpty(newInsuranceCost)) {
            this.copy.setInsuranceCost(0);
        } else {
            this.copy.setInsuranceCost(Double.parseDouble(newInsuranceCost));
        }

        updateState();
    }

    protected void handleRateChanged( final String newRate ) {
        if (TextUtils.isEmpty(newRate)) {
            this.copy.setRate(0);
        } else {
            this.copy.setRate(Double.parseDouble(newRate));
        }

        updateState();
    }

    private void handleRejectSelected() {
        this.copy.setApproved(false);

        this.lblRate.setVisibility(View.INVISIBLE);
        this.txtRate.setVisibility(View.INVISIBLE);
        this.lblInsuraceCost.setVisibility(View.INVISIBLE);
        this.txtInsuraceCost.setVisibility(View.INVISIBLE);
        this.txtInsuraceCost.getParent().requestLayout();

        updateState();
    }

    /**
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick( final View view ) {
        if (R.id.btnApprove == view.getId()) {
            handleApproveSelected();
        } else if (R.id.btnReject == view.getId()) {
            handleRejectSelected();
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
            txtView.setText(Util.formatSsnWithMask(this.copy.getSsn()));
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
            this.lblRate = (TextView)view.findViewById(R.id.lblRate);
            this.txtRate = (TextView)view.findViewById(R.id.editRate);
            this.txtRate.addTextChangedListener(new TextWatcherAdapter() {

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
            this.lblInsuraceCost = (TextView)view.findViewById(R.id.lblInsuranceCost);
            this.txtInsuraceCost = (TextView)view.findViewById(R.id.editInsuranceCost);
            this.txtInsuraceCost.addTextChangedListener(new TextWatcherAdapter() {

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
                               .setPositiveButton(android.R.string.ok, this.listener).create();
        this.dialog.setOnShowListener(this);

        return this.dialog;
    }

    /**
     * @see android.content.DialogInterface.OnShowListener#onShow(android.content.DialogInterface)
     */
    @Override
    public void onShow( final DialogInterface dialogInterface ) {
        if (this.copy.isApproved()) {
            handleApproveSelected();
        } else {
            handleRejectSelected();
        }

        updateState();
    }

    void setEvaluation( final Evaluation edit ) {
        this.original = edit;
        this.copy = this.original.copy();
    }

    void setOkListener( final DialogInterface.OnClickListener okListener ) {
        this.listener = okListener;
    }

    private void updateState() {
        final Button btn = this.dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (btn != null) {
            final List<ValidationError> errors = EvaluationValidator.SHARED.validate(this.copy);
            final boolean valid = errors.isEmpty();

            if (btn.isEnabled() != valid) {
                btn.setEnabled(valid);
            }
        }
    }

}
