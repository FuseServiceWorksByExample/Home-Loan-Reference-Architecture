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
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.CashDeposit;
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

final class DepositsAdapter implements AssetsAndLiabilitiesAdapter {

    private static final String TAB_ID = "tab_deposits"; //$NON-NLS-1$

    static void doEditDeposit( final AssetEditor<CashDeposit> editor,
                               final CashDeposit editDeposit ) {
        try {
            editDeposit.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
        }

        editDeposit.setDescription(editor.getDescription());
    }

    private AssetsAndLiabilities assets;
    private Context context;
    private ViewGroup depositsContainer;
    private LayoutInflater inflater;
    private boolean setup = false;

    AssetsAndLiabilities accessAssets() {
        return this.assets;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#createTab(android.widget.TabHost)
     */
    @Override
    public TabSpec createTab( final TabHost tabHost ) {
        final TabSpec tab = tabHost.newTabSpec(TAB_ID);
        tab.setContent(R.id.tab_cash_deposits);
        tab.setIndicator(this.context.getString(R.string.Deposits));
        return tab;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#getTabId()
     */
    @Override
    public String getTabId() {
        return TAB_ID;
    }

    void handleAddDeposit() {
        final AssetEditor<CashDeposit> editor =
                        new AssetEditor<CashDeposit>(this.context, R.string.add_deposit_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final CashDeposit newDeposit = new CashDeposit();

                try {
                    newDeposit.setAmount(Util.parseDouble(editor.getAmount()));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    newDeposit.setAmount(0);
                }

                newDeposit.setDescription(editor.getDescription());
                accessAssets().addCashDeposit(newDeposit);
            }
        });
        editor.show();
    }

    void handleDeleteDeposit( final CashDeposit deleteDeposit ) {
        String msg = null;

        if (Util.isBlank(deleteDeposit.getDescription())) {
            msg = this.context.getString(R.string.delete_deposit_msg);
        } else {
            msg = this.context.getString(R.string.delete_deposit_with_description_msg, deleteDeposit.getDescription());
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
                       accessAssets().removeCashDeposit(deleteDeposit);
                   }
               }).show();
    }

    void handleEditDeposit( final CashDeposit editDeposit ) {
        final AssetEditor<CashDeposit> editor =
                        new AssetEditor<CashDeposit>(this.context, R.string.edit_deposit_dialog_title, editDeposit);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doEditDeposit(editor, editDeposit);
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
            final ViewGroup depositsTab = (ViewGroup)viewGroup.findViewById(R.id.tab_cash_deposits);
            this.depositsContainer = (ViewGroup)depositsTab.findViewById(R.id.deposits_container);

            { // add deposit
                final ImageButton btn = (ImageButton)viewGroup.findViewById(R.id.btn_add_deposit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View view ) {
                        handleAddDeposit();
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
        if (this.depositsContainer == null) {
            return;
        }

        this.depositsContainer.removeAllViews();

        for (final CashDeposit deposit : this.assets.getCashDeposits()) {
            final View view = this.inflater.inflate(R.layout.asset_item, null);
            this.depositsContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(deposit.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);

                try {
                    txt.setText(Util.formatMoneyAmount(Double.toString(deposit.getAmount())));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    txt.setError(this.context.getText(R.string.err_invalid_amount));
                }
            }

            { // edit deposit
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditDeposit(deposit);
                    }
                });
            }

            { // delete deposit
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteDeposit(deposit);
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
