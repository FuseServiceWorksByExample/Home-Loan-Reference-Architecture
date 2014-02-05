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
import java.util.Collections;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;
import android.text.TextUtils;

/**
 * A borrower's employer.
 */
public final class Employer implements ModelObject<Employer>, PropertyChangeListener {

    /**
     * An empty collection of employers.
     */
    static final List<Employer> NONE = Collections.emptyList();

    protected static final String PROPERTY_PREFIX = Employer.class.getSimpleName() + '.';

    private Address address;
    private String businessType; // max 100
    private String fromDate; // see date pattern
    private BigDecimal monthlyIncome;
    private String name; // max 100
    private final PropertyChangeSupport pcs;
    private String phone; // max 20
    private String position; // max 100
    private boolean selfEmployed = false;
    private String title; // max 100
    private String toDate; // see date pattern

    /**
     * Constructs an employer.
     */
    public Employer() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public Employer copy() {
        final Employer copy = new Employer();

        if (this.address != null) {
            copy.setAddress(getAddress().copy());
        }

        copy.setBusinessType(getBusinessType());
        copy.setFromDate(getFromDate());
        copy.setMonthlyIncome(getMonthlyIncome());
        copy.setName(getName());
        copy.setPhone(getPhone());
        copy.setPosition(getPosition());
        copy.setSelfEmployed(isSelfEmployed());
        copy.setTitle(getTitle());
        copy.setToDate(getToDate());

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

        final Employer that = (Employer)obj;
        return Util.equals(this.address, that.address) && Util.equals(this.businessType, that.businessType)
                        && Util.equals(this.fromDate, that.fromDate)
                        && Util.equals(this.monthlyIncome, that.monthlyIncome)
                        && Util.equals(this.name, that.name)
                        && Util.equals(this.phone, that.phone)
                        && Util.equals(this.position, that.position)
                        && Util.equals(this.selfEmployed, that.selfEmployed)
                        && Util.equals(this.title, that.title)
                        && Util.equals(this.toDate, that.toDate);
    }

    private void firePropertyChange( final String propId,
                                     final Object oldValue,
                                     final Object newValue ) {
        if (oldValue == newValue) {
            return;
        }

        if ((oldValue != null) && oldValue.equals(newValue)) {
            return;
        }

        this.pcs.firePropertyChange(propId, oldValue, newValue);
    }

    /**
     * @return the address (never <code>null</code>)
     */
    public Address getAddress() {
        if (this.address == null) {
            this.address = new Address();
        }

        return this.address;
    }

    /**
     * @return the business type (can be <code>null</code> or empty)
     */
    public String getBusinessType() {
        return this.businessType;
    }

    /**
     * @return the from date (can be <code>null</code>)
     */
    public String getFromDate() {
        return this.fromDate;
    }

    /**
     * @return the monthly amount
     */
    public double getMonthlyIncome() {
        if (this.monthlyIncome == null) {
            return 0;
        }

        return this.monthlyIncome.doubleValue();
    }

    /**
     * @return the name (can be <code>null</code> or empty)
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the phone (can be <code>null</code> or empty)
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * @return the position (can be <code>null</code> or empty)
     */
    public String getPosition() {
        return this.position;
    }

