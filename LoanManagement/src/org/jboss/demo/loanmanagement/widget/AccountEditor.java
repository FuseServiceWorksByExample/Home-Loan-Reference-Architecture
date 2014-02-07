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

import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.Address;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * An editor for an account.
 */
final class AccountEditor extends AssetEditor<Account> {

    private final Address address;
    private String number;

    private final Address originalAddress;
    private final String originalNumber;

    private EditText txtAddrLine1;
    private EditText txtAddrLine2;
    private EditText txtCity;
    private EditText txtCounty;
    private EditText txtNumber;
    private EditText txtState;
    private EditText txtZipcode;

    AccountEditor( final Context context,
                   final int titleId,
                   final Account account ) {
        super(context, titleId, account);

        this.originalNumber =
                        (((account == null) || (account.getNumber() == null)) ? Util.EMPTY_STRING : account.getNumber());
        this.number = this.originalNumber;

        this.originalAddress = ((account == null) ? null : account.getAddress());
        this.address = ((this.originalAddress == null) ? new Address() : this.originalAddress.copy());
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetEditor#doSetView()
     */
    @Override
    protected void doSetView() {
        final LayoutInflater inflator = LayoutInflater.from(getContext());
        final View view = inflator.inflate(R.layout.account, null);
        setView(view);
    }

    Address getAddress() {
        return this.address;
    }

    String getNumber() {
        return this.number;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetEditor#hasChanges()
     */
    @Override
    protected boolean hasChanges() {
        return (super.hasChanges() || !Util.equals(this.number, this.originalNumber) || !Util.equals(this.address,
                                                                                                     this.originalAddress));
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetEditor#hasErrors()
     */
    @Override
    protected boolean hasErrors() {
        return (super.hasErrors() || (this.txtNumber.getError() != null)
                        || (this.txtAddrLine1.getError() != null)
                        || (this.txtAddrLine2.getError() != null)
                        || (this.txtCity.getError() != null)
                        || (this.txtCounty.getError() != null)
                        || (this.txtState.getError() != null) || (this.txtZipcode.getError() != null));
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetEditor#show()
     */
    @Override
    public void show() {
        super.show();

        { // number
            this.txtNumber = (EditText)findViewById(R.id.txt_account_number);
            this.txtNumber.setText(this.number);
            addWatcher(this.txtNumber);
        }

        { // address line 1
            this.txtAddrLine1 = (EditText)findViewById(R.id.txt_addr_line_1);
            this.txtAddrLine1.setText(this.address.getLine1());
            addWatcher(this.txtAddrLine1);
        }

        { // address line 2
            this.txtAddrLine2 = (EditText)findViewById(R.id.txt_addr_line_2);
            this.txtAddrLine2.setText(this.address.getLine2());
            addWatcher(this.txtAddrLine2);
        }

        { // address city
            this.txtCity = (EditText)findViewById(R.id.txt_city);
            this.txtCity.setText(this.address.getCity());
            addWatcher(this.txtCity);
        }

        { // address state
            this.txtState = (EditText)findViewById(R.id.txt_state);
            this.txtState.setText(this.address.getState());
            addWatcher(this.txtState);
        }

        { // address zipcode
            this.txtZipcode = (EditText)findViewById(R.id.txt_zipcode);
            this.txtZipcode.setText(this.address.getPostalCode());
            addWatcher(this.txtZipcode);
        }

        { // address county
            this.txtCounty = (EditText)findViewById(R.id.txt_county);
            this.txtCounty.setText(this.address.getCounty());
            addWatcher(this.txtCounty);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetEditor#validate()
     */
    @Override
    protected void validate() {
        super.validate();

        { // number
            this.number = this.txtNumber.getText().toString();

            if (TextUtils.isEmpty(this.number)) {
                this.txtNumber.setError(getContext().getText(R.string.err_invalid_account_number));
            }
        }
    }

}
