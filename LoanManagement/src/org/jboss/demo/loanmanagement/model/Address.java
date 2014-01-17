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

import java.util.Arrays;
import android.text.TextUtils;

/**
 * A borrower's address model object.
 */
public class Address {

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
    private String postalCode; // max 20
    private String state; // max 50

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
     * @param newCity the new value for the city
     */
    public void setCity( final String newCity ) {
        if (!TextUtils.equals(this.city, newCity)) {
            this.city = newCity;
        }
    }

    /**
     * @param newCounty the new value for the county
     */
    public void setCounty( final String newCounty ) {
        if (!TextUtils.equals(this.county, newCounty)) {
            this.county = newCounty;
        }
    }

    /**
     * @param newLine1 the new value for the line1
     */
    public void setLine1( final String newLine1 ) {
        if (!TextUtils.equals(this.line1, newLine1)) {
            this.line1 = newLine1;
        }
    }

    /**
     * @param newLine2 the new value for the line2
     */
    public void setLine2( final String newLine2 ) {
        if (!TextUtils.equals(this.line2, newLine2)) {
            this.line2 = newLine2;
        }
    }

    /**
     * @param newPostalCode the new value for the postalCode
     */
    public void setPostalCode( final String newPostalCode ) {
        if (!TextUtils.equals(this.postalCode, newPostalCode)) {
            this.postalCode = newPostalCode;
        }
    }

    /**
     * @param newState the new value for the state
     */
    public void setState( final String newState ) {
        if (!TextUtils.equals(this.state, newState)) {
            this.state = newState;
        }
    }

}