    /**
     * @return the title (can be <code>null</code> or empty)
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return the to date (can be <code>null</code>)
     */
    public String getToDate() {
        return this.toDate;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.address, this.businessType, this.fromDate, this.monthlyIncome,
                                             this.name, this.phone, this.position, this.selfEmployed, this.title,
                                             this.toDate});
    }

    /**
     * @return the selfEmployed
     */
    public boolean isSelfEmployed() {
        return this.selfEmployed;
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        firePropertyChange(Properties.ADDRESS, event.getOldValue(), event.getNewValue());
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newAddress the new value for the address
     */
    public void setAddress( final Address newAddress ) {
        if (!Util.equals(this.address, newAddress)) {
            final Address oldValue = this.address;
            this.address = newAddress;
            firePropertyChange(Properties.ADDRESS, oldValue, this.address);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.address != null) {
                this.address.add(this);
            }
        }
    }

    /**
     * @param newBusinessType the new value for the businessType
     */
    public void setBusinessType( final String newBusinessType ) {
        if (!TextUtils.equals(this.businessType, newBusinessType)) {
            final Object oldValue = this.businessType;
            this.businessType = newBusinessType;
            firePropertyChange(Properties.BUSINESS_TYPE, oldValue, this.businessType);
        }
    }

    /**
     * @param newFrom the new value for the from date
     */
    public void setFromDate( final String newFrom ) {
        if (!TextUtils.equals(this.fromDate, newFrom)) {
            final Object oldValue = this.fromDate;
            this.fromDate = newFrom;
            firePropertyChange(Properties.FROM_DATE, oldValue, this.fromDate);
        }
    }

    /**
     * @param newMonthlyIncome the new value for the monthly income
     */
    public void setMonthlyIncome( final double newMonthlyIncome ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.monthlyIncome == null) && (newMonthlyIncome != 0)) {
            changed = true;
            oldValue = this.monthlyIncome;
            this.monthlyIncome = new BigDecimal(newMonthlyIncome);
            this.monthlyIncome.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.monthlyIncome != null) && (this.monthlyIncome.doubleValue() != newMonthlyIncome)) {
            changed = true;
            oldValue = this.monthlyIncome;

            if (newMonthlyIncome == 0) {
                this.monthlyIncome = null;
            } else {
                this.monthlyIncome = new BigDecimal(newMonthlyIncome);
                this.monthlyIncome.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.MONTHLY_INCOME, oldValue, this.monthlyIncome);
        }
    }

    /**
     * @param newName the new value for the name
     */
    public void setName( final String newName ) {
        if (!TextUtils.equals(this.name, newName)) {
            final Object oldValue = this.name;
            this.name = newName;
            firePropertyChange(Properties.NAME, oldValue, this.name);
        }
    }

    /**
     * @param newPhone the new value for the phone
     */
    public void setPhone( final String newPhone ) {
        if (!TextUtils.equals(this.phone, newPhone)) {
            final Object oldValue = this.phone;
            this.phone = newPhone;
            firePropertyChange(Properties.PHONE, oldValue, this.phone);
        }
    }

    /**
     * @param newPosition the new value for the position
     */
    public void setPosition( final String newPosition ) {
        if (!TextUtils.equals(this.position, newPosition)) {
            final Object oldValue = this.position;
            this.position = newPosition;
            firePropertyChange(Properties.POSITION, oldValue, this.position);
        }
    }

    /**
     * @param newSelfEmployed the new value for the selfEmployed
     */
    public void setSelfEmployed( final boolean newSelfEmployed ) {
        if (this.selfEmployed != newSelfEmployed) {
            final Object oldValue = this.selfEmployed;
            this.selfEmployed = newSelfEmployed;
            firePropertyChange(Properties.SELF_EMPLOYED, oldValue, this.selfEmployed);
        }
    }

    /**
     * @param newTitle the new value for the title
     */
    public void setTitle( final String newTitle ) {
        if (!TextUtils.equals(this.title, newTitle)) {
            final Object oldValue = this.title;
            this.title = newTitle;
            firePropertyChange(Properties.TITLE, oldValue, this.title);
        }
    }

    /**
     * @param newTo the new value for the to date (can be <code>null</code>)
     */
    public void setToDate( final String newTo ) {
        if (!TextUtils.equals(this.toDate, newTo)) {
            final Object oldValue = this.toDate;
            this.toDate = newTo;
            firePropertyChange(Properties.TO_DATE, oldValue, this.toDate);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final Employer from ) {
        if (from.address == null) {
            setAddress(null);
        } else {
            setAddress(from.address.copy());
        }

        setBusinessType(from.getBusinessType());
        setFromDate(from.getFromDate());
        setMonthlyIncome(from.getMonthlyIncome());
        setName(from.getName());
        setPhone(from.getPhone());
        setPosition(from.getPosition());
        setSelfEmployed(from.isSelfEmployed());
        setTitle(from.getTitle());
        setToDate(from.getToDate());
    }

    /**
     * An employer's property identifiers.
     */
    public interface Properties {

        /**
         * The employer's address property identifier.
         */
        String ADDRESS = PROPERTY_PREFIX + "address"; //$NON-NLS-1$

        /**
         * The employer's business type property identifier.
         */
        String BUSINESS_TYPE = PROPERTY_PREFIX + "business_type"; //$NON-NLS-1$

        /**
         * The employer's employee from date property identifier.
         */
        String FROM_DATE = PROPERTY_PREFIX + "from_date"; //$NON-NLS-1$

        /**
         * The employer's employee monthly income property identifier.
         */
        String MONTHLY_INCOME = PROPERTY_PREFIX + "monthly_income"; //$NON-NLS-1$

        /**
         * The employer's name property identifier.
         */
        String NAME = PROPERTY_PREFIX + "name"; //$NON-NLS-1$

        /**
         * The employer's phone property identifier.
         */
        String PHONE = PROPERTY_PREFIX + "phone"; //$NON-NLS-1$

        /**
         * The employer's employee position property identifier.
         */
        String POSITION = PROPERTY_PREFIX + "position"; //$NON-NLS-1$

        /**
         * The employer's self employed indicator property identifier.
         */
        String SELF_EMPLOYED = PROPERTY_PREFIX + "self_employed"; //$NON-NLS-1$

        /**
         * The employer's employee title type property identifier.
         */
        String TITLE = PROPERTY_PREFIX + "title"; //$NON-NLS-1$

        /**
         * The employer's employee to date property identifier.
         */
        String TO_DATE = PROPERTY_PREFIX + "to_date"; //$NON-NLS-1$

    }

}
