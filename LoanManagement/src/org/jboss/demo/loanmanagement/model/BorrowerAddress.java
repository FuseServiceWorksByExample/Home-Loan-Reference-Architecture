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
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower's address model object.
 */
public final class BorrowerAddress extends Address {

    /**
     * The address types.
     */
    public static final String[] ADDRESS_TYPES = new String[] {"Own", "Rent", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 

    protected static final String PREFIX = BorrowerAddress.class.getSimpleName() + '.';

    private BigDecimal numYears; // xx.xx
    private String type;

    /**
     * Return value can safely be cast to a {@link BorrowerAddress}.
     * 
     * @see org.jboss.demo.loanmanagement.model.Address#copy()
     */
    @Override
    public Address copy() {
        final BorrowerAddress copy = new BorrowerAddress();

        copy.setCity(getCity());
        copy.setCounty(getCounty());
        copy.setLine1(getLine1());
        copy.setLine2(getLine2());
        copy.setPostalCode(getPostalCode());
        copy.setState(getState());
        copy.setNumYears(getNumYears());
        copy.setType(getType());

        return copy;
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
     * @see org.jboss.demo.loanmanagement.model.Address#update(org.jboss.demo.loanmanagement.model.Address)
     */
    @Override
    public void update( final Address from ) {
        super.update(from);

        if (from instanceof BorrowerAddress) {
            setNumYears(((BorrowerAddress)from).getNumYears());
            setType(((BorrowerAddress)from).getType());
        }
    }

    /**
     * A borrower address's property identifiers
     */
    public interface Properties {

        /**
         * The address's number of years live at property identifier.
         */
        String NUM_YEARS = PREFIX + "num_years"; //$NON-NLS-1$

        /**
         * The address's borrower type property identifier.
         */
        String TYPE = PREFIX + "type"; //$NON-NLS-1$

    }

}
