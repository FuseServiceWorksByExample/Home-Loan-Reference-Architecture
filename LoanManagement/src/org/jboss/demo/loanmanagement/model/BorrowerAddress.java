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
import java.util.Collections;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower's address model object.
 */
public final class BorrowerAddress extends Address {

    /**
     * The address types.
     */
    public static final String[] ADDRESS_TYPES = new String[] {"Own", "Rent", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 

    /**
     * An empty collection of borrower addresses.
     */
    static List<BorrowerAddress> NONE = Collections.emptyList();

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
    private String type;

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
        if ((this.numYears == null) || (this.numYears.doubleValue() != newNumYears)) {
            this.numYears = new BigDecimal(newNumYears);
            this.numYears.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newType the new value for the type (can be <code>null</code>)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            this.type = newType;
        }
    }

}
