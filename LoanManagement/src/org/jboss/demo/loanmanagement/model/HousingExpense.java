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
 * A housing expense model object.
 */
public final class HousingExpense {

    /**
     * The housing expense types.
     */
    public static final String[] HOUSING_EXPENSE_TYPES = new String[] {"Present", "Proposed"}; //$NON-NLS-1$ //$NON-NLS-2$ 

    /**
     * @param original the housing expense being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static HousingExpense copy( final HousingExpense original ) {
        final HousingExpense copy = new HousingExpense();

        copy.setFirstMortgage(original.getFirstMortgage());
        copy.setHazardInsurance(original.getHazardInsurance());
        copy.setHomeOwnerAssociationDues(original.getHomeOwnerAssociationDues());
        copy.setOther(original.getOther());
        copy.setOtherMortgage(original.getOtherMortgage());
        copy.setRealEstateTaxes(original.getRealEstateTaxes());
        copy.setRent(original.getRent());

        return copy;
    }

    private BigDecimal firstMortgage;
    private BigDecimal hazardInsurance;
    private BigDecimal homeOwnerAssociationDues;
    private BigDecimal other;
    private BigDecimal otherMortgage;
    private BigDecimal realEstateTaxes;
    private BigDecimal rent;
    private String type;

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

        final HousingExpense that = (HousingExpense)obj;
        return (Util.equals(this.firstMortgage, that.firstMortgage) && Util.equals(this.hazardInsurance,
                                                                                   that.hazardInsurance)
                        && Util.equals(this.homeOwnerAssociationDues, that.homeOwnerAssociationDues)
                        && Util.equals(this.other, that.other)
                        && Util.equals(this.otherMortgage, that.otherMortgage)
                        && Util.equals(this.realEstateTaxes, that.realEstateTaxes)
                        && Util.equals(this.rent, that.rent) && Util.equals(this.type, that.type));
    }

    /**
     * @return the first mortgage amount
     */
    public double getFirstMortgage() {
        if (this.firstMortgage == null) {
            return 0;
        }

        return this.firstMortgage.doubleValue();
    }

    /**
     * @return the hazard insurance amount
     */
    public double getHazardInsurance() {
        if (this.hazardInsurance == null) {
            return 0;
        }

        return this.hazardInsurance.doubleValue();
    }

    /**
     * @return the home owner association dues
     */
    public double getHomeOwnerAssociationDues() {
        if (this.homeOwnerAssociationDues == null) {
            return 0;
        }

        return this.homeOwnerAssociationDues.doubleValue();
    }

    /**
     * @return a miscellaneous amount
     */
    public double getOther() {
        if (this.other == null) {
            return 0;
        }

        return this.other.doubleValue();
    }

    /**
     * @return the other mortgage amount
     */
    public double getOtherMortgage() {
        if (this.otherMortgage == null) {
            return 0;
        }

        return this.otherMortgage.doubleValue();
    }

    /**
     * @return the real estate taxes
     */
    public double getRealEstateTaxes() {
        if (this.realEstateTaxes == null) {
            return 0;
        }

        return this.realEstateTaxes.doubleValue();
    }

    /**
     * @return the rent amount
     */
    public double getRent() {
        if (this.rent == null) {
            return 0;
        }

        return this.rent.doubleValue();
    }

    /**
     * @return the type of housing expense (can be <code>null</code> or empty)
     */
    public String getType() {
        return this.type;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.firstMortgage, this.hazardInsurance, this.homeOwnerAssociationDues,
                                             this.other, this.otherMortgage, this.realEstateTaxes, this.rent, this.type});
    }

    /**
     * @param newFirstMortgage the new value for the firstMortgage
     */
    public void setFirstMortgage( final double newFirstMortgage ) {
        if ((this.firstMortgage == null) || (this.firstMortgage.doubleValue() != newFirstMortgage)) {
            if (newFirstMortgage == 0) {
                this.firstMortgage = null;
            } else {
                this.firstMortgage = new BigDecimal(newFirstMortgage);
                this.firstMortgage.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newHazardInsurance the new value for the hazardInsurance
     */
    public void setHazardInsurance( final double newHazardInsurance ) {
        if ((this.hazardInsurance == null) || (this.hazardInsurance.doubleValue() != newHazardInsurance)) {
            if (newHazardInsurance == 0) {
                this.hazardInsurance = null;
            } else {
                this.hazardInsurance = new BigDecimal(newHazardInsurance);
                this.hazardInsurance.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newHomeOwnerAssociationDues the new value for the homeOwnerAssociationDues
     */
    public void setHomeOwnerAssociationDues( final double newHomeOwnerAssociationDues ) {
        if ((this.homeOwnerAssociationDues == null) || (this.homeOwnerAssociationDues.doubleValue() != newHomeOwnerAssociationDues)) {
            if (newHomeOwnerAssociationDues == 0) {
                this.homeOwnerAssociationDues = null;
            } else {
                this.homeOwnerAssociationDues = new BigDecimal(newHomeOwnerAssociationDues);
                this.homeOwnerAssociationDues.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newOther the new value for the other
     */
    public void setOther( final double newOther ) {
        if ((this.other == null) || (this.other.doubleValue() != newOther)) {
            if (newOther == 0) {
                this.other = null;
            } else {
                this.other = new BigDecimal(newOther);
                this.other.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newOtherMortgage the new value for the otherMortgage
     */
    public void setOtherMortgage( final double newOtherMortgage ) {
        if ((this.otherMortgage == null) || (this.otherMortgage.doubleValue() != newOtherMortgage)) {
            if (newOtherMortgage == 0) {
                this.otherMortgage = null;
            } else {
                this.otherMortgage = new BigDecimal(newOtherMortgage);
                this.otherMortgage.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newRealEstateTaxes the new value for the realEstateTaxes
     */
    public void setRealEstateTaxes( final double newRealEstateTaxes ) {
        if ((this.realEstateTaxes == null) || (this.realEstateTaxes.doubleValue() != newRealEstateTaxes)) {
            if (newRealEstateTaxes == 0) {
                this.realEstateTaxes = null;
            } else {
                this.realEstateTaxes = new BigDecimal(newRealEstateTaxes);
                this.realEstateTaxes.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newRent the new value for the rent
     */
    public void setRent( final double newRent ) {
        if ((this.rent == null) || (this.rent.doubleValue() != newRent)) {
            if (newRent == 0) {
                this.rent = null;
            } else {
                this.rent = new BigDecimal(newRent);
                this.rent.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newType the type (can be <code>null</code> or empty)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            this.type = newType;
        }
    }

}
