/*
 * Copyright 2013 JBoss Inc
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

    private Address address;
    private int numUnits = 1;
    private int yearBuilt = 0;
    private Type type = null;

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
     * @return the type
     */
    public Type getType() {
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
    public void setType( final Type newType ) {
        if (this.type != newType) {
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

    /**
     * The property type.
     */
    public enum Type {

        /**
         * A primary residence property.
         */
        PRIMARY_RESIDENCE("Primary_Residence"), //$NON-NLS-1$

        /**
         * A secondary residence property.
         */
        SECONDARY_RESIDENCE("Secondary_Residence"), //$NON-NLS-1$

        /**
         * An investment property.
         */
        INVESTMENT("Investment"); //$NON-NLS-1$

        private final String value;

        private Type( final String enumValue ) {
            this.value = enumValue;
        }

        /**
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.value;
        }

    }

}
