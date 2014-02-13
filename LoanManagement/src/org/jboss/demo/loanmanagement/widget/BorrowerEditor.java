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
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Borrower;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;
import org.jboss.demo.loanmanagement.model.Declarations;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * An editor for an account.
 */
public final class BorrowerEditor extends AlertDialog implements PropertyChangeListener {

    private static final String ADDRESSES_TAB_ID = "tab_addresses"; //$NON-NLS-1$
    private static final String DECLARATIONS_TAB_ID = "tab_declarations"; //$NON-NLS-1$
    private static final String DETAILS_TAB_ID = "tab_details"; //$NON-NLS-1$

    private ViewGroup addressContainer;
    private boolean addressesTabSetup;
    private final Borrower borrower;
    private Button btnOk;
    private boolean declarationsTabSetup;
    private boolean detailsTabSetup;
    private final LayoutInflater inflator;
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

        this.inflator = LayoutInflater.from(getContext());
        this.original = editBorrower;
        this.borrower = ((this.original == null) ? new Borrower() : this.original.copy());
        this.borrower.add(this);

        final View view = this.inflator.inflate(R.layout.borrower_editor, null);
        setView(view);
    }

    private void addWatcher( final EditText textView ) {
        textView.addTextChangedListener(this.txtWatcher);
    }

    /**
     * @return the borrower being edited (never <code>null</code>);
     */
    public Borrower getBorrower() {
        return this.borrower;
    }

    protected void handleAddAddress() {
        final BorrowerAddressEditor editor =
                        new BorrowerAddressEditor(getContext(), R.string.add_address_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final BorrowerAddress newAddress = editor.getAddress();
                getBorrower().addAddress(newAddress);
            }
        });
        editor.show();
    }

    protected void handleDeleteAddress( final BorrowerAddress deleteAddress ) {
        String msg = null;

        if (Util.isBlank(deleteAddress.getLine1())) {
            msg = getContext().getString(R.string.delete_address_msg);
        } else {
            msg = getContext().getString(R.string.delete_address__with_line1_msg, deleteAddress.getLine1());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       getBorrower().removeAddress(deleteAddress);
                   }
               }).show();
    }

    protected void handleEditAddress( final BorrowerAddress editAddress ) {
        final BorrowerAddressEditor editor =
                        new BorrowerAddressEditor(getContext(), R.string.edit_address_dialog_title, editAddress);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final BorrowerAddress newAddress = editor.getAddress();
                editAddress.update(newAddress);
            }
        });
        editor.show();
    }

    protected void handleTabChanged( final String tabId,
                                     final View tabHost ) {
        if (DETAILS_TAB_ID.equals(tabId) && !this.detailsTabSetup) {
            setupDetailsTab();
            this.detailsTabSetup = true;
        } else if (ADDRESSES_TAB_ID.equals(tabId) && !this.addressesTabSetup) {
            final ViewGroup addressTab = (ViewGroup)tabHost.findViewById(R.id.borrower_addresses_tab);
            this.addressContainer = (ViewGroup)addressTab.findViewById(R.id.address_container);

            { // add address
                final ImageButton btn = (ImageButton)addressTab.findViewById(R.id.btn_add_address);
                btn.setOnClickListener(new View.OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View view ) {
                        handleAddAddress();
                    }
                });
            }

            refreshAddresses();
            this.addressesTabSetup = true;
        } else if (DECLARATIONS_TAB_ID.equals(tabId) && !this.declarationsTabSetup) {
            setupDeclarationsTab();
            this.declarationsTabSetup = true;
        } else {
            assert false;
        }
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
     * @see android.app.AlertDialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate( final Bundle bundle ) {
        super.onCreate(bundle);

        final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();

        { // general tab
            final TabSpec tab = tabHost.newTabSpec(DETAILS_TAB_ID);
            tab.setContent(R.id.borrower_details_tab);
            tab.setIndicator(getContext().getString(R.string.Details));
            tabHost.addTab(tab);
        }

        { // address tab
            final TabSpec tab = tabHost.newTabSpec(ADDRESSES_TAB_ID);
            tab.setContent(R.id.borrower_addresses_tab);
            tab.setIndicator(getContext().getString(R.string.Addresses));
            tabHost.addTab(tab);
        }

        { // declarations tab
            final TabSpec tab = tabHost.newTabSpec(DECLARATIONS_TAB_ID);
            tab.setContent(R.id.borrower_declarations_tab);
            tab.setIndicator(getContext().getString(R.string.Declarations));
            tabHost.addTab(tab);
        }

        // call this manually since no event is generated when tabs first displayed
        handleTabChanged(DETAILS_TAB_ID, tabHost);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        refreshAddresses();
    }

    private void refreshAddresses() {
        if (this.addressContainer == null) {
            return;
        }

        this.addressContainer.removeAllViews();

        for (final BorrowerAddress address : this.borrower.getAddresses()) {
            final View view = this.inflator.inflate(R.layout.address_item, null);
            this.addressContainer.addView(view);

            { // line 1
                final TextView txt = (TextView)view.findViewById(R.id.lbl_addr_line_1);

                if (Util.isBlank(address.getLine1())) {
                    txt.setText(getContext().getString(R.string.empty_addr_line1));
                } else {
                    txt.setText(address.getLine1());
                }
            }

            { // line 2
                final StringBuilder line2 = new StringBuilder();

                if (!Util.isBlank(address.getCity())) {
                    line2.append(address.getCity());
                    line2.append(',');
                }

                if (!Util.isBlank(address.getCity())) {
                    line2.append(address.getCity());
                }

                if (!Util.isBlank(address.getState())) {
                    if (line2.length() != 0) {
                        line2.append(", "); //$NON-NLS-1$
                    }

                    line2.append(address.getState());
                }

                if (!Util.isBlank(address.getPostalCode())) {
                    if (line2.length() != 0) {
                        line2.append("  "); //$NON-NLS-1$
                    }

                    line2.append(address.getPostalCode());
                }

                final TextView txt = (TextView)view.findViewById(R.id.lbl_addr_line_2);
                txt.setText(line2.toString());
            }

            { // edit address
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new View.OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditAddress(address);
                    }
                });
            }

            { // delete address
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new View.OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteAddress(address);
                    }
                });
            }
        }
    }

    /**
     * Must be called before <code>show</code> is called.
     * 
     * @param okListener the listener receiving event when OK button is pushed (can be <code>null</code>)
     */
    public void setListener( final DialogInterface.OnClickListener okListener ) {
        this.listener = okListener;
    }

    void setupDeclarationsTab() {
        Declarations tempDeclarations = this.borrower.getDeclarations();

        if (tempDeclarations == null) {
            tempDeclarations = new Declarations();
            this.borrower.setDeclarations(tempDeclarations);
        }

        final Declarations declarations = tempDeclarations;

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
    }

    void setupDetailsTab() {
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

                    assert !Util.isBlank(newType);
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
    }

    // TODO employment information tab
    // TODO dependent ages

    /**
     * @see android.app.Dialog#show()
     */
    @Override
    public void show() {
        setButton(DialogInterface.BUTTON_POSITIVE, getContext().getText(android.R.string.ok), this.listener);
        super.show();

        this.btnOk = getButton(DialogInterface.BUTTON_POSITIVE);
        this.btnOk.setEnabled(false);

        final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            /**
             * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
             */
            @Override
            public void onTabChanged( final String tabId ) {
                handleTabChanged(tabId, tabHost);
            }
        });
    }

    protected void validate() {
        // TODO implement validate
        this.btnOk.setEnabled(true);
    }

}
