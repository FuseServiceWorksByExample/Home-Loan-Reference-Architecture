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

/**
 * A borrower's address model object.
 */
public final class BorrowerAddress extends Address implements PropertyChangeListener {

    /**
     * The address types.
     */
    public static final String[] ADDRESS_TYPES = new String[] {"Own", "Rent", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 

    /**
     * An empty collection of borrower addresses.
     */
    static List<BorrowerAddress> NONE = Collections.emptyList();

    protected static final String PREFIX = BorrowerAddress.class.getSimpleName() + '.';

    /**
     * @param original the address being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static BorrowerAddress copy( final BorrowerAddress original ) {
        final BorrowerAddress copy = new BorrowerAddress();

        copy.setCity(original.getCity());
        copy.setCounty(original.getCounty());
        copy.setLine1(original.getLine1());
        copy.setLine2(original.getLine2());
        copy.setPostalCode(original.getPostalCode());
        copy.setState(original.getState());
        copy.setNumYears(original.getNumYears());
        copy.setType(original.type);

        return copy;
    }

    private BigDecimal numYears; // xx.xx
    private final PropertyChangeSupport pcs;
    private String type;

    /**
     * Constructs a borrower's address.
     */
    public BorrowerAddress() {
        this.pcs = new PropertyChangeSupport(this);
        super.add(this);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Address#add(java.beans.PropertyChangeListener)
     */
    @Override
    public final void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Address#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if (super.equals(obj)) {
            final BorrowerAddress that = (BorrowerAddress)obj;
            return ((this.numYears == that.numYears) && Util.equals(this.type, that.type));
        }

        return false;
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Address#firePropertyChange(java.lang.String, java.lang.Object,
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
     * @return the number of years
     */
    public double getNumYears() {
        if (this.numYears == null) {
            return 0;
        }

        return this.numYears.doubleValue();
    }

    /**
     * @return the type (can be <code>null</code> or empty)
     */
    public String getType() {
        return this.type;
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Address#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {super.hashCode(), this.numYears, this.type});
    }

    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        final String propId = event.getPropertyName();
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();

        if (Address.Properties.CITY.equals(propId)) {
            firePropertyChange(Properties.CITY, oldValue, newValue);
        } else if (Address.Properties.COUNTY.equals(propId)) {
            firePropertyChange(Properties.COUNTY, oldValue, newValue);
        } else if (Address.Properties.LINE_1.equals(propId)) {
            firePropertyChange(Properties.LINE_1, oldValue, newValue);
        } else if (Address.Properties.LINE_2.equals(propId)) {
            firePropertyChange(Properties.LINE_2, oldValue, newValue);
        } else if (Address.Properties.POSTAL_CODE.equals(propId)) {
            firePropertyChange(Properties.POSTAL_CODE, oldValue, newValue);
        } else if (Address.Properties.STATE.equals(propId)) {
            firePropertyChange(Properties.STATE, oldValue, newValue);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.Address#remove(java.beans.PropertyChangeListener)
     */
    @Override
    public final void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newNumYears the new value for the number of years
     */
    public void setNumYears( final double newNumYears ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.numYears == null) && (newNumYears != 0)) {
            changed = true;
            oldValue = this.numYears;
            this.numYears = new BigDecimal(newNumYears);
            this.numYears.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.numYears != null) && (this.numYears.doubleValue() != newNumYears)) {
            changed = true;
            oldValue = this.numYears;

            if (newNumYears == 0) {
                this.numYears = null;
            } else {
                this.numYears = new BigDecimal(newNumYears);
                this.numYears.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.NUM_YEARS, oldValue, this.numYears);
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
     * A borrower address's property identifiers
     */
    public interface Properties {

        /**
         * The address's city property identifier.
         */
        String CITY = PREFIX + "city"; //$NON-NLS-1$

        /**
         * The address's county property identifier.
         */
        String COUNTY = PREFIX + "county"; //$NON-NLS-1$

        /**
         * The address's line 1 property identifier.
         */
        String LINE_1 = PREFIX + "line_1"; //$NON-NLS-1$

        /**
         * The address's line 2 property identifier.
         */
        String LINE_2 = PREFIX + "line_2"; //$NON-NLS-1$

        /**
         * The address's number of years live at property identifier.
         */
        String NUM_YEARS = PREFIX + "num_years"; //$NON-NLS-1$

        /**
         * The address's postal code (zipcode) property identifier.
         */
        String POSTAL_CODE = PREFIX + "postal_code"; //$NON-NLS-1$

        /**
         * The address's state property identifier.
         */
        String STATE = PREFIX + "state"; //$NON-NLS-1$

        /**
         * The address's borrower type property identifier.
         */
        String TYPE = PREFIX + "type"; //$NON-NLS-1$

    }

}
