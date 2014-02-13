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
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.HousingExpense;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

final class HousingExpenseGroupAdapter implements GroupAdapter {

    static final int DESCRIPTION = R.string.item_housing_expense_description;
    static final int TITLE = R.string.HousingExpense;

    private Application application;
    private Context context;

    Context accessContext() {
        return this.context;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#createView(android.view.ViewGroup,
     *      android.view.LayoutInflater)
     */
    @Override
    public View createView( final ViewGroup parent,
                            final LayoutInflater layoutInflater ) {
        final View view = layoutInflater.inflate(R.layout.housing_expense, parent, false);

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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
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
                        textView.setError(accessContext().getText(R.string.err_invalid_amount));
                    }
                }
            });
        }

        return view;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#getDescription()
     */
    @Override
    public int getDescription() {
        return DESCRIPTION;
    }

    private HousingExpense getHousingExpense() {
        return this.application.getHousingExpense();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#getTitle()
     */
    @Override
    public int getTitle() {
        return TITLE;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#refresh()
     */
    @Override
    public void refresh() {
        // nothing to do
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#setContext(android.content.Context)
     */
    @Override
    public void setContext( final Context appContext ) {
        this.context = appContext;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#setModel(org.jboss.demo.loanmanagement.model.Application)
     */
    @Override
    public void setModel( final Application model ) {
        this.application = model;
    }

}
