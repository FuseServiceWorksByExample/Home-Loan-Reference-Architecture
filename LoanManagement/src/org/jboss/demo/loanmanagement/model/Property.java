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

import org.jboss.demo.loanmanagement.Util;

/**
 * A property address model object.
 */
public final class Property {

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
    private String type;
    private int yearBuilt;

    /**
     * @return the address
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
     * @param newAddress the new value for the address (can be <code>null</code>)
     */
    public void setAddress( final Address newAddress ) {
        if (!Util.equals(this.address, newAddress)) {
            this.address = newAddress;
        }
    }

    /**
     * @param newNumUnits the new value for the numUnits
     */
    public void setNumUnits( final int newNumUnits ) {
        if (this.numUnits != newNumUnits) {
            this.numUnits = newNumUnits;
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

    /**
     * @param newYearBuilt the new value for the year built
     */
    public void setYearBuilt( final int newYearBuilt ) {
        if (this.yearBuilt != newYearBuilt) {
            this.yearBuilt = newYearBuilt;
        }
    }

}
