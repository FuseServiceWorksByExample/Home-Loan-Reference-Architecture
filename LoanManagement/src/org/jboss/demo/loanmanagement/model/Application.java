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
     * The loan amortization types.
     */
    public static final String[] AMORTIZATION_TYPES = new String[] {"Fixed_Rate", "GPM", "ARM", "Other"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    /**
     * The loan types.
     */
    public static final String[] LOAN_TYPES = new String[] {"Conventional", "FHA", "VA", "FmHA", "Other"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

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
    private Property property;
    private String purchaseType;
    private BigDecimal rate; // n.xxxx
    private String type;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
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
     * @param newAmortizationType the new value for the amortizationType
     */
    public void setAmortizationType( final String newAmortizationType ) {
        if (!Util.equals(this.amortizationType, newAmortizationType)) {
            this.amortizationType = newAmortizationType;
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
     * @param newRate the new value for the loan interest rate
     */
    public void setInterestRate( final double newRate ) {
        if ((this.rate == null) || (this.rate.doubleValue() != newRate)) {
            this.rate = new BigDecimal(newRate);
            this.rate.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newAmount the new value for the loan amount
     */
    public void setLoanAmount( final double newAmount ) {
        if ((this.amount == null) || (this.amount.doubleValue() != newAmount)) {
            this.amount = new BigDecimal(newAmount);
            this.amount.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newNumMonths the new value for the numMonths
     */
    public void setNumberOfMonths( final int newNumMonths ) {
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
    public void setPurchaseType( final String newPurchaseType ) {
        if (!Util.equals(this.purchaseType, newPurchaseType)) {
            this.purchaseType = newPurchaseType;
        }
    }

    /**
     * @param newType the new value for the type
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            this.type = newType;
        }
    }

}
