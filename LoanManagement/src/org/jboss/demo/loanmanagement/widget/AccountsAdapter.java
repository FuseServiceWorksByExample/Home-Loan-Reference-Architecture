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
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

final class AccountsAdapter implements AssetsAndLiabilitiesAdapter {

    private static final String TAB_ID = "tab_accounts"; //$NON-NLS-1$

    static void doEditAccount( final AccountEditor editor,
                               final Account editAccount ) {
        try {
            editAccount.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
        }

        editAccount.setDescription(editor.getDescription());
        editAccount.setNumber(editor.getNumber());
        editAccount.setAddress(editor.getAddress());
    }

    private ViewGroup accountsContainer;
    private AssetsAndLiabilities assets;
    private Context context;
    private LayoutInflater inflater;
    private boolean setup = false;

    AssetsAndLiabilities accessAssets() {
        return this.assets;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#createTab(android.widget.TabHost)
     */
    @Override
    public TabSpec createTab( final TabHost tabPanel ) {
        final TabSpec tab = tabPanel.newTabSpec(TAB_ID);
        tab.setContent(R.id.tab_accounts);
        tab.setIndicator(this.context.getString(R.string.Accounts));
        return tab;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#getTabId()
     */
    @Override
    public String getTabId() {
        return TAB_ID;
    }

    void handleAddAccount() {
        final AccountEditor editor = new AccountEditor(this.context, R.string.add_account_dialog_title, null);

        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final Account newAccount = new Account();

                try {
                    newAccount.setAmount(Util.parseDouble(editor.getAmount()));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    newAccount.setAmount(0);
                }

                newAccount.setDescription(editor.getDescription());
                newAccount.setNumber(editor.getNumber());
                newAccount.setAddress(editor.getAddress());

                accessAssets().addAccount(newAccount);
            }
        });

        editor.show();
    }

    void handleDeleteAccount( final Account deleteAccount ) {
        String msg = null;

        if (Util.isBlank(deleteAccount.getNumber())) {
            msg = this.context.getString(R.string.delete_account_msg);
        } else {
            msg = this.context.getString(R.string.delete_account_with_id_msg, deleteAccount.getNumber());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       accessAssets().removeAccount(deleteAccount);
                   }
               }).show();
    }

    void handleEditAccount( final Account editAccount ) {
        final AccountEditor editor = new AccountEditor(this.context, R.string.edit_account_dialog_title, editAccount);

        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doEditAccount(editor, editAccount);
            }
        });

        editor.show();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#handleTabSelected(android.view.ViewGroup)
     */
    @Override
    public void handleTabSelected( final ViewGroup viewGroup ) {
        if (!this.setup) {
            final ViewGroup accountsTab = (ViewGroup)viewGroup.findViewById(R.id.tab_accounts);
            this.accountsContainer = (ViewGroup)accountsTab.findViewById(R.id.accounts_container);

            { // add account
                final ImageButton btn = (ImageButton)viewGroup.findViewById(R.id.btn_add_account);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View view ) {
                        handleAddAccount();
                    }
                });
            }

            refresh();
            this.setup = true;
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#refresh()
     */
    @Override
    public void refresh() {
        if (this.accountsContainer == null) {
            return;
        }

        this.accountsContainer.removeAllViews();

        for (final Account account : this.assets.getAccounts()) {
            final View view = this.inflater.inflate(R.layout.account_item, null);
            this.accountsContainer.addView(view);

            { // number
                final TextView txt = (TextView)view.findViewById(R.id.txt_account_number);
                txt.setText(account.getNumber());
            }

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(account.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);

                try {
                    txt.setText(Util.formatMoneyAmount(Double.toString(account.getAmount())));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    txt.setError(this.context.getText(R.string.err_invalid_amount));
                }
            }

            { // edit account
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditAccount(account);
                    }
                });
            }

            { // delete account
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteAccount(account);
                    }
                });
            }
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setContext(android.content.Context)
     */
    @Override
    public void setContext( final Context appContext ) {
        this.context = appContext;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setInflater(android.view.LayoutInflater)
     */
    @Override
    public void setInflater( final LayoutInflater layoutInflater ) {
        this.inflater = layoutInflater;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setModel(org.jboss.demo.loanmanagement.model.AssetsAndLiabilities)
     */
    @Override
    public void setModel( final AssetsAndLiabilities model ) {
        this.assets = model;
    }

}
