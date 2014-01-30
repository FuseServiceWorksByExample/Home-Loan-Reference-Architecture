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

import java.text.ParseException;
import org.jboss.demo.loanmanagement.model.Borrower;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;
import org.jboss.demo.loanmanagement.widget.ItemSelectedAdapter;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The UI for editing a borrower.
 */
public final class BorrowerDialog extends DialogFragment implements
                                                        DialogInterface.OnClickListener,
                                                        DialogInterface.OnShowListener,
                                                        View.OnClickListener {

    private Borrower copy;
    private AlertDialog dialog;
    private Borrower original;

    /**
     * Edits a new borrower.
     */
    public BorrowerDialog() {
        this.original = null;
        this.copy = new Borrower();
    }

    /**
     * @return the borrower's address
     */
    protected BorrowerAddress getAddress() {
        BorrowerAddress address = null;

        if (this.copy.getAddresses().isEmpty()) {
            address = new BorrowerAddress();
            this.copy.addAddress(address);
        } else {
            address = this.copy.getAddresses().get(0);
        }

        return address;
    }

    /**
     * @return the borrower represented by the dialog
     */
    public Borrower getBorrower() {
        return this.copy;
    }

    private void handleBorrowerTypeSelected( final int index ) {
        this.copy.setType(Borrower.BORROWER_TYPE[index]);
        updateState();
    }

    private void handleSsnChanged( final String newSsn ) {
        this.copy.setSsn(newSsn);
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
        if (R.id.btn_borrower == view.getId()) {
            handleBorrowerTypeSelected(Borrower.BORROWER_INDEX);
        } else if (R.id.btn_co_borrower == view.getId()) {
            handleBorrowerTypeSelected(Borrower.CO_BORROWER_INDEX);
        }

        updateState();
    }

    /**
     * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
     */
    @Override
    public Dialog onCreateDialog( final Bundle savedInstanceState ) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.borrower_dialog, null);

        { // borrower type
            final RadioButton btnBorrower = (RadioButton)view.findViewById(R.id.btn_borrower);
            final RadioButton btnCoBorrower = (RadioButton)view.findViewById(R.id.btn_co_borrower);
            final String type = this.copy.getType();

            if (!TextUtils.isEmpty(type)) {
                if (Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX].equals(type)) {
                    btnBorrower.setChecked(true);
                } else {
                    btnCoBorrower.setChecked(true);
                }
            }

            btnBorrower.setOnClickListener(this);
            btnCoBorrower.setOnClickListener(this);

            // TODO finish
            updateState();
        }

        { // title
            final TextView textView = (TextView)view.findViewById(R.id.txt_title);

            if (!TextUtils.isEmpty(this.copy.getTitle())) {
                textView.setText(this.copy.getTitle());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newTitle ) {
                    getBorrower().setTitle(newTitle.toString());
                    updateState();
                }
            });
        }

        { // first name
            final TextView textView = (TextView)view.findViewById(R.id.txt_first_name);

            if (!TextUtils.isEmpty(this.copy.getFirstName())) {
                textView.setText(this.copy.getFirstName());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newFirstName ) {
                    getBorrower().setFirstName(newFirstName.toString());
                    updateState();
                }
            });
        }

        { // middle name
            final TextView textView = (TextView)view.findViewById(R.id.txt_middle_name);

            if (!TextUtils.isEmpty(this.copy.getMiddleName())) {
                textView.setText(this.copy.getMiddleName());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newMiddleName ) {
                    getBorrower().setMiddleName(newMiddleName.toString());
                    updateState();
                }
            });
        }

        { // last name
            final TextView textView = (TextView)view.findViewById(R.id.txt_last_name);

            if (!TextUtils.isEmpty(this.copy.getLastName())) {
                textView.setText(this.copy.getLastName());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newLastName ) {
                    getBorrower().setLastName(newLastName.toString());
                    updateState();
                }
            });
        }

        { // ssn
            final TextView txtView = (TextView)view.findViewById(R.id.txt_ssn);
            final String ssn = this.copy.getSsn();

            if (!TextUtils.isEmpty(ssn)) {
                txtView.setText(Util.formatSsnWithMask(Integer.parseInt(ssn)));
            }
        }

        { // dob
            final TextView textView = (TextView)view.findViewById(R.id.txt_dob);

            if (!TextUtils.isEmpty(this.copy.getDob())) {
                textView.setText(this.copy.getDob());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newDob ) {
                    getBorrower().setDob(newDob.toString());
                    updateState();
                }
            });
        }

        { // marital status
            final Spinner cbxMaritalStatus = (Spinner)view.findViewById(R.id.cbx_marital_status);
            final String maritalStatus = this.copy.getMaritalStatus();

            if (!TextUtils.isEmpty(maritalStatus)) {
                int index = -1;
                int i = 0;

                for (final String status : Borrower.MARITAL_TYPE) {
                    if (status.equals(maritalStatus)) {
                        index = i;
                        break;
                    }

                    ++i;
                }

                if (index != -1) {
                    cbxMaritalStatus.setSelection(index);
                }
            }

            cbxMaritalStatus.setOnItemSelectedListener(new ItemSelectedAdapter() {

                /**
                 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
                 *      android.view.View, int, long)
                 */
                @Override
                public void onItemSelected( final AdapterView<?> spinner,
                                            final View textView,
                                            final int position,
                                            final long id ) {
                    final int index = ((Spinner)spinner).getSelectedItemPosition();
                    getBorrower().setMaritalStatus(Borrower.MARITAL_TYPE[index]);
                    updateState();
                }
            });
        }

        { // phone
            final TextView textView = (TextView)view.findViewById(R.id.txt_phone);

            if (!TextUtils.isEmpty(this.copy.getPhone())) {
                textView.setText(this.copy.getPhone());
            }

            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newPhone ) {
                    getBorrower().setPhone(newPhone.toString());
                    updateState();
                }
            });
        }

        { // years in school
            final TextView textView = (TextView)view.findViewById(R.id.txt_years_in_school);
            textView.setText(Double.toString(this.copy.getYearsSchool()));
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newYearsInSchool ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newYearsInSchool.toString());
                        getBorrower().setYearsSchool(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getString(R.string.err_invalid_amount));
                    }

                    updateState();
                }
            });
        }

        { // address
            { // address type
                final Spinner cbxAddressType = (Spinner)view.findViewById(R.id.cbx_address_type);
                final String addressType = getAddress().getType();

                if (!TextUtils.isEmpty(addressType)) {
                    int index = -1;
                    int i = 0;

                    for (final String type : BorrowerAddress.ADDRESS_TYPES) {
                        if (type.equals(addressType)) {
                            index = i;
                            break;
                        }

                        ++i;
                    }

                    if (index != -1) {
                        cbxAddressType.setSelection(index);
                    }
                }

                cbxAddressType.setOnItemSelectedListener(new ItemSelectedAdapter() {

                    /**
                     * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
                     *      android.view.View, int, long)
                     */
                    @Override
                    public void onItemSelected( final AdapterView<?> spinner,
                                                final View textView,
                                                final int position,
                                                final long id ) {
                        final int index = ((Spinner)spinner).getSelectedItemPosition();
                        getBorrower().setType(BorrowerAddress.ADDRESS_TYPES[index]);
                        updateState();
                    }
                });
            }

            { // number of years
                final TextView txtNumYears = (TextView)view.findViewById(R.id.txt_num_years);
                txtNumYears.setText(Double.toString(getAddress().getNumYears()));
                txtNumYears.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newNumYears ) {
                        double newValue;

                        try {
                            newValue = Util.parseDouble(newNumYears.toString());
                            getAddress().setNumYears(newValue);
                        } catch (final ParseException e) {
                            txtNumYears.setError(getString(R.string.err_invalid_amount));
                        }
                        updateState();
                    }
                });
            }

            { // line 1
                final TextView txtView = (TextView)view.findViewById(R.id.txt_addr_line_1);

                if (!TextUtils.isEmpty(getAddress().getLine1())) {
                    txtView.setText(getAddress().getLine1());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newLine1 ) {
                        getAddress().setLine1(newLine1.toString());
                        updateState();
                    }
                });
            }

            { // line 2
                final TextView txtView = (TextView)view.findViewById(R.id.txt_addr_line_2);

                if (!TextUtils.isEmpty(getAddress().getLine2())) {
                    txtView.setText(getAddress().getLine2());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newLine2 ) {
                        getAddress().setLine2(newLine2.toString());
                        updateState();
                    }
                });
            }

            { // city
                final TextView txtView = (TextView)view.findViewById(R.id.txt_city);

                if (!TextUtils.isEmpty(getAddress().getCity())) {
                    txtView.setText(getAddress().getCity());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newCity ) {
                        getAddress().setCity(newCity.toString());
                        updateState();
                    }
                });
            }

            { // state
                final TextView txtView = (TextView)view.findViewById(R.id.txt_state);

                if (!TextUtils.isEmpty(getAddress().getState())) {
                    txtView.setText(getAddress().getState());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newState ) {
                        getAddress().setState(newState.toString());
                        updateState();
                    }
                });
            }

            { // zipcode
                final TextView txtView = (TextView)view.findViewById(R.id.txt_zipcode);

                if (!TextUtils.isEmpty(getAddress().getPostalCode())) {
                    txtView.setText(getAddress().getPostalCode());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newZipcode ) {
                        getAddress().setPostalCode(newZipcode.toString());
                        updateState();
                    }
                });
            }

            { // county
                final TextView txtView = (TextView)view.findViewById(R.id.txt_county);

                if (!TextUtils.isEmpty(getAddress().getCounty())) {
                    txtView.setText(getAddress().getCounty());
                }

                txtView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newCounty ) {
                        getAddress().setCounty(newCounty.toString());
                        updateState();
                    }
                });
            }
        }

        // TODO declarations

        // TODO dependent ages

        // TODO employment

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.dialog =
                        builder.setView(view).setTitle(R.string.borrower_dialog_title).setIcon(R.drawable.ic_home)
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

    /**
     * Call before dialog is shown.
     * 
     * @param edit the borrower being edited (can be <code>null</code> when editing a new borrower)
     */
    public void setBorrower( final Borrower edit ) {
        this.original = edit;

        if (edit == null) {
            this.copy = new Borrower();
        } else {
            this.copy = Borrower.copy(this.original);
        }
    }

    protected void updateState() {
        final Button btn = this.dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        if (btn != null) {
            // TODO need loan application validator
            // final List<EvaluationError> errors = EvaluationValidator.isValid(this.copy);
            // final boolean valid = errors.isEmpty();
            //
            // if (btn.isEnabled() != valid) {
            // btn.setEnabled(valid);
            // }
        }
    }

}
