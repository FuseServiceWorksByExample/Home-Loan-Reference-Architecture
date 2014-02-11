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
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.Address;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.Automobile;
import org.jboss.demo.loanmanagement.model.Borrower;
import org.jboss.demo.loanmanagement.model.CashDeposit;
import org.jboss.demo.loanmanagement.model.HousingExpense;
import org.jboss.demo.loanmanagement.model.Property;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RadioButton;
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

    private static final int[] GROUP_DESCRIPTIONS = new int[] {R.string.item_loan_description,
                                                               R.string.item_borrowers_description,
                                                               R.string.item_housing_expense_description,
                                                               R.string.item_assets_and_liabilities_description};

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

    private static String constructBorrowerName( final Borrower borrower ) {
        final StringBuilder name = new StringBuilder();

        { // first
            final String first = borrower.getFirstName();

            if (!Util.isBlank(first)) {
                name.append(first);
            }
        }

        { // middle
            final String middle = borrower.getMiddleName();

            if (!Util.isBlank(middle)) {
                if (name.length() != 0) {
                    name.append(' ');
                }

                name.append(middle);
            }
        }

        { // last
            final String last = borrower.getLastName();

            if (!Util.isBlank(last)) {
                if (name.length() != 0) {
                    name.append(' ');
                }

                name.append(last);
            }
        }

        return name.toString();
    }

    protected static void doEditAccount( final AccountEditor editor,
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

    protected static void doEditAuto( final AssetEditor<Automobile> editor,
                                      final Automobile editAuto ) {
        try {
            editAuto.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
        }

        editAuto.setDescription(editor.getDescription());
    }

    protected static void doEditDeposit( final AssetEditor<CashDeposit> editor,
                                         final CashDeposit editDeposit ) {
        try {
            editDeposit.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
        }

        editDeposit.setDescription(editor.getDescription());
    }

    private ViewGroup accountsContainer;
    private boolean accountsTabSetup = false;
    private final Application application;
    private ViewGroup autosContainer;
    private boolean autosTabSetup = false;
    private ViewGroup borrowersContainer;
    private final Context context;
    private ViewGroup depositsContainer;
    private boolean depositsTabSetup = false;
    private final ExpandableListView expandableListView;
    private final LayoutInflater inflater;
    private int lastExpandedGroupIndex = AdapterView.INVALID_POSITION;

    private final boolean[] listenersSetup = new boolean[] {false, false, false, false};

    /**
     * @param screen the home loan main screen (cannot be <code>null</code>)
     * @param view the expandable list view (cannot be <code>null</code>)
     * @param loanApplication the loan application (cannot be <code>null</code>)
     */
    public ApplicationAdapter( final Context screen,
                               final ExpandableListView view,
                               final Application loanApplication ) {
        this.context = screen;
        this.inflater = LayoutInflater.from(this.context);
        this.expandableListView = view;
        this.application = loanApplication;
    }

    /**
     * Collapses the last group that was expanded.
     */
    public void collapseAllGroups() {
        if (this.lastExpandedGroupIndex != AdapterView.INVALID_POSITION) {
            this.expandableListView.collapseGroup(this.lastExpandedGroupIndex);
        }
    }

    protected void doAddAccount( final AccountEditor editor ) {
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
        getAssetsAndLiabilities().addAccount(newAccount);
    }

    protected void doAddAuto( final AssetEditor<Automobile> editor ) {
        final Automobile newAuto = new Automobile();

        try {
            newAuto.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
            newAuto.setAmount(0);
        }

        newAuto.setDescription(editor.getDescription());
        getAssetsAndLiabilities().addAutomobile(newAuto);
    }

    protected void doAddDeposit( final AssetEditor<CashDeposit> editor ) {
        final CashDeposit newDeposit = new CashDeposit();

        try {
            newDeposit.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
            newDeposit.setAmount(0);
        }

        newDeposit.setDescription(editor.getDescription());
        getAssetsAndLiabilities().addCashDeposit(newDeposit);
    }

    protected Application getApplication() {
        return this.application;
    }

    protected AssetsAndLiabilities getAssetsAndLiabilities() {
        return this.application.getAssetsAndLiabilities();
    }

    protected List<Borrower> getBorrowers() {
        return this.application.getBorrowers();
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
                    final TabHost tabHost = (TabHost)result.findViewById(R.id.assets_tab_host);
                    tabHost.setup();

                    { // accounts tab
                        final TabSpec tab = tabHost.newTabSpec(ACCOUNTS_TAB_ID);
                        tab.setContent(R.id.tab_accounts);
                        tab.setIndicator(getContext().getString(R.string.Accounts));
                        tabHost.addTab(tab);
                    }

                    { // autos tab
                        final TabSpec tab = tabHost.newTabSpec(AUTOS_TAB_ID);
                        tab.setContent(R.id.tab_autos);
                        tab.setIndicator(getContext().getString(R.string.Automobiles));
                        tabHost.addTab(tab);
                    }

                    { // cash deposits tab
                        final TabSpec tab = tabHost.newTabSpec(DEPOSITS_TAB_ID);
                        tab.setContent(R.id.tab_cash_deposits);
                        tab.setIndicator(getContext().getString(R.string.Deposits));
                        tabHost.addTab(tab);
                    }

                    setupAssetsAndLiabilitiesViewListeners(result);
                    this.listenersSetup[ASSETS_INDEX] = true;
                }
            }
        }

        return result;
    }

    protected Context getContext() {
        return this.context;
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
            result = this.inflater.inflate(android.R.layout.simple_expandable_list_item_2, viewGroup, false);
            result.setBackgroundResource(R.drawable.gradient);
        }

        // set group name as tag so view can be found view later
        result.setTag(GROUPS[groupIndex]);

        { // group title
            final TextView textView = (TextView)result.findViewById(android.R.id.text1);
            textView.setText(GROUPS[groupIndex]);
        }

        { // group description
            final TextView textView = (TextView)result.findViewById(android.R.id.text2);
            textView.setText(GROUP_DESCRIPTIONS[groupIndex]);
        }

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
        final AccountEditor editor = new AccountEditor(getContext(), R.string.add_account_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doAddAccount(editor);
            }
        });
        editor.show();
    }

    protected void handleAddAuto() {
        final AssetEditor<Automobile> editor =
                        new AssetEditor<Automobile>(getContext(), R.string.add_auto_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doAddAuto(editor);
            }
        });
        editor.show();
    }

    protected void handleAddBorrower() {
        final BorrowerEditor editor = new BorrowerEditor(getContext(), R.string.add_borrower_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final Borrower newBorrower = editor.getBorrower();
                getApplication().addBorrower(newBorrower);
            }
        });
        editor.show();
    }

    protected void handleAddDeposit() {
        final AssetEditor<CashDeposit> editor =
                        new AssetEditor<CashDeposit>(getContext(), R.string.add_deposit_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doAddDeposit(editor);
            }
        });
        editor.show();
    }

    protected void handleDeleteAccount( final Account deleteAccount ) {
        String msg = null;

        if (Util.isBlank(deleteAccount.getNumber())) {
            msg = getContext().getString(R.string.delete_account_msg);
        } else {
            msg = getContext().getString(R.string.delete_account_with_id_msg, deleteAccount.getNumber());
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
                       getAssetsAndLiabilities().removeAccount(deleteAccount);
                   }
               }).show();
    }

    protected void handleDeleteAuto( final Automobile deleteAuto ) {
        String msg = null;

        if (Util.isBlank(deleteAuto.getDescription())) {
            msg = getContext().getString(R.string.delete_auto_msg);
        } else {
            msg = getContext().getString(R.string.delete_auto_with_description_msg, deleteAuto.getDescription());
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
                       getAssetsAndLiabilities().removeAutomobile(deleteAuto);
                   }
               }).show();
    }

    protected void handleDeleteBorrower( final Borrower deleteBorrower ) {
        final String msg = getContext().getString(R.string.delete_borrower_msg, constructBorrowerName(deleteBorrower));
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
                       getApplication().removeBorrower(deleteBorrower);
                   }
               }).show();
    }

    protected void handleDeleteDeposit( final CashDeposit deleteDeposit ) {
        String msg = null;

        if (Util.isBlank(deleteDeposit.getDescription())) {
            msg = getContext().getString(R.string.delete_deposit_msg);
        } else {
            msg = getContext().getString(R.string.delete_deposit_with_description_msg, deleteDeposit.getDescription());
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
                       getAssetsAndLiabilities().removeCashDeposit(deleteDeposit);
                   }
               }).show();
    }

    protected void handleEditAccount( final Account editAccount ) {
        final AccountEditor editor = new AccountEditor(getContext(), R.string.edit_account_dialog_title, editAccount);
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

    protected void handleEditAuto( final Automobile editAuto ) {
        final AssetEditor<Automobile> editor =
                        new AssetEditor<Automobile>(getContext(), R.string.edit_auto_dialog_title, editAuto);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doEditAuto(editor, editAuto);
            }
        });
        editor.show();
    }

    protected void handleEditBorrower( final Borrower editBorrower ) {
        final BorrowerEditor editor =
                        new BorrowerEditor(getContext(), R.string.edit_borrower_dialog_title, editBorrower);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                editBorrower.update(editor.getBorrower());
            }
        });
        editor.show();
    }

    protected void handleEditDeposit( final CashDeposit editDeposit ) {
        final AssetEditor<CashDeposit> editor =
                        new AssetEditor<CashDeposit>(getContext(), R.string.edit_deposit_dialog_title, editDeposit);
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

    protected void handleTabChanged( final String tabId,
                                     final View tabHost ) {
        if (ACCOUNTS_TAB_ID.equals(tabId) && !this.accountsTabSetup) {
            final ViewGroup accountsTab = (ViewGroup)tabHost.findViewById(R.id.tab_accounts);
            this.accountsContainer = (ViewGroup)accountsTab.findViewById(R.id.accounts_container);

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
            final ViewGroup autosTab = (ViewGroup)tabHost.findViewById(R.id.tab_autos);
            this.autosContainer = (ViewGroup)autosTab.findViewById(R.id.autos_container);

            { // add auto
                final ImageButton btn = (ImageButton)autosTab.findViewById(R.id.btn_add_auto);
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
            final ViewGroup depositsTab = (ViewGroup)tabHost.findViewById(R.id.tab_cash_deposits);
            this.depositsContainer = (ViewGroup)depositsTab.findViewById(R.id.deposits_container);

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
        } else {
            assert false;
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
                    txt.setError(getContext().getText(R.string.err_invalid_amount));
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
     * Re-loads the automobile data from the application.
     */
    public void refreshAutos() {
        this.autosContainer.removeAllViews();

        for (final Automobile auto : getAssetsAndLiabilities().getAutomobiles()) {
            final View view = this.inflater.inflate(R.layout.asset_item, null);
            this.autosContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(auto.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);

                try {
                    txt.setText(Util.formatMoneyAmount(Double.toString(auto.getAmount())));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    txt.setError(getContext().getText(R.string.err_invalid_amount));
                }
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
     * Re-loads the borrowers data from the application.
     */
    public void refreshBorrowers() {
        this.borrowersContainer.removeAllViews();

        for (final Borrower borrower : getBorrowers()) {
            final View view = this.inflater.inflate(R.layout.borrower_item, null);
            this.borrowersContainer.addView(view);

            { // name
                final TextView txt = (TextView)view.findViewById(R.id.txt_borrower_name);
                txt.setText(constructBorrowerName(borrower));
            }

            { // type
                final TextView txt = (TextView)view.findViewById(R.id.txt_borrower_type);
                final String type = borrower.getType();
                String label = null;

                if (Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX].equals(type)) {
                    label = getContext().getString(R.string.Borrower);
                } else {
                    label = getContext().getString(R.string.CoBorrower);
                }

                txt.setText(label);
            }

            { // edit borrower
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditBorrower(borrower);
                    }
                });
            }

            { // delete borrower
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteBorrower(borrower);
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
                    txt.setError(getContext().getText(R.string.err_invalid_amount));
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

                    assert !Util.isBlank(completedBy);
                    getAssetsAndLiabilities().setCompletedType(completedBy);
                }
            });
        }

        final TabHost tabHost = (TabHost)view.findViewById(R.id.assets_tab_host);
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
        this.borrowersContainer = (ViewGroup)view.findViewById(R.id.borrowers_container);

        { // add borrower
            final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_add_borrower);
            btn.setOnClickListener(new OnClickListener() {

                /**
                 * @see android.view.View.OnClickListener#onClick(android.view.View)
                 */
                @Override
                public void onClick( final View imageView ) {
                    handleAddBorrower();
                }
            });
        }

        refreshBorrowers();
    }

    private void setupHousingExpenseViewListeners( final View view ) {
        final HousingExpense housingExpense = getHousingExpense();

        { // type
            final String type = housingExpense.getType();

            if (HousingExpense.HOUSING_EXPENSE_TYPES[HousingExpense.PRESENT_INDEX].equals(type)) {
                final RadioButton btnPresent = (RadioButton)view.findViewById(R.id.btn_present);
                btnPresent.setChecked(true);
            } else if (HousingExpense.HOUSING_EXPENSE_TYPES[HousingExpense.PROPOSED_INDEX].equals(type)) {
                final RadioButton btnProposed = (RadioButton)view.findViewById(R.id.btn_proposed);
                btnProposed.setChecked(true);
            }

            final RadioGroup group = (RadioGroup)view.findViewById(R.id.grp_housing_expense_type);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                /**
                 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup,
                 *      int)
                 */
                @Override
                public void onCheckedChanged( final RadioGroup radioGroup,
                                              final int btnId ) {
                    String newType = null;

                    if (btnId == R.id.btn_present) {
                        newType = HousingExpense.HOUSING_EXPENSE_TYPES[HousingExpense.PRESENT_INDEX];
                    } else if (btnId == R.id.btn_proposed) {
                        newType = HousingExpense.HOUSING_EXPENSE_TYPES[HousingExpense.PROPOSED_INDEX];
                    }

                    assert !Util.isBlank(newType);
                    housingExpense.setType(newType);
                }
            });
        }

        { // first mortgage
            final TextView textView = (TextView)view.findViewById(R.id.txt_first_mortgage);
            textView.setText(Util.formatMoneyAmount(housingExpense.getFirstMortgage()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newFirstMortgage ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newFirstMortgage.toString());
                        housingExpense.setFirstMortgage(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // hazard insurance
            final TextView textView = (TextView)view.findViewById(R.id.txt_hazard_insurance);
            textView.setText(Util.formatMoneyAmount(housingExpense.getHazardInsurance()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newHazardInsurance ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newHazardInsurance.toString());
                        housingExpense.setHazardInsurance(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // association dues
            final TextView textView = (TextView)view.findViewById(R.id.txt_association_dues);
            textView.setText(Util.formatMoneyAmount(housingExpense.getHomeOwnerAssociationDues()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newAssociationDues ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newAssociationDues.toString());
                        housingExpense.setHomeOwnerAssociationDues(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // real estate taxes
            final TextView textView = (TextView)view.findViewById(R.id.txt_real_estate_taxes);
            textView.setText(Util.formatMoneyAmount(housingExpense.getRealEstateTaxes()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRealEstateTaxes ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newRealEstateTaxes.toString());
                        housingExpense.setRealEstateTaxes(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // rent
            final TextView textView = (TextView)view.findViewById(R.id.txt_rent);
            textView.setText(Util.formatMoneyAmount(housingExpense.getRent()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newRent ) {
                    double newValue;
                    try {
                        newValue = Util.parseDouble(newRent.toString());
                        housingExpense.setRent(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // other mortgages
            final TextView textView = (TextView)view.findViewById(R.id.txt_other_mortgages);
            textView.setText(Util.formatMoneyAmount(housingExpense.getOtherMortgages()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newOther ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newOther.toString());
                        housingExpense.setOtherMortgages(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // misc
            final TextView textView = (TextView)view.findViewById(R.id.txt_misc_housing_expense);
            textView.setText(Util.formatMoneyAmount(housingExpense.getOther()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
            textView.addTextChangedListener(new TextWatcherAdapter() {

                /**
                 * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                 */
                @Override
                public void afterTextChanged( final Editable newOther ) {
                    double newValue;

                    try {
                        newValue = Util.parseDouble(newOther.toString());
                        housingExpense.setOther(newValue);
                    } catch (final ParseException e) {
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }
    }

    private void setupLoanViewListeners( final View view ) {
        { // loan type
            final Spinner cbxLoanType = (Spinner)view.findViewById(R.id.cbx_loan_type);
            final String type = this.application.getType();
            Util.selectSpinnerItem(cbxLoanType, type, Application.LOAN_TYPES);

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
            textView.setText(this.application.getDescription());
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
            textView.setText(Util.formatMoneyAmount(this.application.getAmount()));
            textView.setFilters(new InputFilter[] {Util.getCurrencyFilter()});
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
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // interest rate
            final TextView textView = (TextView)view.findViewById(R.id.txt_interest_rate);
            textView.setText(Double.toString(this.application.getRate()));
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
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // loan months
            final TextView textView = (TextView)view.findViewById(R.id.txt_loan_months);
            textView.setText(Integer.toString(this.application.getNumMonths()));
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
                        textView.setError(getContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        { // amortization type
            final Spinner cbxAmortizationType = (Spinner)view.findViewById(R.id.cbx_amortization_type);
            final String amortizationType = this.application.getAmoritizationType();
            Util.selectSpinnerItem(cbxAmortizationType, amortizationType, Application.AMORTIZATION_TYPES);

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
            final Property property = getProperty();
            Address tempAddress = property.getAddress();

            if (tempAddress == null) {
                tempAddress = new Address();
                property.setAddress(tempAddress);
            }

            final Address address = tempAddress;

            { // property type
                final Spinner cbxPropertyType = (Spinner)view.findViewById(R.id.cbx_property_type);
                final String propertyType = property.getType();
                Util.selectSpinnerItem(cbxPropertyType, propertyType, Property.PROPERTY_TYPES);

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
                        property.setType(Property.PROPERTY_TYPES[index]);
                    }
                });
            }

            { // line 1
                final TextView textView = (TextView)view.findViewById(R.id.txt_addr_line_1);
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
                final TextView textView = (TextView)view.findViewById(R.id.txt_addr_line_2);
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
                final TextView textView = (TextView)view.findViewById(R.id.txt_city);
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
                final TextView textView = (TextView)view.findViewById(R.id.txt_state);
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
                final TextView textView = (TextView)view.findViewById(R.id.txt_zipcode);
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
                final TextView textView = (TextView)view.findViewById(R.id.txt_county);
                textView.setText(address.getCounty());
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

            { // year built
                final TextView textView = (TextView)view.findViewById(R.id.txt_year_built);
                textView.setText(Integer.toString(property.getYearBuilt()));
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newYearBuilt ) {
                        int newValue;

                        try {
                            newValue = Util.parseInt(newYearBuilt.toString());
                            property.setYearBuilt(newValue);
                        } catch (final ParseException e) {
                            textView.setError(getContext().getText(R.string.err_invalid_amount));
                        }
                    }
                });
            }

            { // number of units
                final TextView textView = (TextView)view.findViewById(R.id.txt_num_units);
                textView.setText(Integer.toString(property.getNumUnits()));
                textView.addTextChangedListener(new TextWatcherAdapter() {

                    /**
                     * @see org.jboss.demo.loanmanagement.widget.TextWatcherAdapter#afterTextChanged(android.text.Editable)
                     */
                    @Override
                    public void afterTextChanged( final Editable newNumUnits ) {
                        int newValue;

                        try {
                            newValue = Util.parseInt(newNumUnits.toString());
                            property.setNumUnits(newValue);
                        } catch (final ParseException e) {
                            textView.setError(getContext().getText(R.string.err_invalid_amount));
                        }
                    }
                });
            }
        }

        { // purchase type
            final Spinner cbxAmortizationType = (Spinner)view.findViewById(R.id.cbx_purchase_type);
            final String purchaseType = this.application.getPurchaseType();
            Util.selectSpinnerItem(cbxAmortizationType, purchaseType, Application.PURCHASE_TYPES);

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
            textView.setText(this.application.getDownPaymentSource());
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
