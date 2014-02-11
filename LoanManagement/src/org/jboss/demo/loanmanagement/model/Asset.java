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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import org.jboss.demo.loanmanagement.Util;
import android.text.TextUtils;

/**
 * An asset model object.
 * 
 * @param <T> the asset type
 */
public abstract class Asset<T> implements ModelObject<T> {

    protected static final String PROPERTY_PREFIX = Asset.class.getSimpleName() + '.';

    private BigDecimal amount;
    private String description;
    private final PropertyChangeSupport pcs;

    /**
     * Constructs an asset.
     */
    public Asset() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
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

        @SuppressWarnings( "unchecked" )
        final Asset<T> that = (Asset<T>)obj;
        return (Util.equals(this.amount, that.amount) && Util.equals(this.description, that.description));
    }

    protected void firePropertyChange( final String name,
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
     * @return the amount
     */
    public double getAmount() {
        if (this.amount == null) {
            return 0;
        }

        return this.amount.doubleValue();
    }

    /**
     * @return the description (can be <code>null</code> or empty)
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.amount, this.description});
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newAmount the new value for the amount
     */
    public void setAmount( final double newAmount ) {
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
     * @param newDescription the new value for the description (can be <code>null</code> or empty)
     */
    public void setDescription( final String newDescription ) {
        String change = newDescription;

        if (change != null) {
            change = change.trim();
        }

        if (!TextUtils.equals(this.description, change)) {
            final Object oldValue = this.description;
            this.description = change;
            firePropertyChange(Properties.DESCRIPTION, oldValue, this.amount);
        }
    }

    /**
     * An asset's property identifiers
     */
    public interface Properties {

        /**
         * The asset's amount property identifier.
         */
        String AMOUNT = PROPERTY_PREFIX + "amount"; //$NON-NLS-1$

        /**
         * The asset's description property identifier.
         */
        String DESCRIPTION = PROPERTY_PREFIX + "desription"; //$NON-NLS-1$

    }

}
