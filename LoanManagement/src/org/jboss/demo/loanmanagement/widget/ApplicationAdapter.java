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
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.Automobile;
import org.jboss.demo.loanmanagement.model.CashDeposit;
import org.jboss.demo.loanmanagement.model.HousingExpense;
import org.jboss.demo.loanmanagement.model.Property;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * The loan application list adapter.
 */
public final class ApplicationAdapter extends BaseExpandableListAdapter {

    private static final String ACCOUNTS_TAB_ID = "tab_accounts"; //$NON-NLS-1$

    /**
     * The index of the assets view in the expandable list.
     */
    public static final int ASSETS_INDEX = 3;

    private static final String AUTOS_TAB_ID = "tab_autos"; //$NON-NLS-1$

    /**
     * The index of the borrowers view in the expandable list.
     */
    public static final int BORROWERS_INDEX = 1;

    private static final String DEPOSITS_TAB_ID = "tab_deposits"; //$NON-NLS-1$

    private static final int[] GROUPS = new int[] {R.string.Loan, R.string.Borrowers, R.string.HousingExpense,
                                                   R.string.AssetsAndLiabilities};
    /**
     * The index of the housing expense view in the expandable list.
     */
    public static final int HOUSING_EXPENSE_INDEX = 2;

    /**
     * The index of the loan view in the expandable list.
     */
    public static final int LOAN_INDEX = 0;

    private ViewGroup accountsContainer;
    private ViewGroup accountsTab;
    private boolean accountsTabSetup = false;
    private final Application application;
    private ViewGroup autosContainer;
    private ViewGroup autosTab;
    private boolean autosTabSetup = false;
    private ViewGroup depositsContainer;
    private ViewGroup depositsTab;
    private boolean depositsTabSetup = false;
    private final ExpandableListView expandableListView;
    private final LayoutInflater inflater;
    private int lastExpandedGroupIndex;
    private final boolean[] listenersSetup = new boolean[] {false, false, false, false};

    /**
     * @param context the home loan main screen (cannot be <code>null</code>)
     * @param view the expandable list view (cannot be <code>null</code>)
     * @param loanApplication the loan application (cannot be <code>null</code>)
     */
    public ApplicationAdapter( final Context context,
                               final ExpandableListView view,
                               final Application loanApplication ) {
        this.inflater = LayoutInflater.from(context);
        this.expandableListView = view;
        this.application = loanApplication;
    }

