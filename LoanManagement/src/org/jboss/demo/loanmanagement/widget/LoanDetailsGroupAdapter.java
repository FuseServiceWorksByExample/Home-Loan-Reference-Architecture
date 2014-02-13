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
import org.jboss.demo.loanmanagement.model.Address;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.Property;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

final class LoanDetailsGroupAdapter implements GroupAdapter {

    static final int DESCRIPTION = R.string.item_loan_description;
    static final int TITLE = R.string.Loan;

    private Application application;
    private Context context;

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#createView(android.view.ViewGroup,
     *      android.view.LayoutInflater)
     */
    @Override
    public View createView( final ViewGroup viewGroup,
                            final LayoutInflater layoutInflater ) {
        final View view = layoutInflater.inflate(R.layout.loan, viewGroup, false);
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
                    getApplication().setType(Application.LOAN_TYPES[index]);
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
                    getApplication().setDescription(newValue);
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
                        getApplication().setLoanAmount(newValue);
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
                        getApplication().setInterestRate(newValue);
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
                        getApplication().setNumberOfMonths(newValue);
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
                    getApplication().setAmortizationType(Application.AMORTIZATION_TYPES[index]);
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
                    getApplication().setPurchaseType(Application.PURCHASE_TYPES[index]);
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
                    getApplication().setDownPaymentSource(newValue);
                }
            });
        }

        return view;
    }

    Application getApplication() {
        return this.application;
    }

    Context getContext() {
        return this.context;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#getDescription()
     */
    @Override
    public int getDescription() {
        return DESCRIPTION;
    }

    private Property getProperty() {
        return this.application.getProperty();
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
