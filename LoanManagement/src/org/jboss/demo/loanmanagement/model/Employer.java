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
import android.text.TextUtils;

/**
 * A borrower's employer.
 */
public final class Employer {

    /**
     * An empty collection of employers.
     */
    static final List<Employer> NONE = Collections.emptyList();

    /**
     * @param original the employer being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Employer copy( final Employer original ) {
        final Employer copy = new Employer();

        copy.setAddress(Address.copy(original.getAddress()));
        copy.setBusinessType(original.businessType);
        copy.setFromDate(original.from);
        copy.setMonthlyIncome(original.getMonthlyIncome());
        copy.setName(original.name);
        copy.setPhone(original.phone);
        copy.setPosition(original.position);
        copy.setSelfEmployed(original.selfEmployed);
        copy.setTitle(original.title);
        copy.setToDate(original.to);

        return copy;
    }

    private Address address;
    private String businessType; // max 100
    private String from; // see date pattern
    private BigDecimal monthlyIncome;
    private String name; // max 100
    private String phone; // max 20
    private String position; // max 100
    private boolean selfEmployed = false;
    private String title; // max 100
    private String to; // see date pattern

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
                        && Util.equals(this.from, that.from)
                        && Util.equals(this.monthlyIncome, that.monthlyIncome)
                        && Util.equals(this.name, that.name)
                        && Util.equals(this.phone, that.phone)
                        && Util.equals(this.position, that.position)
                        && Util.equals(this.selfEmployed, that.selfEmployed)
                        && Util.equals(this.title, that.title)
                        && Util.equals(this.to, that.to);
    }

    /**
     * @return the address (can be <code>null</code>)
     */
    public Address getAddress() {
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
        return this.from;
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
        return this.to;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.address, this.businessType, this.from, this.monthlyIncome, this.name,
                                             this.phone, this.position, this.selfEmployed, this.title, this.to});
    }

    /**
     * @return the selfEmployed
     */
    public boolean isSelfEmployed() {
        return this.selfEmployed;
    }

    /**
     * @param newAddress the new value for the address
     */
    public void setAddress( final Address newAddress ) {
        if (!Util.equals(this.address, newAddress)) {
            this.address = newAddress;
        }
    }

    /**
     * @param newBusinessType the new value for the businessType
     */
    public void setBusinessType( final String newBusinessType ) {
        if (!TextUtils.equals(this.businessType, newBusinessType)) {
            this.businessType = newBusinessType;
        }
    }

    /**
     * @param newFrom the new value for the from date
     */
    public void setFromDate( final String newFrom ) {
        if (!TextUtils.equals(this.from, newFrom)) {
            this.from = newFrom;
        }
    }

    /**
     * @param newMonthlyIncome the new value for the monthly income
     */
    public void setMonthlyIncome( final double newMonthlyIncome ) {
        if ((this.monthlyIncome == null) || (this.monthlyIncome.doubleValue() != newMonthlyIncome)) {
            this.monthlyIncome = new BigDecimal(newMonthlyIncome);
            this.monthlyIncome.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    /**
     * @param newName the new value for the name
     */
    public void setName( final String newName ) {
        if (!TextUtils.equals(this.name, newName)) {
            this.name = newName;
        }
    }

    /**
     * @param newPhone the new value for the phone
     */
    public void setPhone( final String newPhone ) {
        if (!TextUtils.equals(this.phone, newPhone)) {
            this.phone = newPhone;
        }
    }

    /**
     * @param newPosition the new value for the position
     */
    public void setPosition( final String newPosition ) {
        if (!TextUtils.equals(this.position, newPosition)) {
            this.position = newPosition;
        }
    }

    /**
     * @param newSelfEmployed the new value for the selfEmployed
     */
    public void setSelfEmployed( final boolean newSelfEmployed ) {
        if (this.selfEmployed != newSelfEmployed) {
            this.selfEmployed = newSelfEmployed;
        }
    }

    /**
     * @param newTitle the new value for the title
     */
    public void setTitle( final String newTitle ) {
        if (!TextUtils.equals(this.title, newTitle)) {
            this.title = newTitle;
        }
    }

    /**
     * @param newTo the new value for the to date (can be <code>null</code>)
     */
    public void setToDate( final String newTo ) {
        if (!TextUtils.equals(this.to, newTo)) {
            this.to = newTo;
        }
    }

}
