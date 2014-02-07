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

import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Borrower;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;
import org.jboss.demo.loanmanagement.model.Declarations;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * An editor for an account.
 */
public final class BorrowerEditor extends AlertDialog {

    private final Borrower borrower;
    private Button btnOk;
    private DialogInterface.OnClickListener listener;
    private final Borrower original;

    private final TextWatcher txtWatcher = new TextWatcherAdapter() {

        /**
         * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#onTextChanged(java.lang.CharSequence, int, int,
         *      int)
         */
        @Override
        public void onTextChanged( final CharSequence newValue,
                                   final int start,
                                   final int before,
                                   final int count ) {
            validate();
        }
    };

    /**
     * @param context the app context (cannot be <code>null</code>)
     * @param titleId the title resource identifier
     * @param editBorrower the borrower (can be <code>null</code> when editor is used to create a borrower)
     */
    public BorrowerEditor( final Context context,
                           final int titleId,
                           final Borrower editBorrower ) {
        super(context);

        setTitle(titleId);
        setIcon(R.drawable.ic_home);
        setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getText(android.R.string.cancel),
                  (DialogInterface.OnClickListener)null);

        this.original = editBorrower;
        this.borrower = ((this.original == null) ? new Borrower() : this.original.copy());

        final LayoutInflater inflator = LayoutInflater.from(getContext());
        final View view = inflator.inflate(R.layout.borrower_editor, null);
        setView(view);
    }

    private void addWatcher( final EditText textView ) {
        textView.addTextChangedListener(this.txtWatcher);
    }

    protected BorrowerAddress getAddress() {
        final List<BorrowerAddress> addresses = this.borrower.getAddresses();

        if (addresses.isEmpty()) {
            final BorrowerAddress newAddress = new BorrowerAddress();
            addresses.add(newAddress);
        }

        return addresses.get(0); // TODO need to be able to handle multiple addresses
    }

    /**
     * @return the borrower being edited (never <code>null</code>);
     */
    public Borrower getBorrower() {
        return this.borrower;
    }

    /**
     * @return <code>true</code> if a new borrower is being created or changes have been made to an existing borrower
     */
    private boolean hasChanges() {
        return ((this.original == null) || !Util.equals(this.original, this.borrower));
    }

    /**
     * @return <code>true</code> if there are validation errors
     */
    private boolean hasErrors() {
        // TODO implement
        return false;
    }

    /**
     * Must be called before <code>show</code> is called.
     * 
     * @param okListener the listener receiving event when OK button is pushed (can be <code>null</code>)
     */
    public void setListener( final DialogInterface.OnClickListener okListener ) {
        this.listener = okListener;
    }

    /**
     * @see android.app.Dialog#show()
     */
    @Override
    public void show() {
        setButton(DialogInterface.BUTTON_POSITIVE, getContext().getText(android.R.string.ok), this.listener);
        super.show();

        this.btnOk = getButton(DialogInterface.BUTTON_POSITIVE);
        this.btnOk.setEnabled(false);

        { // type
            final String type = this.borrower.getType();

            if (Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX].equals(type)) {
                final RadioButton btnBorrower = (RadioButton)findViewById(R.id.btn_borrower);
                btnBorrower.setChecked(true);
            } else if (Borrower.BORROWER_TYPE[Borrower.CO_BORROWER_INDEX].equals(type)) {
                final RadioButton btnCoBorrower = (RadioButton)findViewById(R.id.btn_co_borrower);
                btnCoBorrower.setChecked(true);
            }

            final RadioGroup group = (RadioGroup)findViewById(R.id.grp_borrower_type);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                /**
                 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup,
                 *      int)
                 */
                @Override
                public void onCheckedChanged( final RadioGroup radioGroup,
                                              final int btnId ) {
                    String newType = null;

                    if (btnId == R.id.btn_borrower) {
                        newType = Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX];
                    } else if (btnId == R.id.btn_co_borrower) {
                        newType = Borrower.BORROWER_TYPE[Borrower.CO_BORROWER_INDEX];
                    }

                    assert !TextUtils.isEmpty(newType);
                    getBorrower().setType(newType);
                }
            });
        }

        { // title
            final EditText textView = (EditText)findViewById(R.id.txt_title);
            textView.setText(this.borrower.getTitle());
            addWatcher(textView);
        }

        { // first name
            final EditText textView = (EditText)findViewById(R.id.txt_first_name);
            textView.setText(this.borrower.getFirstName());
            addWatcher(textView);
        }

        { // middle name
            final EditText textView = (EditText)findViewById(R.id.txt_middle_name);
            textView.setText(this.borrower.getMiddleName());
            addWatcher(textView);
        }

        { // last name
            final EditText textView = (EditText)findViewById(R.id.txt_last_name);
            textView.setText(this.borrower.getLastName());
            addWatcher(textView);
        }

        { // SSN
            final EditText textView = (EditText)findViewById(R.id.txt_ssn);
            textView.setText(this.borrower.getSsn());
            addWatcher(textView);
        }

        { // DOB
            final EditText textView = (EditText)findViewById(R.id.txt_dob);
            textView.setText(this.borrower.getDob());
            addWatcher(textView);
        }

        { // marital status
            final Spinner cbx = (Spinner)findViewById(R.id.cbx_marital_status);
            final String maritalStatus = this.borrower.getMaritalStatus();
            Util.selectSpinnerItem(cbx, maritalStatus, Borrower.MARITAL_TYPE);

            cbx.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                }
            });
        }

        { // phone
            final EditText textView = (EditText)findViewById(R.id.txt_phone);
            textView.setText(this.borrower.getPhone());
            addWatcher(textView);
        }

        { // years in school
            final EditText textView = (EditText)findViewById(R.id.txt_years_in_school);
            textView.setText(Double.toString(this.borrower.getYearsSchool()));
            addWatcher(textView);
        }

        { // addresses
          // TODO need multiple address. make a tab for this and one for declarations
            final BorrowerAddress address = getAddress();

            { // line 1
                final TextView textView = (TextView)findViewById(R.id.txt_addr_line_1);
                textView.setText(address.getLine1());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newLine1 ) {
                        final String newValue = newLine1.toString();
                        address.setLine1(newValue);
                    }
                });
            }

            { // line 2
                final TextView textView = (TextView)findViewById(R.id.txt_addr_line_2);
                textView.setText(address.getLine2());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newLine2 ) {
                        final String newValue = newLine2.toString();
                        address.setLine2(newValue);
                    }
                });
            }

            { // city
                final TextView textView = (TextView)findViewById(R.id.txt_city);
                textView.setText(address.getCity());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newCity ) {
                        final String newValue = newCity.toString();
                        address.setCity(newValue);
                    }
                });
            }

            { // state
                final TextView textView = (TextView)findViewById(R.id.txt_state);
                textView.setText(address.getState());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newState ) {
                        final String newValue = newState.toString();
                        address.setState(newValue);
                    }
                });
            }

            { // zipcode
                final TextView textView = (TextView)findViewById(R.id.txt_zipcode);
                textView.setText(address.getPostalCode());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newZipcode ) {
                        final String newValue = newZipcode.toString();
                        address.setPostalCode(newValue);
                    }
                });
            }

            { // county
                final TextView textView = (TextView)findViewById(R.id.txt_county);
                textView.setText(getAddress().getCounty());
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newCounty ) {
                        final String newValue = newCounty.toString();
                        address.setCounty(newValue);
                    }
                });
            }

            { // declarations
                Declarations tempDeclarations = this.borrower.getDeclarations();

                if (tempDeclarations == null) {
                    tempDeclarations = new Declarations();
                    this.borrower.setDeclarations(tempDeclarations);
                }

                final Declarations declarations = tempDeclarations;

                { // any judgements
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_judgements);

                    if (declarations.isAnyJudgments()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setAnyJudgments(checked);
                        }
                    });
                }

                { // down payment
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_borrowed_down_payment);

                    if (declarations.isBorrowedDownPayment()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setBorrowedDownPayment(checked);
                        }
                    });
                }

                { // co-maker note
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_co_maker_note);

                    if (declarations.isCoMakerNote()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setCoMakerNote(checked);
                        }
                    });
                }

                { // bankruptcy
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_declared_bankruptcy);

                    if (declarations.isDeclaredBankrupt()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setDeclaredBankrupt(checked);
                        }
                    });
                }

                { // delinquent
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_delinquent);

                    if (declarations.isDelinquent()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setDelinquent(checked);
                        }
                    });
                }

                { // lawsuit
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_lawsuit);

                    if (declarations.isLawsuit()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setLawsuit(checked);
                        }
                    });
                }

                { // obligate on other loan
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_obligated_on_any_loan);

                    if (declarations.isObligatedOnAnyLoan()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setObligatedOnAnyLoan(checked);
                        }
                    });
                }

                { // obligated to pay alimony
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_obligated_to_pay_alimony);

                    if (declarations.isObligatedToPayAlimony()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setObligatedToPayAlimony(checked);
                        }
                    });
                }

                { // ownership interest
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_ownership_interest);

                    if (declarations.isOwnershipInterest()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setOwnershipInterest(checked);
                        }
                    });
                }

                { // permanent residence
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_permanent_resident);

                    if (declarations.isPermanentResident()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setPermanentResident(checked);
                        }
                    });
                }

                { // primary residence
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_primary_residence);

                    if (declarations.isPrimaryResidence()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setPrimaryResidence(checked);
                        }
                    });
                }

                { // foreclosed
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_property_foreclosed);

                    if (declarations.isPropertyForeclosed()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setPropertyForeclosed(checked);
                        }
                    });
                }

                { // U.S. citizen
                    final CheckBox chk = (CheckBox)findViewById(R.id.chk_us_citizen);

                    if (declarations.isUsCitizen()) {
                        chk.setChecked(true);
                    }

                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        /**
                         * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
                         *      boolean)
                         */
                        @Override
                        public void onCheckedChanged( final CompoundButton button,
                                                      final boolean checked ) {
                            declarations.setUsCitizen(checked);
                        }
                    });
                }

                { // title by
                    final Spinner cbx = (Spinner)findViewById(R.id.cbx_titled);
                    final String titledBy = declarations.getTitled();
                    Util.selectSpinnerItem(cbx, titledBy, Declarations.TITLED_BY_TYPES);

                    cbx.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                            declarations.setPropertyType(Declarations.PROPERTY_TYPES[index]);
                        }
                    });
                }
            }
        }
    }

    // TODO employment information
    // TODO dependent ages
    // TODO tabs for addresses, declarations, employment info, and general

    protected void validate() {
        // TODO implement validate
    }
}
