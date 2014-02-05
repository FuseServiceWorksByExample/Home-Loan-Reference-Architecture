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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower's assets and liabilities model object.
 */
public final class AssetsAndLiabilities implements ModelObject<AssetsAndLiabilities>, PropertyChangeListener {

    /**
     * Indicates who was present when the assets and liabilities were declared.
     */
    public static final String[] COMPLETED_BY = new String[] {"Jointly", "Not_Jointly"}; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The {@link #COMPLETED_BY} index for the completed by type for jointly.
     */
    public static final int JOINTLY_INDEX = 0;

    /**
     * The {@link #COMPLETED_BY} index for the completed by type for not-jointly.
     */
    public static final int NOT_JOINTLY_INDEX = 1;

    protected static final String PROPERTY_PREFIX = AssetsAndLiabilities.class.getSimpleName() + '.';

    private List<Account> accounts = null;
    private List<Automobile> autos = null;
    private List<CashDeposit> cashDeposits = null;
    private String completedType = null;
    private final PropertyChangeSupport pcs;

    /**
     * Constructs an assets and liabilities.
     */
    public AssetsAndLiabilities() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param newAccount the account being added (cannot be <code>null</code>)
     */
    public void addAccount( final Account newAccount ) {
        if (newAccount == null) {
            throw new NullPointerException();
        }

        if (getAccounts().add(newAccount)) {
            newAccount.add(this);
            firePropertyChange(Properties.ACCOUNTS, null, newAccount);
        }
    }

    /**
     * @param newAutomobile the automobile being added (cannot be <code>null</code>)
     */
    public void addAutomobile( final Automobile newAutomobile ) {
        if (newAutomobile == null) {
            throw new NullPointerException();
        }

        if (getAutomobiles().add(newAutomobile)) {
            newAutomobile.add(this);
            firePropertyChange(Properties.AUTOS, null, newAutomobile);
        }
    }

    /**
     * @param newCashDeposit the cash deposit being added (cannot be <code>null</code>)
     */
    public void addCashDeposit( final CashDeposit newCashDeposit ) {
        if (newCashDeposit == null) {
            throw new NullPointerException();
        }

        if (getCashDeposits().add(newCashDeposit)) {
            newCashDeposit.add(this);
            firePropertyChange(Properties.CASH_DEPOSITS, null, newCashDeposit);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public AssetsAndLiabilities copy() {
        final AssetsAndLiabilities copy = new AssetsAndLiabilities();
        copy.setCompletedType(getCompletedType());

        // accounts
        if ((this.accounts != null) && !this.accounts.isEmpty()) {
            for (final Account account : getAccounts()) {
                copy.addAccount(account.copy());
            }
        }

        // autos
        if ((this.autos != null) && !this.autos.isEmpty()) {
            for (final Automobile auto : getAutomobiles()) {
                copy.addAutomobile(auto.copy());
            }
        }

        // cash deposit
        if ((this.cashDeposits != null) && !this.cashDeposits.isEmpty()) {
            for (final CashDeposit cashDeposit : getCashDeposits()) {
                copy.addCashDeposit(cashDeposit.copy());
            }
        }

        return copy;
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

        final AssetsAndLiabilities that = (AssetsAndLiabilities)obj;
        return (Util.equals(this.accounts, that.accounts) && Util.equals(this.autos, that.autos)
                        && Util.equals(this.completedType, that.completedType) && Util.equals(this.cashDeposits,
                                                                                              that.cashDeposits));
    }

    private void firePropertyChange( final String name,
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
     * Use {@link #addAccount(Account)} and {@link #removeAccount(Account)} when adding and removing accounts.
     * 
     * @return the accounts (never <code>null</code>)
     */
    public List<Account> getAccounts() {
        if (this.accounts == null) {
            this.accounts = new ArrayList<Account>();
        }

        return this.accounts;
    }

    /**
     * Use {@link #addAutomobile(Automobile)} and {@link #removeAutomobile(Automobile)} when adding and removing
     * automobiles.
     * 
     * @return the automobile assets (never <code>null</code>)
     */
    public List<Automobile> getAutomobiles() {
        if (this.autos == null) {
            this.autos = new ArrayList<Automobile>();
        }

        return this.autos;
    }

    /**
     * Use {@link #addCashDeposit(CashDeposit)} and {@link #removeCashDeposit(CashDeposit)} when adding and removing
     * deposits.
     * 
     * @return the cash deposits (never <code>null</code>)
     */
    public List<CashDeposit> getCashDeposits() {
        if (this.cashDeposits == null) {
            this.cashDeposits = new ArrayList<CashDeposit>();
        }

        return this.cashDeposits;
    }

    /**
     * @return the completed type (can be <code>null</code>)
     */
    public String getCompletedType() {
        return this.completedType;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.accounts, this.autos, this.completedType, this.cashDeposits});
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        final Object source = event.getSource();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();

        if (source instanceof Account) {
            firePropertyChange(Properties.ACCOUNTS, oldValue, newValue);
        } else if (source instanceof Automobile) {
            firePropertyChange(Properties.AUTOS, oldValue, newValue);
        } else {
            firePropertyChange(Properties.CASH_DEPOSITS, oldValue, newValue);
        }
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param removeAccount the account being deleted (cannot be <code>null</code>)
     */
    public void removeAccount( final Account removeAccount ) {
        if (removeAccount == null) {
            throw new NullPointerException();
        }

        if ((this.accounts != null) && this.accounts.remove(removeAccount)) {
            removeAccount.remove(this);
            firePropertyChange(Properties.ACCOUNTS, removeAccount, null);
        }
    }

    /**
     * @param removeAuto the automobile being deleted (cannot be <code>null</code>)
     */
    public void removeAutomobile( final Automobile removeAuto ) {
        if (removeAuto == null) {
            throw new NullPointerException();
        }

        if ((this.autos != null) && this.autos.remove(removeAuto)) {
            removeAuto.remove(this);
            firePropertyChange(Properties.AUTOS, removeAuto, null);
        }
    }

    /**
     * @param removeDeposit the cash deposit being deleted (cannot be <code>null</code>)
     */
    public void removeCashDeposit( final CashDeposit removeDeposit ) {
        if (removeDeposit == null) {
            throw new NullPointerException();
        }

        if ((this.cashDeposits != null) && this.cashDeposits.remove(removeDeposit)) {
            removeDeposit.remove(this);
            firePropertyChange(Properties.CASH_DEPOSITS, removeDeposit, null);
        }
    }

    /**
     * @param newCompletedType the new value for the completed type (can be <code>null</code>)
     */
    public void setCompletedType( final String newCompletedType ) {
        if (!Util.equals(this.completedType, newCompletedType)) {
            final Object oldValue = this.completedType;
            this.completedType = newCompletedType;
            firePropertyChange(Properties.COMPLETED_TYPE, oldValue, this.completedType);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final AssetsAndLiabilities from ) {
        setCompletedType(from.getCompletedType());

        { // accounts
            final List<Account> oldValue = this.accounts;

            if ((oldValue != null) && !oldValue.isEmpty()) {
                // unregister
                for (final Account account : oldValue) {
                    account.remove(this);
                }

                oldValue.clear();
            }

            if ((from.accounts == null) || from.accounts.isEmpty()) {
                if ((oldValue != null) && !oldValue.isEmpty()) {
                    this.accounts = null;
                    firePropertyChange(Properties.ACCOUNTS, oldValue, null);
                }
            } else {
                for (final Account account : from.getAccounts()) {
                    addAccount(account.copy());
                }
            }
        }

        { // autos
            final List<Automobile> oldValue = this.autos;

            if ((oldValue != null) && !oldValue.isEmpty()) {
                // unregister
                for (final Automobile auto : oldValue) {
                    auto.remove(this);
                }

                oldValue.clear();
            }

            if ((from.autos == null) || from.autos.isEmpty()) {
                if ((oldValue != null) && !oldValue.isEmpty()) {
                    this.autos = null;
                    firePropertyChange(Properties.AUTOS, oldValue, null);
                }
            } else {
                for (final Automobile auto : from.getAutomobiles()) {
                    addAutomobile(auto.copy());
                }
            }
        }

        { // cash deposit
            final List<CashDeposit> oldValue = this.cashDeposits;

            if ((oldValue != null) && !oldValue.isEmpty()) {
                // unregister
                for (final CashDeposit deposit : oldValue) {
                    deposit.remove(this);
                }

                oldValue.clear();
            }

            if ((from.cashDeposits == null) || from.cashDeposits.isEmpty()) {
                if ((oldValue != null) && !oldValue.isEmpty()) {
                    this.cashDeposits = null;
                    firePropertyChange(Properties.CASH_DEPOSITS, oldValue, null);
                }
            } else {
                for (final CashDeposit cashDeposit : from.getCashDeposits()) {
                    addCashDeposit(cashDeposit.copy());
                }
            }
        }
    }

    /**
     * An assets and liabilities' identifiers.
     */
    public interface Properties {

        /**
         * The assets and liabilities' account collection property identifier.
         */
        String ACCOUNTS = PROPERTY_PREFIX + "accounts"; //$NON-NLS-1$

        /**
         * The assets and liabilities' automobile collection property identifier.
         */
        String AUTOS = PROPERTY_PREFIX + "automobiles"; //$NON-NLS-1$

        /**
         * The assets and liabilities' cash deposit collection property identifier.
         */
        String CASH_DEPOSITS = PROPERTY_PREFIX + "cash_deposits"; //$NON-NLS-1$

        /**
         * The assets and liabilities' completed by type property identifier.
         */
        String COMPLETED_TYPE = PROPERTY_PREFIX + "completed_type"; //$NON-NLS-1$

    }

}
