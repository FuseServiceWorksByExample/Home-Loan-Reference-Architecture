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
import java.util.Arrays;
import android.text.TextUtils;

/**
 * A borrower's address model object.
 */
public class Address {

    protected static final String PROPERTY_PREFIX = Address.class.getSimpleName() + '.';

    /**
     * @param original the address being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Address copy( final Address original ) {
        final Address copy = new Address();

        copy.setCity(original.city);
        copy.setCounty(original.county);
        copy.setLine1(original.line1);
        copy.setLine2(original.line2);
        copy.setPostalCode(original.postalCode);
        copy.setState(original.state);

        return copy;
    }

    private String city; // max 50
    private String county; // max 50
    private String line1; // max 255
    private String line2; // max 255
    private final PropertyChangeSupport pcs;
    private String postalCode; // max 20
    private String state; // max 50

    /**
     * Constructs an address.
     */
    public Address() {
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

        final Address that = (Address)obj;
        return TextUtils.equals(this.line1, that.line1) && TextUtils.equals(this.line2, that.line2)
                        && TextUtils.equals(this.city, that.city)
                        && TextUtils.equals(this.county, that.county)
                        && TextUtils.equals(this.state, that.state)
                        && TextUtils.equals(this.postalCode, that.postalCode);
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
     * @return the city (can be <code>null</code> or empty)
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @return the county (can be <code>null</code> or empty)
     */
    public String getCounty() {
        return this.county;
    }

    /**
     * @return the line1 (can be <code>null</code> or empty)
     */
    public String getLine1() {
        return this.line1;
    }

    /**
     * @return the line2 (can be <code>null</code> or empty)
     */
    public String getLine2() {
        return this.line2;
    }

    /**
     * @return the postalCode (can be <code>null</code> or empty)
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * @return the state (can be <code>null</code> or empty)
     */
    public String getState() {
        return this.state;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.city, this.county, this.line1, this.line2, this.postalCode,
                                             this.state});
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newCity the new value for the city
     */
    public void setCity( final String newCity ) {
        if (!TextUtils.equals(this.city, newCity)) {
            final Object oldValue = this.city;
            this.city = newCity;
            firePropertyChange(Properties.CITY, oldValue, this.city);
        }
    }

    /**
     * @param newCounty the new value for the county
     */
    public void setCounty( final String newCounty ) {
        if (!TextUtils.equals(this.county, newCounty)) {
            final Object oldValue = this.county;
            this.county = newCounty;
            firePropertyChange(Properties.COUNTY, oldValue, this.county);
        }
    }

    /**
     * @param newLine1 the new value for the line1
     */
    public void setLine1( final String newLine1 ) {
        if (!TextUtils.equals(this.line1, newLine1)) {
            final Object oldValue = this.line1;
            this.line1 = newLine1;
            firePropertyChange(Properties.LINE_1, oldValue, this.line1);
        }
    }

    /**
     * @param newLine2 the new value for the line2
     */
    public void setLine2( final String newLine2 ) {
        if (!TextUtils.equals(this.line2, newLine2)) {
            final Object oldValue = this.line2;
            this.line2 = newLine2;
            firePropertyChange(Properties.LINE_2, oldValue, this.line2);
        }
    }

    /**
     * @param newPostalCode the new value for the postalCode
     */
    public void setPostalCode( final String newPostalCode ) {
        if (!TextUtils.equals(this.postalCode, newPostalCode)) {
            final Object oldValue = this.postalCode;
            this.postalCode = newPostalCode;
            firePropertyChange(Properties.POSTAL_CODE, oldValue, this.postalCode);
        }
    }

    /**
     * @param newState the new value for the state
     */
    public void setState( final String newState ) {
        if (!TextUtils.equals(this.state, newState)) {
            final Object oldValue = this.state;
            this.state = newState;
            firePropertyChange(Properties.STATE, oldValue, this.state);
        }
    }

    /**
     * An address's property identifiers
     */
    public interface Properties {

        /**
         * The address's city property identifier.
         */
        String CITY = PROPERTY_PREFIX + "city"; //$NON-NLS-1$

        /**
         * The address's county property identifier.
         */
        String COUNTY = PROPERTY_PREFIX + "county"; //$NON-NLS-1$

        /**
         * The address's line 1 property identifier.
         */
        String LINE_1 = PROPERTY_PREFIX + "line_1"; //$NON-NLS-1$

        /**
         * The address's line 2 property identifier.
         */
        String LINE_2 = PROPERTY_PREFIX + "line_2"; //$NON-NLS-1$

        /**
         * The address's postal code (zipcode) property identifier.
         */
        String POSTAL_CODE = PROPERTY_PREFIX + "postal_code"; //$NON-NLS-1$

        /**
         * The address's state property identifier.
         */
        String STATE = PROPERTY_PREFIX + "state"; //$NON-NLS-1$

    }

}
