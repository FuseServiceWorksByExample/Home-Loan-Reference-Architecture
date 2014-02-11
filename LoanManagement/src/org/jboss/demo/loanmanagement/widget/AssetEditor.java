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

import java.text.ParseException;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Asset;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * An editor for an asset.
 * 
 * @param <T> the type of asset
 */
class AssetEditor<T extends Asset<T>> extends AlertDialog {

    private String amount;
    private Button btnOk;
    private String description;
    private DialogInterface.OnClickListener listener;
    private final String originalAmount;
    private final T originalAsset;
    private final String originalDescription;
    private EditText txtAmount;
    private EditText txtDescription;

    private final TextWatcher txtWatcher = new TextWatcherAdapter() {

        /**
         * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#onTextChanged(java.lang.CharSequence, int, int,
         *      int)
         */
        @Override
        public void onTextChanged( final CharSequence newDescription,
                                   final int start,
                                   final int before,
                                   final int count ) {
            validate();
        }
    };

    AssetEditor( final Context context,
                 final int titleId,
                 final T asset ) {
        super(context);

        setTitle(titleId);
        setIcon(R.drawable.ic_home);
        setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getText(android.R.string.cancel),
                  (DialogInterface.OnClickListener)null);

        this.originalAsset = asset;
        this.originalDescription =
                        (((asset == null) || (asset.getDescription() == null)) ? Util.EMPTY_STRING
                                        : asset.getDescription());
        this.description = this.originalDescription;
        this.originalAmount = ((asset == null) ? "0.00" : Util.formatMoneyAmount(asset.getAmount())); //$NON-NLS-1$
        this.amount = this.originalAmount;

        doSetView();
    }

    protected void addWatcher( final EditText textView ) {
        textView.addTextChangedListener(this.txtWatcher);
    }

    protected void doSetView() {
        final LayoutInflater inflator = LayoutInflater.from(getContext());
        final View view = inflator.inflate(R.layout.asset, null);
        setView(view);
    }

    String getAmount() {
        try {
            return Util.formatMoneyAmount(this.amount);
        } catch (final ParseException e) {
            return null;
        }
    }

    String getDescription() {
        return this.description;
    }

    /**
     * @return <code>true</code> if a new asset is being created or changes have been made to an existing asset
     */
    protected boolean hasChanges() {
        return ((this.originalAsset == null) || !Util.equals(this.description, this.originalDescription) || !Util.equals(this.amount,
                                                                                                                         this.originalAmount));
    }

    /**
     * @return <code>true</code> if there are validation errors
     */
    protected boolean hasErrors() {
        return ((this.txtAmount.getError() != null) || (this.txtDescription.getError() != null));
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

        { // amount
            this.txtAmount = (EditText)findViewById(R.id.txt_asset_amount);
            this.txtAmount.setText(this.amount);
            this.txtAmount.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            addWatcher(this.txtAmount);
        }

        { // description
            this.txtDescription = (EditText)findViewById(R.id.txt_asset_description);
            this.txtDescription.setText(this.description);
            addWatcher(this.txtDescription);
        }

        this.btnOk = getButton(DialogInterface.BUTTON_POSITIVE);
        this.btnOk.setEnabled(false);
    }

    protected void validate() {
        { // amount
            this.amount = this.txtAmount.getText().toString();

            if (Util.isBlank(this.amount)) {
                this.txtAmount.setError(getContext().getText(R.string.err_empty_amount));
            }
        }

        { // description
            this.description = this.txtDescription.getText().toString();

            if (Util.isBlank(this.description)) {
                this.txtDescription.setError(getContext().getText(R.string.err_invalid_description));
            }
        }

        final boolean enable = (!hasErrors() && hasChanges());

        if (this.btnOk.isEnabled() != enable) {
            this.btnOk.setEnabled(enable);
        }
    }

}
