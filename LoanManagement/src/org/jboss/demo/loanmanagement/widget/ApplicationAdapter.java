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
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.HousingExpense;
import org.jboss.demo.loanmanagement.model.Property;
import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The loan application list adapter.
 */
public final class ApplicationAdapter extends BaseExpandableListAdapter {

    /**
     * The index of the assets view in the expandable list.
     */
    private static final int ASSETS_INDEX = 3;

    /**
     * The index of the borrowers view in the expandable list.
     */
    private static final int BORROWERS_INDEX = 1;

    private static final int[] GROUPS = new int[] {R.string.Loan, R.string.Borrowers, R.string.HousingExpense,
                                                   R.string.AssetsAndLiabilities};

    /**
     * The index of the housing expense view in the expandable list.
     */
    private static final int HOUSING_EXPENSE_INDEX = 2;

    /**
     * The index of the loan view in the expandable list.
     */
    private static final int LOAN_INDEX = 0;

    private final Application application;
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
                    setupAssetsAndLiabilitiesViewListeners(result);
                    this.listenersSetup[ASSETS_INDEX] = true;
                }
            }
        }

        return result;
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
     * @param view the assets and liabilities view of the application screen (cannot be <code>null</code>)
     */
    public void setupAssetsAndLiabilitiesViewListeners( final View view ) {
        // TODO implement
    }

    /**
     * @param view the borrowers view of the application screen (cannot be <code>null</code>)
     */
    public void setupBorrowersViewListeners( final View view ) {
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
                    final double newValue = Util.parseDouble(newFirstMortgage.toString());
                    getHousingExpense().setFirstMortgage(newValue);
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
                    final double newValue = Util.parseDouble(newHazardInsurance.toString());
                    getHousingExpense().setHazardInsurance(newValue);
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
                    final double newValue = Util.parseDouble(newAssociationDues.toString());
                    getHousingExpense().setHomeOwnerAssociationDues(newValue);
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
                    final double newValue = Util.parseDouble(newOther.toString());
                    getHousingExpense().setOther(newValue);
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
                    final double newValue = Util.parseDouble(newRealEstateTaxes.toString());
                    getHousingExpense().setRealEstateTaxes(newValue);
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
                    final double newValue = Util.parseDouble(newRent.toString());
                    getHousingExpense().setRent(newValue);
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
                    final double newValue = Util.parseDouble(newAmount.toString());
                    getLoanApplication().setLoanAmount(newValue);
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
                    final double newValue = Util.parseDouble(newRate.toString());
                    getLoanApplication().setInterestRate(newValue);
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
                    final int newValue = Util.parseInt(newNumMonths.toString());
                    getLoanApplication().setNumberOfMonths(newValue);
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
                        final int newValue = Util.parseInt(newNumMonths.toString());
                        getLoanApplication().setNumberOfMonths(newValue);
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
