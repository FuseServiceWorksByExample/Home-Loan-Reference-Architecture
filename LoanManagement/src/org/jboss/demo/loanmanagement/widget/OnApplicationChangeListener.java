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
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Listens for changes to UI widgets and modifies the {@link Application} model object.
 */
public final class OnApplicationChangeListener {

    private final Application application;

    /**
     * @param loanApplication the loan application (cannot be <code>null</code>)
     */
    public OnApplicationChangeListener( final Application loanApplication ) {
        this.application = loanApplication;
    }

    protected Application getApplication() {
        return this.application;
    }

    protected AssetsAndLiabilities getAssetsAndLiabilities() {
        return this.application.getAssetsAndLiabilities();
    }

    protected HousingExpense getHousingExpense() {
        return this.application.getHousingExpense();
    }

    protected Property getProperty() {
        return this.application.getProperty();
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

    /**
     * @param view the housing expense view of the application screen (cannot be <code>null</code>)
     */
    public void setupHousingExpenseViewListeners( final View view ) {
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

    /**
     * @param view the loan view of the application screen (cannot be <code>null</code>)
     */
    public void setupLoanViewListeners( final View view ) {
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
                    getApplication().setType(Application.LOAN_TYPES[index]);
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
                    getApplication().setDescription(newValue);
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
                    getApplication().setLoanAmount(newValue);
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
                    getApplication().setInterestRate(newValue);
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
                    getApplication().setNumberOfMonths(newValue);
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
                    getApplication().setAmortizationType(Application.AMORTIZATION_TYPES[index]);
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
                        getApplication().setNumberOfMonths(newValue);
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
                    getApplication().setPurchaseType(Application.PURCHASE_TYPES[index]);
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
                    getApplication().setDownPaymentSource(newValue);
                }
            });
        }
    }

}
