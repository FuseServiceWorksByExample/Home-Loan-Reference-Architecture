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
import java.util.Arrays;
import org.jboss.demo.loanmanagement.Util;
import android.text.TextUtils;

/**
 * A savings or checking account model object.
 */
public final class Account extends Asset<Account> implements PropertyChangeListener {

    protected static final String PREFIX = Account.class.getSimpleName() + '.';

    private Address address;
    private String number; // max 100
    private final PropertyChangeSupport pcs;

    /**
     * Constructs an account.
     */
    public Account() {
        this.pcs = new PropertyChangeSupport(this);
        super.add(this);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Asset#add(java.beans.PropertyChangeListener)
     */
    @Override
    public final void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public Account copy() {
        final Account copy = new Account();

        copy.setAmount(getAmount());
        copy.setDescription(getDescription());
        copy.setNumber(getNumber());

        if (getAddress() != null) {
            copy.setAddress(getAddress().copy());
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

        final Account that = (Account)obj;
        return (Util.equals(this.number, that.number) && Util.equals(this.address, that.address));
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Asset#firePropertyChange(java.lang.String, java.lang.Object,
     *      java.lang.Object)
     */
    @Override
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
     * @return the address (can be <code>null</code>)
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @return the account name (can be <code>null</code> or empty)
     */
    public String getName() {
        return getDescription();
    }

    /**
     * @return the account number (can be <code>null</code> or empty)
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.number, this.address});
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        if (Asset.Properties.AMOUNT.equals(event.getPropertyName())) {
            firePropertyChange(Properties.AMOUNT, event.getOldValue(), event.getNewValue());
        } else if (Asset.Properties.DESCRIPTION.equals(event.getPropertyName())) {
            firePropertyChange(Properties.NAME, event.getOldValue(), event.getNewValue());
        } else {
            firePropertyChange(Properties.ADDRESS, event.getOldValue(), event.getNewValue());
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Asset#remove(java.beans.PropertyChangeListener)
     */
    @Override
    public final void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newAddress the new value for the address
     */
    public void setAddress( final Address newAddress ) {
        if (!Util.equals(this.address, newAddress)) {
            final Object oldValue = this.address;
            this.address = newAddress;
            firePropertyChange(Properties.ADDRESS, oldValue, this.address);

            if (oldValue != null) {
                ((Address)oldValue).remove(this);
            }

            if (this.address != null) {
                this.address.add(this);
            }
        }
    }

    /**
     * @param newName the new value for the name (can be <code>null</code> or empty)
     */
    public void setName( final String newName ) {
        setDescription(newName);
    }

    /**
     * @param newNumber the new value for the account number
     */
    public void setNumber( final String newNumber ) {
        String change = newNumber;

        if (change != null) {
            change = change.trim();
        }

        if (!TextUtils.equals(this.number, change)) {
            final Object oldValue = this.number;
            this.number = change;
            firePropertyChange(Properties.NUMBER, oldValue, this.number);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final Account from ) {
        setAmount(from.getAmount());
        setDescription(from.getDescription());
        setNumber(from.getNumber());

        { // address
            final Address oldValue = getAddress();

            if (from.getAddress() == null) {
                this.address = null;
                firePropertyChange(Properties.ADDRESS, oldValue, null);
            } else {
                setAddress(from.getAddress().copy());
            }
        }
    }

    /**
     * An account's property identifiers
     */
    public interface Properties {

        /**
         * The account's address property identifier.
         */
        String ADDRESS = PREFIX + "address"; //$NON-NLS-1$

        /**
         * The account's amount property identifier.
         */
        String AMOUNT = PREFIX + "amount"; //$NON-NLS-1$

        /**
         * The account's name and description property identifier.
         */
        String NAME = PREFIX + "name"; //$NON-NLS-1$

        /**
         * The account's number property identifier.
         */
        String NUMBER = PREFIX + "number"; //$NON-NLS-1$

    }

}
