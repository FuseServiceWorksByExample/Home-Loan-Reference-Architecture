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
package org.jboss.demo.loanmanagement.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A loan application model object.
 */
public final class Application implements PropertyChangeListener {

    /**
     * The loan amortization types.
     */
    public static final String[] AMORTIZATION_TYPES = new String[] {"Fixed_Rate", "GPM", "ARM", "Other"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * The loan types.
     */
    public static final String[] LOAN_TYPES = new String[] {"Conventional", "FHA", "VA", "FmHA", "Other"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

    protected static final String PROPERTY_PREFIX = Application.class.getSimpleName() + '.';

    /**
     * The loan purchase types.
     */
    public static final String[] PURCHASE_TYPES = new String[] {"Purchase", "Refinance", "Construction", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                "Construction_Permanent", "Other"}; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * @param original the application being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Application copy( final Application original ) {
        final Application copy = new Application();

        copy.setAmortizationType(original.amortizationType);
        copy.setLoanAmount(original.getAmount());
        copy.setAssetsAndLiabilities(AssetsAndLiabilities.copy(original.getAssetsAndLiabilities()));
        copy.setBorrowers(original.getBorrowers());
        copy.setDescription(original.description);
        copy.setDownPaymentSource(original.downPaymentSource);
        copy.setHousingExpense(HousingExpense.copy(original.getHousingExpense()));
        copy.setNumberOfMonths(original.numMonths);
        copy.setProperty(Property.copy(original.getProperty()));
        copy.setPurchaseType(original.purchaseType);
        copy.setInterestRate(original.getRate());
        copy.setType(original.type);

        return copy;
    }

    private String amortizationType;
    private BigDecimal amount; // n.xx
    private AssetsAndLiabilities assetsAndLiabilities;
    private List<Borrower> borrowers; // need at least one
    private String description; // max 1000
    private String downPaymentSource; // max 1000
    private HousingExpense housingExpense;
    private int numMonths = 0; // positive
    private final PropertyChangeSupport pcs;
    private Property property;
    private String purchaseType;
    private BigDecimal rate; // n.xxxx
    private String type;

    /**
     * Constructs an application with default values.
     */
    public Application() {
        this.pcs = new PropertyChangeSupport(this);

        this.assetsAndLiabilities = new AssetsAndLiabilities();
        this.assetsAndLiabilities.add(this);

        this.housingExpense = new HousingExpense();
        this.housingExpense.add(this);

        this.property = new Property();
        this.property.add(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public final void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        final Application that = (Application)obj;
        return (Util.equals(this.amortizationType, that.amortizationType) && Util.equals(this.amount, that.amount)
                        && Util.equals(this.assetsAndLiabilities, that.assetsAndLiabilities)
                        && Util.equals(this.borrowers, that.borrowers)
                        && Util.equals(this.description, that.description)
                        && Util.equals(this.downPaymentSource, that.downPaymentSource)
                        && Util.equals(this.housingExpense, that.housingExpense)
                        && Util.equals(this.numMonths, that.numMonths)
                        && Util.equals(this.property, that.property)
                        && Util.equals(this.purchaseType, that.purchaseType)
                        && Util.equals(this.rate, that.rate) && Util.equals(this.type, that.type));
    }

    protected final void firePropertyChange( final String name,
                                             final Object oldValue,
                                             final Object newValue ) {
        if (oldValue == newValue) {
            return;
        }

        if ((oldValue != null) && oldValue.equals(newValue)) {
            return;
        }

        this.pcs.firePropertyChange(name, oldValue, newValue);
    }

    /**
     * @return the amortization type (can be <code>null</code> or empty)
     */
    public String getAmoritizationType() {
        return this.amortizationType;
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        if (this.amount == null) {
            return 0;
        }

        return this.amount.doubleValue();
    }

    /**
     * @return the assets and liabilities (can be <code>null</code>)
     */
    public AssetsAndLiabilities getAssetsAndLiabilities() {
        return this.assetsAndLiabilities;
    }

    /**
     * @return the borrowers (never <code>null</code>)
     */
    public List<Borrower> getBorrowers() {
        if (this.borrowers == null) {
            return Borrower.NONE;
        }

        return this.borrowers;
    }

    /**
     * @return the description (can be <code>null</code> or empty)
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @return the down payment source (can be <code>null</code> or empty)
     */
    public String getDownPaymentSource() {
        return this.downPaymentSource;
    }

    /**
     * @return the housing expense (can be <code>null</code>)
     */
    public HousingExpense getHousingExpense() {
        return this.housingExpense;
    }

    /**
     * @return the number of months
     */
    public int getNumMonths() {
        return this.numMonths;
    }

    /**
     * @return the property (can be <code>null</code>)
     */
    public Property getProperty() {
        return this.property;
    }

    /**
     * @return the purchase type (can be <code>null</code> or empty)
     */
    public String getPurchaseType() {
        return this.purchaseType;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        if (this.rate == null) {
            return 0;
        }

        return this.rate.doubleValue();
    }

    /**
     * @return the type (can be <code>null</code> or empty)
     */
    public String getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.amortizationType, this.amount, this.assetsAndLiabilities,
                                             this.borrowers, this.description, this.downPaymentSource,
                                             this.housingExpense, this.numMonths, this.property, this.purchaseType,
                                             this.rate, this.type});
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        final Object source = event.getSource();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();

        if (source instanceof AssetsAndLiabilities) {
            firePropertyChange(Properties.ASSETS_LIABILITIES, oldValue, newValue);
        } else if (source instanceof HousingExpense) {
            firePropertyChange(Properties.HOUSING_EXPENSE, oldValue, newValue);
        } else if (source instanceof Property) {
            firePropertyChange(Properties.PROPERTY, oldValue, newValue);
        }
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public final void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newAmortizationType the new value for the amortization type (can be <code>null</code> or empty)
     */
    public void setAmortizationType( final String newAmortizationType ) {
        if (!Util.equals(this.amortizationType, newAmortizationType)) {
            final Object oldValue = this.amortizationType;
            this.amortizationType = newAmortizationType;
            firePropertyChange(Properties.AMORTIZATION_TYPE, oldValue, this.amortizationType);
        }
    }

    /**
     * @param newAssetsAndLiabilities the new value for the assets and liabilities (can be <code>null</code>)
     */
    public void setAssetsAndLiabilities( final AssetsAndLiabilities newAssetsAndLiabilities ) {
        if (!Util.equals(this.assetsAndLiabilities, newAssetsAndLiabilities)) {
            final AssetsAndLiabilities oldValue = this.assetsAndLiabilities;
            this.assetsAndLiabilities = newAssetsAndLiabilities;
            firePropertyChange(Properties.ASSETS_LIABILITIES, oldValue, this.assetsAndLiabilities);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.assetsAndLiabilities != null) {
                this.assetsAndLiabilities.add(this);
            }
        }
    }

