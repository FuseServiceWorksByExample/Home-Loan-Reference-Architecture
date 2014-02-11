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
package org.jboss.demo.loanmanagement.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;
import org.jboss.demo.loanmanagement.validate.BorrowerAddressValidator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * An editor for a borrower address.
 */
class BorrowerAddressEditor extends AlertDialog {

    private final BorrowerAddress address;
    private Button btnOk;
    private DialogInterface.OnClickListener listener;
    private final BorrowerAddress original;
    private final PropertyChangeListener propListener;

    BorrowerAddressEditor( final Context context,
                           final int titleId,
                           final BorrowerAddress borrowerAddress ) {
        super(context);

        setTitle(titleId);
        setIcon(R.drawable.ic_home);
        setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getText(android.R.string.cancel),
                  (DialogInterface.OnClickListener)null);

        this.original = borrowerAddress;
        this.address = ((borrowerAddress == null) ? new BorrowerAddress() : borrowerAddress);
        this.propListener = new PropertyChangeListener() {

            /**
             * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
             */
            @Override
            public void propertyChange( final PropertyChangeEvent newEvent ) {
                validate();

            }
        };
        this.address.add(this.propListener);

        doSetView();
    }

    protected void doSetView() {
        final LayoutInflater inflator = LayoutInflater.from(getContext());
        final View view = inflator.inflate(R.layout.borrower_address, null);
        setView(view);
    }

    BorrowerAddress getAddress() {
        return this.address;
    }

    /**
     * @return <code>true</code> if a new asset is being created or changes have been made to an existing asset
     */
    protected boolean hasChanges() {
        return ((this.original == null) || !Util.equals(this.address, this.original));
    }

    /**
     * @return <code>true</code> if there are validation errors
     */
    protected boolean hasErrors() {
        return !BorrowerAddressValidator.SHARED.validate(this.address).isEmpty();
    }

    /**
     * Must be called before <code>show</code> is called.
     * 
     * @param okListener the listener receiving event when OK button is pushed (can be <code>null</code>)
     */
    void setListener( final DialogInterface.OnClickListener okListener ) {
        this.listener = okListener;
    }

    /**
     * @see android.app.Dialog#show()
     */
    @Override
    public void show() {
        setButton(DialogInterface.BUTTON_POSITIVE, getContext().getText(android.R.string.ok), this.listener);
        super.show();

        { // number of years
            final EditText txt = (EditText)findViewById(R.id.txt_num_years);
            txt.setText(Double.toString(getAddress().getNumYears()));
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#onTextChanged(java.lang.CharSequence,
                 *      int, int, int)
                 */
                @Override
                public void onTextChanged( final CharSequence newNumYears,
                                           final int start,
                                           final int before,
                                           final int count ) {
                    try {
                        getAddress().setNumYears(Util.parseDouble(newNumYears.toString()));
                    } catch (final ParseException e) {
                        Log.e(BorrowerAddressEditor.class.getSimpleName(),
                              getContext().getString(R.string.err_invalid_number_of_years, newNumYears), e);
                    }

                    validate();
                }
            });
        }

        { // type
            final Spinner cbxAddressType = (Spinner)findViewById(R.id.cbx_address_type);
            final String type = getAddress().getType();
            Util.selectSpinnerItem(cbxAddressType, type, BorrowerAddress.ADDRESS_TYPES);

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
                    getAddress().setType(BorrowerAddress.ADDRESS_TYPES[index]);
                    validate();
                }
            });
        }

        { // address line 1
            final EditText txt = (EditText)findViewById(R.id.txt_addr_line_1);
            txt.setText(this.address.getLine1());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newLine1 ) {
                    getAddress().setLine1(newLine1.toString());
                }
            });
        }

        { // address line 2
            final EditText txt = (EditText)findViewById(R.id.txt_addr_line_2);
            txt.setText(this.address.getLine2());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newLine2 ) {
                    getAddress().setLine2(newLine2.toString());
                }
            });
        }

        { // address city
            final EditText txt = (EditText)findViewById(R.id.txt_city);
            txt.setText(this.address.getCity());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newCity ) {
                    getAddress().setCity(newCity.toString());
                }
            });
        }

        { // address state
            final EditText txt = (EditText)findViewById(R.id.txt_state);
            txt.setText(this.address.getState());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newState ) {
                    getAddress().setState(newState.toString());
                }
            });
        }

        { // address zipcode
            final EditText txt = (EditText)findViewById(R.id.txt_zipcode);
            txt.setText(this.address.getPostalCode());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newZip ) {
                    getAddress().setPostalCode(newZip.toString());
                }
            });
        }

        { // address county
            final EditText txt = (EditText)findViewById(R.id.txt_county);
            txt.setText(this.address.getCounty());
            txt.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newCounty ) {
                    getAddress().setCounty(newCounty.toString());
                }
            });
        }

        this.btnOk = getButton(DialogInterface.BUTTON_POSITIVE);
        this.btnOk.setEnabled(false);

        validate();
    }

    protected void validate() {
        final boolean enable = (!hasErrors() && hasChanges());

        if (this.btnOk.isEnabled() != enable) {
            this.btnOk.setEnabled(enable);
        }
    }

}
