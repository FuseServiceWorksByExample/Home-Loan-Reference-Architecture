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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower's assets and liabilities model object.
 */
public final class AssetsAndLiabilities {

    /**
     * @param original the assets and liabilities being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static AssetsAndLiabilities copy( final AssetsAndLiabilities original ) {
        final AssetsAndLiabilities copy = new AssetsAndLiabilities();

        copy.setCompletedType(original.getCompletedType());
        copy.setHousingExpense(HousingExpense.copy(original.getHousingExpense()));

        // accounts
        if (!original.getAccounts().isEmpty()) {
            for (final Account account : original.getAccounts()) {
                copy.addAccount(Account.copy(account));
            }
        }

        // autos
        if (!original.getAutomobiles().isEmpty()) {
            for (final Automobile auto : original.getAutomobiles()) {
                copy.addAutomobile(Automobile.copy(auto));
            }
        }

        // cash deposit
        if (!original.getCashDeposits().isEmpty()) {
            for (final CashDeposit cashDeposit : original.getCashDeposits()) {
                copy.addCashDeposit(CashDeposit.copy(cashDeposit));
            }
        }

        return copy;
    }

    private List<Account> accounts = null;
    private List<Automobile> autos = null;
    private CompletedType completedType = null;
    private List<CashDeposit> cashDeposits = null;
    private HousingExpense housingExpense = null;

    /**
     * @param newAccount the account being added (cannot be <code>null</code>)
     */
    public void addAccount( final Account newAccount ) {
        if (newAccount == null) {
            throw new NullPointerException();
        }

        if (this.accounts == null) {
            this.accounts = new ArrayList<Account>();
        }

        this.accounts.add(newAccount);
    }

    /**
     * @param newAutomobile the automobile being added (cannot be <code>null</code>)
     */
    public void addAutomobile( final Automobile newAutomobile ) {
        if (newAutomobile == null) {
            throw new NullPointerException();
        }

        if (this.autos == null) {
            this.autos = new ArrayList<Automobile>();
        }

        this.autos.add(newAutomobile);
    }

    /**
     * @param newCashDeposit the cash deposit being added (cannot be <code>null</code>)
     */
    public void addCashDeposit( final CashDeposit newCashDeposit ) {
        if (newCashDeposit == null) {
            throw new NullPointerException();
        }

        if (this.cashDeposits == null) {
            this.cashDeposits = new ArrayList<CashDeposit>();
        }

        this.cashDeposits.add(newCashDeposit);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final AssetsAndLiabilities that = (AssetsAndLiabilities)obj;
        return (Util.equals(this.accounts, that.accounts) && Util.equals(this.autos, that.autos)
                        && Util.equals(this.completedType, that.completedType)
                        && Util.equals(this.cashDeposits, that.cashDeposits) && Util.equals(this.housingExpense,
                                                                                            that.housingExpense));
    }

    /**
     * @return the cash deposits (never <code>null</code>)
     */
    public List<Account> getAccounts() {
        if (this.accounts == null) {
            return Account.NONE;
        }

        return this.accounts;
    }

    /**
     * @return the automobile assets (never <code>null</code>)
     */
    public List<Automobile> getAutomobiles() {
        if (this.autos == null) {
            return Automobile.NONE;
        }

        return this.autos;
    }

    /**
     * @return the cash deposits (never <code>null</code>)
     */
    public List<CashDeposit> getCashDeposits() {
        if (this.cashDeposits == null) {
            return CashDeposit.NONE;
        }

        return this.cashDeposits;
    }

    /**
     * @return the completed type (can be <code>null</code>)
     */
    public CompletedType getCompletedType() {
        return this.completedType;
    }

    /**
     * @return the housing expense (can be <code>null</code>)
     */
    public HousingExpense getHousingExpense() {
        return this.housingExpense;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.accounts, this.autos, this.completedType, this.cashDeposits,
                                             this.housingExpense});
    }

    /**
     * @param newCompletedType the new value for the completed type (can be <code>null</code>)
     */
    public void setCompletedType( final CompletedType newCompletedType ) {
        if (this.completedType != newCompletedType) {
            this.completedType = newCompletedType;
        }
    }

    /**
     * @param newHousingExpense the new housing expense (can be <code>null</code>)
     */
    public void setHousingExpense( final HousingExpense newHousingExpense ) {
        if (this.housingExpense == null) {
            if (newHousingExpense != null) {
                this.housingExpense = newHousingExpense;
            }
        } else if ((newHousingExpense == null) || !this.housingExpense.equals(newHousingExpense)) {
            this.housingExpense = newHousingExpense;
        }
    }

    /**
     * Indicates who was present when the assets and liabilities were declared.
     */
    public enum CompletedType {

        /**
         * The assets and liabilities were completed jointly.
         */
        JOINTLY("Jointly"), //$NON-NLS-1$

        /**
         * The assets and liabilities were <em>not</em> completed jointly.
         */
        NOT_JOINTLY("Not_Jointly"); //$NON-NLS-1$

        private final String value;

        private CompletedType( final String enumValue ) {
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