    /**
     * @param newBorrowers the new value for the borrower collection (can be <code>null</code>)
     */
    public void setBorrowers( final List<Borrower> newBorrowers ) {
        if (!Util.equals(this.borrowers, newBorrowers)) {
            final Object oldValue = this.borrowers;

            if ((newBorrowers != null) && newBorrowers.isEmpty()) {
                this.borrowers = null;
            } else {
                this.borrowers = newBorrowers;
            }

            firePropertyChange(Properties.BORROWERS, oldValue, this.borrowers);
        }
    }

    /**
     * @param newDescription the new value for the description (can be <code>null</code> or empty)
     */
    public void setDescription( final String newDescription ) {
        if (!Util.equals(this.description, newDescription)) {
            final Object oldValue = this.description;
            this.description = newDescription;
            firePropertyChange(Properties.DESCRIPTION, oldValue, this.description);
        }
    }

    /**
     * @param newDownPaymentSource the new value for the down payment source (can be <code>null</code> or empty)
     */
    public void setDownPaymentSource( final String newDownPaymentSource ) {
        if (!Util.equals(this.downPaymentSource, newDownPaymentSource)) {
            final Object oldValue = this.downPaymentSource;
            this.downPaymentSource = newDownPaymentSource;
            firePropertyChange(Properties.DOWN_PAYMENT_SOURCE, oldValue, this.downPaymentSource);
        }
    }

