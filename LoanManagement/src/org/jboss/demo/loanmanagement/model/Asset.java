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
import android.text.TextUtils;

/**
 * An asset model object.
 */
public abstract class Asset {

    private BigDecimal amount;
    private String description;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final Asset that = (Asset)obj;
        return (Util.equals(this.amount, that.amount) && Util.equals(this.description, that.description));
    }

    /**
     * @return the amount
     */
    public double getAmount() {
        if (this.amount == null) {
            return 0;
        }

        return this.amount.doubleValue();
    }

    /**
     * @return the description (can be <code>null</code> or empty)
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.amount, this.description});
    }

    /**
     * @param newAmount the new value for the amount
     */
    public void setAmount( final double newAmount ) {
        if ((this.amount == null) || (this.amount.doubleValue() != newAmount)) {
            if (newAmount == 0) {
                this.amount = null;
            } else {
                this.amount = new BigDecimal(newAmount);
                this.amount.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newDescription the new value for the description (can be <code>null</code> or empty)
     */
    public void setDescription( final String newDescription ) {
        if (!TextUtils.equals(this.description, newDescription)) {
            this.description = newDescription;
        }
    }

}
