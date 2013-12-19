/*
 * Copyright 2013 JBoss Inc
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A loan application model object.
 */
public final class Application {

    /**
     * @param original the application being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Application copy( final Application original ) {
        final Application copy = new Application();

        copy.setAmoritizationType(original.getAmoritizationType());
        copy.setAmount(original.getAmount());
        copy.setAssetsAndLiabilities(AssetsAndLiabilities.copy(original.getAssetsAndLiabilities()));
        copy.setBorrowers(original.getBorrowers());
        copy.setDescription(original.getDescription());
        copy.setDownPaymentSource(original.getDownPaymentSource());
        copy.setHousingExpense(original.getHousingExpense());
        copy.setNumMonths(original.getNumMonths());
        copy.setProperty(original.getProperty());
        copy.setPurchaseType(original.getPurchaseType());
        copy.setRate(original.getRate());
        copy.setType(original.getType());

        return copy;
    }

    private AmortizationType amoritizationType;
    private BigDecimal amount; // n.xx
    private AssetsAndLiabilities assetsAndLiabilities;
    private List<Borrower> borrowers; // need at least one
    private String description; // max 1000
    private String downPaymentSource; // max 1000
    private HousingExpense housingExpense;
    private int numMonths = 0; // positive
    private Property property;
    private PurchaseType purchaseType;
    private BigDecimal rate; // n.xxxx
    private Type type;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final Application that = (Application)obj;
        return (Util.equals(this.amoritizationType, that.amoritizationType) && Util.equals(this.amount, that.amount)
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

    /**
     * @return the amortization type (can be <code>null</code>)
     */
    public AmortizationType getAmoritizationType() {
        return this.amoritizationType;
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
     * @return the purchase type (can be <code>null</code>)
     */
    public PurchaseType getPurchaseType() {
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
     * @return the type (can be <code>null</code>)
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.amoritizationType, this.amount, this.assetsAndLiabilities,
                                             this.borrowers, this.description, this.downPaymentSource,
                                             this.housingExpense, this.numMonths, this.property, this.purchaseType,
                                             this.rate, this.type});
    }

    /**
     * @param newAmoritizationType the new value for the amoritizationType
     */
    public void setAmoritizationType( final AmortizationType newAmoritizationType ) {
        if (!Util.equals(this.amoritizationType, newAmoritizationType)) {
            this.amoritizationType = newAmoritizationType;
        }
    }

    /**
     * @param newAmount the new value for the amount
     */
    public void setAmount( final double newAmount ) {
        if ((this.amount == null) || (this.amount.doubleValue() != newAmount)) {
            this.amount = new BigDecimal(newAmount);
            this.amount.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newAssetsAndLiabilities the new value for the assetsAndLiabilities
     */
    public void setAssetsAndLiabilities( final AssetsAndLiabilities newAssetsAndLiabilities ) {
        if (!Util.equals(this.assetsAndLiabilities, newAssetsAndLiabilities)) {
            this.assetsAndLiabilities = newAssetsAndLiabilities;
        }
    }

    /**
     * @param newBorrowers the new value for the borrowers
     */
    public void setBorrowers( final List<Borrower> newBorrowers ) {
        if (!Util.equals(this.borrowers, newBorrowers)) {
            if ((newBorrowers != null) && newBorrowers.isEmpty()) {
                this.borrowers = null;
            } else {
                this.borrowers = newBorrowers;
            }
        }
    }

    /**
     * @param newDescription the new value for the description
     */
    public void setDescription( final String newDescription ) {
        if (!Util.equals(this.description, newDescription)) {
            this.description = newDescription;
        }
    }

    /**
     * @param newDownPaymentSource the new value for the downPaymentSource
     */
    public void setDownPaymentSource( final String newDownPaymentSource ) {
        if (!Util.equals(this.downPaymentSource, newDownPaymentSource)) {
            this.downPaymentSource = newDownPaymentSource;
        }
    }

    /**
     * @param newHousingExpense the new value for the housingExpense
     */
    public void setHousingExpense( final HousingExpense newHousingExpense ) {
        if (!Util.equals(this.housingExpense, newHousingExpense)) {
            this.housingExpense = newHousingExpense;
        }
    }

    /**
     * @param newNumMonths the new value for the numMonths
     */
    public void setNumMonths( final int newNumMonths ) {
        if (this.numMonths != newNumMonths) {
            this.numMonths = newNumMonths;
        }
    }

    /**
     * @param newProperty the new value for the property
     */
    public void setProperty( final Property newProperty ) {
        if (!Util.equals(this.property, newProperty)) {
            this.property = newProperty;
        }
    }

    /**
     * @param newPurchaseType the new value for the purchaseType
     */
    public void setPurchaseType( final PurchaseType newPurchaseType ) {
        if (!Util.equals(this.purchaseType, newPurchaseType)) {
            this.purchaseType = newPurchaseType;
        }
    }

    /**
     * @param newRate the new value for the rate
     */
    public void setRate( final double newRate ) {
        if ((this.rate == null) || (this.rate.doubleValue() != newRate)) {
            this.rate = new BigDecimal(newRate);
            this.rate.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newType the new value for the type
     */
    public void setType( final Type newType ) {
        if (!Util.equals(this.type, newType)) {
            this.type = newType;
        }
    }

    /**
     * The loan financing type.
     */
    public enum AmortizationType {

        /**
         * A fixed rate loan.
         */
        FIXED_RATE("Fixed_Rate"), //$NON-NLS-1$

        /**
         * A graduated payment loan.
         */
        GPM("GPM"), //$NON-NLS-1$

        /**
         * A miscellaneous type of loan.
         */
        OTHER("Other"), //$NON-NLS-1$

        /**
         * An adjustable rate loan.
         */
        ARM("ARM"); //$NON-NLS-1$

        private final String value;

        private AmortizationType( final String enumValue ) {
            this.value = enumValue;
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * The purchase type.
     */
    public enum PurchaseType {

        /**
         * An application to purchase a property.
         */
        PURCHASE("Purchase"), //$NON-NLS-1$

        /**
         * An application to refinance a property.
         */
        REFINANCE("Refinance"), //$NON-NLS-1$

        /**
         * An application for construction.
         */
        CONSTRUCTION("Construction"), //$NON-NLS-1$

        /**
         * An application for construction of a permanent structure property.
         */
        CONSTRUCTION_PERMANENT("Construction_Permanent"), //$NON-NLS-1$

        /**
         * An application for a miscellaneous purchase type.
         */
        OTHER("Other"); //$NON-NLS-1$

        private final String value;

        private PurchaseType( final String enumValue ) {
            this.value = enumValue;
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

    /**
     * The type of application.
     */
    public enum Type {

        /**
         * A conventional loan application.
         */
        CONVENTIONAL("Conventional"), //$NON-NLS-1$

        /**
         * An FHA loan application.
         */
        FHA("FHA"), //$NON-NLS-1$

        /**
         * A VA loan application.
         */
        VA("VA"), //$NON-NLS-1$

        /**
         * An FmHA loan application.
         */
        FMHA("FmHA"), //$NON-NLS-1$

        /**
         * The loan application does not have a common financing type.
         */
        OTHER("Other"); //$NON-NLS-1$

        private final String value;

        private Type( final String enumValue ) {
            this.value = enumValue;
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

}