    /**
     * @param newHousingExpense the new value for the housing expense (can be <code>null</code>)
     */
    public void setHousingExpense( final HousingExpense newHousingExpense ) {
        if (!Util.equals(this.housingExpense, newHousingExpense)) {
            final HousingExpense oldValue = this.housingExpense;
            this.housingExpense = newHousingExpense;
            firePropertyChange(Properties.HOUSING_EXPENSE, oldValue, this.housingExpense);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.housingExpense != null) {
                this.housingExpense.add(this);
            }
        }
    }

    /**
     * @param newRate the new value for the loan interest rate
     */
    public void setInterestRate( final double newRate ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.rate == null) && (newRate != 0)) {
            changed = true;
            oldValue = this.rate;
            this.rate = new BigDecimal(newRate);
            this.rate.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.rate != null) && (this.rate.doubleValue() != newRate)) {
            changed = true;
            oldValue = this.rate;

            if (newRate == 0) {
                this.rate = null;
            } else {
                this.rate = new BigDecimal(newRate);
                this.rate.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.RATE, oldValue, this.rate);
        }
    }

    /**
     * @param newAmount the new value for the loan amount
     */
    public void setLoanAmount( final double newAmount ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.amount == null) && (newAmount != 0)) {
            changed = true;
            oldValue = this.amount;
            this.amount = new BigDecimal(newAmount);
            this.amount.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.amount != null) && (this.amount.doubleValue() != newAmount)) {
            changed = true;
            oldValue = this.amount;

            if (newAmount == 0) {
                this.amount = null;
            } else {
                this.amount = new BigDecimal(newAmount);
                this.amount.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.AMOUNT, oldValue, this.amount);
        }
    }

    /**
     * @param newNumMonths the new value for the number of months
     */
    public void setNumberOfMonths( final int newNumMonths ) {
        if (this.numMonths != newNumMonths) {
            final Object oldValue = this.numMonths;
            this.numMonths = newNumMonths;
            firePropertyChange(Properties.NUM_MONTHS, oldValue, this.numMonths);
        }
    }

    /**
     * @param newProperty the new value for the property (can be <code>null</code>)
     */
    public void setProperty( final Property newProperty ) {
        if (!Util.equals(this.property, newProperty)) {
            final Property oldValue = this.property;
            this.property = newProperty;
            firePropertyChange(Properties.PROPERTY, oldValue, this.property);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.property != null) {
                this.property.add(this);
            }
        }
    }

    /**
     * @param newPurchaseType the new value for the purchase type (can be <code>null</code> or empty)
     */
    public void setPurchaseType( final String newPurchaseType ) {
        if (!Util.equals(this.purchaseType, newPurchaseType)) {
            final Object oldValue = this.purchaseType;
            this.purchaseType = newPurchaseType;
            firePropertyChange(Properties.PURCHASE_TYPE, oldValue, this.purchaseType);
        }
    }

    /**
     * @param newType the new value for the type (can be <code>null</code> or empty)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            final Object oldValue = this.type;
            this.type = newType;
            firePropertyChange(Properties.TYPE, oldValue, this.type);
        }
    }

    /**
     * An application's property identifiers.
     */
    public interface Properties {

        /**
         * The application's amortization type property identifier.
         */
        String AMORTIZATION_TYPE = PROPERTY_PREFIX + "amortization_type"; //$NON-NLS-1$

        /**
         * The application's loan amount property identifier.
         */
        String AMOUNT = PROPERTY_PREFIX + "amount"; //$NON-NLS-1$

        /**
         * The application's assets and liabilities property identifier.
         */
        String ASSETS_LIABILITIES = PROPERTY_PREFIX + "assets_liabilities"; //$NON-NLS-1$

        /**
         * The application's borrowers property identifier.
         */
        String BORROWERS = PROPERTY_PREFIX + "borrowers"; //$NON-NLS-1$

        /**
         * The application's description property identifier.
         */
        String DESCRIPTION = PROPERTY_PREFIX + "description"; //$NON-NLS-1$

        /**
         * The application's down payment source property identifier.
         */
        String DOWN_PAYMENT_SOURCE = PROPERTY_PREFIX + "down_payment_source"; //$NON-NLS-1$

        /**
         * The application's housing expense property identifier.
         */
        String HOUSING_EXPENSE = PROPERTY_PREFIX + "housing_expense"; //$NON-NLS-1$

        /**
         * The application's number of months of the loan property identifier.
         */
        String NUM_MONTHS = PROPERTY_PREFIX + "num_months"; //$NON-NLS-1$

        /**
         * The application's property property identifier.
         */
        String PROPERTY = PROPERTY_PREFIX + "property"; //$NON-NLS-1$

        /**
         * The application's purchase type property identifier.
         */
        String PURCHASE_TYPE = PROPERTY_PREFIX + "purchase_type"; //$NON-NLS-1$

        /**
         * The application's loan rate property identifier.
         */
        String RATE = PROPERTY_PREFIX + "rate"; //$NON-NLS-1$

        /**
         * The application's type property identifier.
         */
        String TYPE = PROPERTY_PREFIX + "type"; //$NON-NLS-1$

    }

}
