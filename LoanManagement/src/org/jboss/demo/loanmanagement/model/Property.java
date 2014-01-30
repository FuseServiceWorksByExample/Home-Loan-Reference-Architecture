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

/**
 * A property address model object.
 */
public final class Property implements PropertyChangeListener {

    protected static final String PROPERTY_PREFIX = Property.class.getSimpleName() + '.';

    /**
     * The types of property.
     */
    public static final String[] PROPERTY_TYPES =
                    new String[] {"Primary_Residence", "Secondary_Residence", "Investment"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 

    /**
     * @param original the property being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Property copy( final Property original ) {
        final Property copy = new Property();

        copy.setAddress(Address.copy(original.address));
        copy.setNumUnits(original.numUnits);
        copy.setYearBuilt(original.yearBuilt);
        copy.setType(original.type);

        return copy;
    }

    private Address address;
    private int numUnits = 1;
    private final PropertyChangeSupport pcs;
    private String type;
    private int yearBuilt;

    /**
     * Constructs a property.
     */
    public Property() {
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

        final Property that = (Property)obj;
        return (Util.equals(this.address, that.address) && Util.equals(this.numUnits, that.numUnits)
                        && Util.equals(this.type, that.type) && Util.equals(this.yearBuilt, that.yearBuilt));
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
     * @return the address (can be <code>null</code>)
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @return the numUnits
     */
    public int getNumUnits() {
        return this.numUnits;
    }

    /**
     * @return the type (can be <code>null</code> or empty)
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the year built
     */
    public int getYearBuilt() {
        return this.yearBuilt;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.address, this.numUnits, this.type, this.yearBuilt});
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
     * @param newAddress the new value for the address (can be <code>null</code>)
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
     * @param newNumUnits the new value for the numUnits
     */
    public void setNumUnits( final int newNumUnits ) {
        if (this.numUnits != newNumUnits) {
            final Object oldValue = this.numUnits;
            this.numUnits = newNumUnits;
            firePropertyChange(Properties.NUM_UNITS, oldValue, this.numUnits);
        }
    }

    /**
     * @param newType the new value for the type (can be <code>null</code>)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            final Object oldValue = this.type;
            this.type = newType;
            firePropertyChange(Properties.TYPE, oldValue, this.type);
        }
    }

    /**
     * @param newYearBuilt the new value for the year built
     */
    public void setYearBuilt( final int newYearBuilt ) {
        if (this.yearBuilt != newYearBuilt) {
            final Object oldValue = this.yearBuilt;
            this.yearBuilt = newYearBuilt;
            firePropertyChange(Properties.YEAR_BUILT, oldValue, this.yearBuilt);
        }
    }

    /**
     * A property's property identifiers
     */
    public interface Properties {

        /**
         * The property's address property identifier.
         */
        String ADDRESS = PROPERTY_PREFIX + "address"; //$NON-NLS-1$

        /**
         * The property's number of units property identifier.
         */
        String NUM_UNITS = PROPERTY_PREFIX + "num_units"; //$NON-NLS-1$

        /**
         * The property's type property identifier.
         */
        String TYPE = PROPERTY_PREFIX + "type"; //$NON-NLS-1$

        /**
         * The property's year built property identifier.
         */
        String YEAR_BUILT = PROPERTY_PREFIX + "year_built"; //$NON-NLS-1$

    }

}