    protected AssetsAndLiabilities getAssetsAndLiabilities() {
        return this.application.getAssetsAndLiabilities();
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild( final int groupIndex,
                            final int childIndex ) {
        return ((groupIndex * 10) + childIndex);
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId( final int groupIndex,
                            final int childIndex ) {
        return childIndex;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount( final int groupIndex ) {
        return 1;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#getChildType(int, int)
     */
    @Override
    public int getChildType( final int groupIndex,
                             final int childIndex ) {
        return groupIndex;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#getChildTypeCount()
     */
    @Override
    public int getChildTypeCount() {
        return GROUPS.length;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getChildView( final int groupIndex,
                              final int childIndex,
                              final boolean isLastChild,
                              final View view,
                              final ViewGroup viewGroup ) {
        View result = view;

        if (view == null) {
            if (LOAN_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.loan, viewGroup, false);

                if (!this.listenersSetup[LOAN_INDEX]) {
                    setupLoanViewListeners(result);
                    this.listenersSetup[LOAN_INDEX] = true;
                }
            } else if (BORROWERS_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.borrowers, viewGroup, false);

                if (!this.listenersSetup[BORROWERS_INDEX]) {
                    setupBorrowersViewListeners(result);
                    this.listenersSetup[BORROWERS_INDEX] = true;
                }
            } else if (HOUSING_EXPENSE_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.housing_expense, viewGroup, false);

                if (!this.listenersSetup[HOUSING_EXPENSE_INDEX]) {
                    setupHousingExpenseViewListeners(result);
                    this.listenersSetup[HOUSING_EXPENSE_INDEX] = true;
                }
            } else if (ASSETS_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.assets_liabilities, viewGroup, false);

                if (!this.listenersSetup[ASSETS_INDEX]) {
                    final TabHost tabHost = (TabHost)result.findViewById(R.id.tabhost);
                    tabHost.setup();

                    { // accounts tab
                        final TabSpec tab = tabHost.newTabSpec(ACCOUNTS_TAB_ID);
                        tab.setContent(R.id.tab_accounts);
                        tab.setIndicator(tabHost.getContext().getString(R.string.Accounts));
                        tabHost.addTab(tab);
                    }

                    { // autos tab
                        final TabSpec tab = tabHost.newTabSpec(AUTOS_TAB_ID);
                        tab.setContent(R.id.tab_autos);
                        tab.setIndicator(tabHost.getContext().getString(R.string.Automobiles));
                        tabHost.addTab(tab);
                    }

                    { // cash deposits tab
                        final TabSpec tab = tabHost.newTabSpec(DEPOSITS_TAB_ID);
                        tab.setContent(R.id.tab_cash_deposits);
                        tab.setIndicator(tabHost.getContext().getString(R.string.Deposits));
                        tabHost.addTab(tab);
                    }

                    setupAssetsAndLiabilitiesViewListeners(result);
                    this.listenersSetup[ASSETS_INDEX] = true;
                }
            }
        }

        return result;
    }

    private Context getContext() {
        return this.expandableListView.getContext();
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup( final int index ) {
        return GROUPS[index];
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return GROUPS.length;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId( final int index ) {
        return index;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getGroupView( final int groupIndex,
                              final boolean isExpanded,
                              final View view,
                              final ViewGroup viewGroup ) {
        View result = view;

        if (view == null) {
            result = this.inflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        }

        // set group name as tag so view can be found view later
        result.setTag(GROUPS[groupIndex]);

        final TextView textView = (TextView)result.findViewById(android.R.id.text1);
        textView.setText(GROUPS[groupIndex]);

        return result;
    }

    protected HousingExpense getHousingExpense() {
        return this.application.getHousingExpense();
    }

    protected Application getLoanApplication() {
        return this.application;
    }

    protected Property getProperty() {
        return this.application.getProperty();
    }

    protected void handleAddAccount() {
        // TODO need different layout
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);
        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        builder.setView(view).setTitle(R.string.add_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.add_account_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       final Account newAccount = new Account();

                       try {
                           newAccount.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           newAccount.setDescription(txtDescription.getText().toString());
                           getAssetsAndLiabilities().addAccount(newAccount);
                       } catch (final ParseException e) {
                           // TODO this code will not do anything as dialog is closed!!!
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleAddAuto() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);
        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        builder.setView(view).setTitle(R.string.add_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.add_auto_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       final Automobile newAuto = new Automobile();

                       try {
                           newAuto.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           newAuto.setDescription(txtDescription.getText().toString());
                           getAssetsAndLiabilities().addAutomobile(newAuto);
                       } catch (final ParseException e) {
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleAddDeposit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);
        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        builder.setView(view).setTitle(R.string.add_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.add_deposit_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       final CashDeposit newDeposit = new CashDeposit();

                       try {
                           newDeposit.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           newDeposit.setDescription(txtDescription.getText().toString());
                           getAssetsAndLiabilities().addCashDeposit(newDeposit);
                       } catch (final ParseException e) {
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleDeleteAccount( final Account deleteAccount ) {
        String msg = null;

        if (TextUtils.isEmpty(deleteAccount.getNumber())) {
            msg = getContext().getString(R.string.delete_account_msg);
        } else {
            msg = getContext().getString(R.string.delete_account_with_id_msg, deleteAccount.getNumber());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       getAssetsAndLiabilities().removeAccount(deleteAccount);
                   }
               }).show();
    }

    protected void handleDeleteAuto( final Automobile deleteAuto ) {
        String msg = null;

        if (TextUtils.isEmpty(deleteAuto.getDescription())) {
            msg = getContext().getString(R.string.delete_auto_msg);
        } else {
            msg = getContext().getString(R.string.delete_auto_with_description_msg, deleteAuto.getDescription());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       getAssetsAndLiabilities().removeAutomobile(deleteAuto);
                   }
               }).show();
    }

    protected void handleDeleteDeposit( final CashDeposit deleteDeposit ) {
        String msg = null;

        if (TextUtils.isEmpty(deleteDeposit.getDescription())) {
            msg = getContext().getString(R.string.delete_deposit_msg);
        } else {
            msg = getContext().getString(R.string.delete_deposit_with_description_msg, deleteDeposit.getDescription());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       getAssetsAndLiabilities().removeCashDeposit(deleteDeposit);
                   }
               }).show();
    }

    protected void handleEditAccount( final Account editAccount ) {
        // TODO need number and address
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);

        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        txtAmount.setText(Double.toString(editAccount.getAmount()));

        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        txtDescription.setText(editAccount.getDescription());

        builder.setView(view).setTitle(R.string.edit_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.edit_account_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       try {
                           editAccount.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           editAccount.setDescription(txtDescription.getText().toString());
                       } catch (final ParseException e) {
                           // TODO the dialog will be closed!!!
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleEditAuto( final Automobile editAuto ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);

        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        txtAmount.setText(Double.toString(editAuto.getAmount()));

        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        txtDescription.setText(editAuto.getDescription());

        builder.setView(view).setTitle(R.string.edit_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.edit_auto_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       try {
                           editAuto.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           editAuto.setDescription(txtDescription.getText().toString());
                       } catch (final ParseException e) {
                           // TODO fix this
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleEditDeposit( final CashDeposit editDeposit ) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.expandableListView.getContext());
        final View view = this.inflater.inflate(R.layout.asset, null);

        final EditText txtAmount = (EditText)view.findViewById(R.id.txt_asset_amount);
        txtAmount.setText(Double.toString(editDeposit.getAmount()));

        final EditText txtDescription = (EditText)view.findViewById(R.id.txt_asset_description);
        txtDescription.setText(editDeposit.getDescription());

        builder.setView(view).setTitle(R.string.edit_dialog_title).setIcon(R.drawable.ic_home)
               .setMessage(R.string.edit_deposit_dialog_msg).setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       try {
                           editDeposit.setAmount(Util.parseDouble(txtAmount.getText().toString()));
                           editDeposit.setDescription(txtDescription.getText().toString());
                       } catch (final ParseException e) {
                           // TODO fix this
                           txtAmount.setError(view.getResources().getString(R.string.err_invalid_amount));
                       }
                   }
               }).show();
    }

    protected void handleTabChanged( final String tabId,
                                     final View tabHost ) {
        // TODO hide or disable edit and delete buttons if list is empty
        if (ACCOUNTS_TAB_ID.equals(tabId) && !this.accountsTabSetup) {
            this.accountsTab = (ViewGroup)tabHost.findViewById(R.id.tab_accounts);
            this.accountsContainer = (ViewGroup)this.accountsTab.findViewById(R.id.accounts_container);

            { // add account
                final ImageButton btn = (ImageButton)tabHost.findViewById(R.id.btn_add_account);
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

            refreshAccounts();
            this.accountsTabSetup = true;
        } else if (AUTOS_TAB_ID.equals(tabId) && !this.autosTabSetup) {
            this.autosTab = (ViewGroup)tabHost.findViewById(R.id.tab_autos);
            this.autosContainer = (ViewGroup)this.autosTab.findViewById(R.id.autos_container);

            { // add auto
                final ImageButton btn = (ImageButton)this.autosTab.findViewById(R.id.btn_add_auto);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View view ) {
                        handleAddAuto();
                    }
                });
            }

            refreshAutos();
            this.autosTabSetup = true;
        } else if (DEPOSITS_TAB_ID.equals(tabId) && !this.depositsTabSetup) {
            this.depositsTab = (ViewGroup)tabHost.findViewById(R.id.tab_cash_deposits);
            this.depositsContainer = (ViewGroup)this.depositsTab.findViewById(R.id.deposits_container);

            { // add deposit
                final ImageButton btn = (ImageButton)tabHost.findViewById(R.id.btn_add_deposit);
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

            refreshDeposits();
            this.depositsTabSetup = true;
        }
    }

    /**
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable( final int newGroupPosition,
                                      final int newChildPosition ) {
        return false;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#onGroupExpanded(int)
     */
    @Override
    public void onGroupExpanded( final int groupIndex ) {
        if (groupIndex != this.lastExpandedGroupIndex) {
            this.expandableListView.collapseGroup(this.lastExpandedGroupIndex);
            this.lastExpandedGroupIndex = groupIndex;
        }

        super.onGroupExpanded(groupIndex);
    }

    /**
     * Re-loads the account data from the application.
     */
    public void refreshAccounts() {
        this.accountsContainer.removeAllViews();

        for (final Account account : getAssetsAndLiabilities().getAccounts()) {
            final View view = this.inflater.inflate(R.layout.readonly_asset, null);
            this.accountsContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(account.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);
                txt.setText(Double.toString(account.getAmount()));
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
     * Re-loads the automobile data from the application.
     */
    public void refreshAutos() {
        this.autosContainer.removeAllViews();

        for (final Automobile auto : getAssetsAndLiabilities().getAutomobiles()) {
            final View view = this.inflater.inflate(R.layout.readonly_asset, null);
            this.autosContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(auto.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);
                txt.setText(Double.toString(auto.getAmount()));
            }

            { // edit auto
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditAuto(auto);
                    }
                });
            }

            { // delete auto
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteAuto(auto);
                    }
                });
            }
        }
    }

    /**
     * Re-loads the cash deposit data from the application.
     */
    public void refreshDeposits() {
        this.depositsContainer.removeAllViews();

        for (final CashDeposit deposit : getAssetsAndLiabilities().getCashDeposits()) {
            final View view = this.inflater.inflate(R.layout.readonly_asset, null);
            this.depositsContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(deposit.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);
                txt.setText(Double.toString(deposit.getAmount()));
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

    private void setupAssetsAndLiabilitiesViewListeners( final View view ) {
        { // completed by
            final RadioGroup group = (RadioGroup)view.findViewById(R.id.grp_completed_by);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                /**
                 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup,
                 *      int)
                 */
                @Override
                public void onCheckedChanged( final RadioGroup radioGroup,
                                              final int btnId ) {
                    String completedBy = null;

                    if (btnId == R.id.btn_jointly) {
                        completedBy = AssetsAndLiabilities.COMPLETED_BY[AssetsAndLiabilities.JOINTLY_INDEX];
                    } else if (btnId == R.id.btn_jointly) {
                        completedBy = AssetsAndLiabilities.COMPLETED_BY[AssetsAndLiabilities.NOT_JOINTLY_INDEX];
                    }

                    getAssetsAndLiabilities().setCompletedType(completedBy);
                }
            });
        }

        final TabHost tabHost = (TabHost)view.findViewById(R.id.tabhost);
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            /**
             * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
             */
            @Override
            public void onTabChanged( final String tabId ) {
                handleTabChanged(tabId, tabHost);
            }
        });

        // call this manually since no event is generated when tabs first displayed
        handleTabChanged(ACCOUNTS_TAB_ID, tabHost);
    }

    /**
     * @param view the borrowers view of the application screen (cannot be <code>null</code>)
     */
    private void setupBorrowersViewListeners( final View view ) {
        // TODO implement
    }

    private void setupHousingExpenseViewListeners( final View view ) {
        { // first mortgage
            final TextView textView = (TextView)view.findViewById(R.id.txt_first_mortgage);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newFirstMortgage ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newFirstMortgage.toString());
                        getHousingExpense().setFirstMortgage(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // hazard insurance
            final TextView textView = (TextView)view.findViewById(R.id.txt_hazard_insurance);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newHazardInsurance ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newHazardInsurance.toString());
                        getHousingExpense().setHazardInsurance(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // association dues
            final TextView textView = (TextView)view.findViewById(R.id.txt_association_dues);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newAssociationDues ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newAssociationDues.toString());
                        getHousingExpense().setHomeOwnerAssociationDues(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // other
            final TextView textView = (TextView)view.findViewById(R.id.txt_other_housing_expense);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newOther ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newOther.toString());
                        getHousingExpense().setOther(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // real estate taxes
            final TextView textView = (TextView)view.findViewById(R.id.txt_real_estate_taxes);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRealEstateTaxes ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newRealEstateTaxes.toString());
                        getHousingExpense().setRealEstateTaxes(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // rent
            final TextView textView = (TextView)view.findViewById(R.id.txt_rent);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRent ) {
                    double newValue;
                    try {
                        newValue = Util.parseDouble(newRent.toString());
                        getHousingExpense().setRent(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }
    }

    private void setupLoanViewListeners( final View view ) {
        { // loan type
            final Spinner cbxLoanType = (Spinner)view.findViewById(R.id.cbx_loan_type);
            cbxLoanType.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                    getLoanApplication().setType(Application.LOAN_TYPES[index]);
                }
            });
        }

        { // loan description
            final TextView textView = (TextView)view.findViewById(R.id.txt_description);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRent ) {
                    final String newValue = newRent.toString();
                    getLoanApplication().setDescription(newValue);
                }
            });
        }

        { // loan amount
            final TextView textView = (TextView)view.findViewById(R.id.txt_loan_amount);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newAmount ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newAmount.toString());
                        getLoanApplication().setLoanAmount(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // interest rate
            final TextView textView = (TextView)view.findViewById(R.id.txt_interest_rate);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRate ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newRate.toString());
                        getLoanApplication().setInterestRate(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // loan months
            final TextView textView = (TextView)view.findViewById(R.id.txt_loan_months);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newNumMonths ) {
                    int newValue;

                    try {
                        newValue = Util.parseInt(newNumMonths.toString());
                        getLoanApplication().setNumberOfMonths(newValue);
                    } catch (final ParseException e) {
                        textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // amortization type
            final Spinner cbxAmortizationType = (Spinner)view.findViewById(R.id.cbx_amortization_type);
            cbxAmortizationType.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                    getLoanApplication().setAmortizationType(Application.AMORTIZATION_TYPES[index]);
                }
            });
        }

        { // property
            { // property type
                final Spinner cbxPropertyType = (Spinner)view.findViewById(R.id.cbx_property_type);
                cbxPropertyType.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                        getProperty().setType(Property.PROPERTY_TYPES[index]);
                    }
                });
            }

            { // number of unites
                final TextView textView = (TextView)view.findViewById(R.id.txt_num_units);
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newNumMonths ) {
                        int newValue;

                        try {
                            newValue = Util.parseInt(newNumMonths.toString());
                            getLoanApplication().setNumberOfMonths(newValue);
                        } catch (final ParseException e) {
                            textView.setError(view.getResources().getString(R.string.err_invalid_amount));
                        }
                    }
                });
            }
        }

        { // purchase type
            final Spinner cbxAmortizationType = (Spinner)view.findViewById(R.id.cbx_purchase_type);
            cbxAmortizationType.setOnItemSelectedListener(new ItemSelectedAdapter() {

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
                    getLoanApplication().setPurchaseType(Application.PURCHASE_TYPES[index]);
                }
            });
        }

        { // source of down payment
            final TextView textView = (TextView)view.findViewById(R.id.txt_down_payment_source);
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newSourceOfDownPayment ) {
                    final String newValue = newSourceOfDownPayment.toString();
                    getLoanApplication().setDownPaymentSource(newValue);
                }
            });
        }
    }

}
