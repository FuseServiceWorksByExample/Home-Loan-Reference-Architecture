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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;
import android.text.TextUtils;

/**
 * A savings or checking account model object.
 */
public final class Account extends Asset {

    /**
     * An empty collection of accounts.
     */
    static final List<Account> NONE = Collections.emptyList();

    /**
     * @param original the account being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Account copy( final Account original ) {
        final Account copy = new Account();

        copy.setAmount(original.getAmount());
        copy.setDescription(original.getDescription());
        copy.setNumber(original.getNumber());
        copy.setAddress(Address.copy(original.getAddress()));

        return copy;
    }

    private String number; // max 100
    private Address address;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final Account that = (Account)obj;
        return (Util.equals(this.number, that.number) && Util.equals(this.address, that.address));
    }

    /**
     * @return the address (can be <code>null</code>)
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * @return the account name (can be <code>null</code> or empty)
     */
    public String getName() {
        return getDescription();
    }

    /**
     * @return the account number (can be <code>null</code> or empty)
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.number, this.address});
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
     * @param newName the new value for the name (can be <code>null</code> or empty)
     */
    public void setName( final String newName ) {
        setDescription(newName);
    }

    /**
     * @param newNumber the new value for the account number
     */
    public void setNumber( final String newNumber ) {
        if (!TextUtils.equals(this.number, newNumber)) {
            this.number = newNumber;
        }
    }

}
