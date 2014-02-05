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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import org.jboss.demo.loanmanagement.Util;

/**
 * A housing expense model object.
 */
public final class HousingExpense implements ModelObject<HousingExpense> {

    /**
     * The housing expense types.
     */
    public static final String[] HOUSING_EXPENSE_TYPES = new String[] {"Present", "Proposed"}; //$NON-NLS-1$ //$NON-NLS-2$ 

    protected static final String PROPERTY_PREFIX = HousingExpense.class.getSimpleName() + '.';

    private BigDecimal firstMortgage;
    private BigDecimal hazardInsurance;
    private BigDecimal homeOwnerAssociationDues;
    private BigDecimal other;
    private BigDecimal otherMortgage;
    private final PropertyChangeSupport pcs;
    private BigDecimal realEstateTaxes;
    private BigDecimal rent;
    private String type;

    /**
     * Constructs a housing expense.
     */
    public HousingExpense() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public HousingExpense copy() {
        final HousingExpense copy = new HousingExpense();

        copy.setFirstMortgage(getFirstMortgage());
        copy.setHazardInsurance(getHazardInsurance());
        copy.setHomeOwnerAssociationDues(getHomeOwnerAssociationDues());
        copy.setOther(getOther());
        copy.setOtherMortgage(getOtherMortgage());
        copy.setRealEstateTaxes(getRealEstateTaxes());
        copy.setRent(getRent());
        copy.setType(getType());

        return copy;
    }

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

    private void firePropertyChange( final String name,
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
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newFirstMortgage the new value for the firstMortgage
     */
    public void setFirstMortgage( final double newFirstMortgage ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.firstMortgage == null) && (newFirstMortgage != 0)) {
            changed = true;
            oldValue = this.firstMortgage;
            this.firstMortgage = new BigDecimal(newFirstMortgage);
            this.firstMortgage.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.firstMortgage != null) && (this.firstMortgage.doubleValue() != newFirstMortgage)) {
            changed = true;
            oldValue = this.firstMortgage;

            if (newFirstMortgage == 0) {
                this.firstMortgage = null;
            } else {
                this.firstMortgage = new BigDecimal(newFirstMortgage);
                this.firstMortgage.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.FIRST_MORTGAGE, oldValue, this.firstMortgage);
        }
    }

    /**
     * @param newHazardInsurance the new value for the hazardInsurance
     */
    public void setHazardInsurance( final double newHazardInsurance ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.hazardInsurance == null) && (newHazardInsurance != 0)) {
            changed = true;
            oldValue = this.hazardInsurance;
            this.hazardInsurance = new BigDecimal(newHazardInsurance);
            this.hazardInsurance.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.hazardInsurance != null) && (this.hazardInsurance.doubleValue() != newHazardInsurance)) {
            changed = true;
            oldValue = this.hazardInsurance;

            if (newHazardInsurance == 0) {
                this.hazardInsurance = null;
            } else {
                this.hazardInsurance = new BigDecimal(newHazardInsurance);
                this.hazardInsurance.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.HAZARD_INSURANCE, oldValue, this.hazardInsurance);
        }
    }

    /**
     * @param newHomeOwnerAssociationDues the new value for the homeOwnerAssociationDues
     */
    public void setHomeOwnerAssociationDues( final double newHomeOwnerAssociationDues ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.homeOwnerAssociationDues == null) && (newHomeOwnerAssociationDues != 0)) {
            changed = true;
            oldValue = this.homeOwnerAssociationDues;
            this.homeOwnerAssociationDues = new BigDecimal(newHomeOwnerAssociationDues);
            this.homeOwnerAssociationDues.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.homeOwnerAssociationDues != null) && (this.homeOwnerAssociationDues.doubleValue() != newHomeOwnerAssociationDues)) {
            changed = true;
            oldValue = this.homeOwnerAssociationDues;

            if (newHomeOwnerAssociationDues == 0) {
                this.homeOwnerAssociationDues = null;
            } else {
                this.homeOwnerAssociationDues = new BigDecimal(newHomeOwnerAssociationDues);
                this.homeOwnerAssociationDues.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.HOME_OWNER_ASSOCIATION_DUES, oldValue, this.homeOwnerAssociationDues);
        }
    }

    /**
     * @param newOther the new value for the other
     */
    public void setOther( final double newOther ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.other == null) && (newOther != 0)) {
            changed = true;
            oldValue = this.other;
            this.other = new BigDecimal(newOther);
            this.other.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.other != null) && (this.other.doubleValue() != newOther)) {
            changed = true;
            oldValue = this.other;

            if (newOther == 0) {
                this.other = null;
            } else {
                this.other = new BigDecimal(newOther);
                this.other.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.OTHER, oldValue, this.other);
        }
    }

    /**
     * @param newOtherMortgage the new value for the otherMortgage
     */
    public void setOtherMortgage( final double newOtherMortgage ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.otherMortgage == null) && (newOtherMortgage != 0)) {
            changed = true;
            oldValue = this.otherMortgage;
            this.otherMortgage = new BigDecimal(newOtherMortgage);
            this.otherMortgage.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.otherMortgage != null) && (this.otherMortgage.doubleValue() != newOtherMortgage)) {
            changed = true;
            oldValue = this.otherMortgage;

            if (newOtherMortgage == 0) {
                this.otherMortgage = null;
            } else {
                this.otherMortgage = new BigDecimal(newOtherMortgage);
                this.otherMortgage.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.OTHER_MORTGAGE, oldValue, this.otherMortgage);
        }
    }

    /**
     * @param newRealEstateTaxes the new value for the realEstateTaxes
     */
    public void setRealEstateTaxes( final double newRealEstateTaxes ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.realEstateTaxes == null) && (newRealEstateTaxes != 0)) {
            changed = true;
            oldValue = this.realEstateTaxes;
            this.realEstateTaxes = new BigDecimal(newRealEstateTaxes);
            this.realEstateTaxes.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.realEstateTaxes != null) && (this.realEstateTaxes.doubleValue() != newRealEstateTaxes)) {
            changed = true;
            oldValue = this.realEstateTaxes;

            if (newRealEstateTaxes == 0) {
                this.realEstateTaxes = null;
            } else {
                this.realEstateTaxes = new BigDecimal(newRealEstateTaxes);
                this.realEstateTaxes.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.REAL_ESTATE_TAXES, oldValue, this.realEstateTaxes);
        }
    }

    /**
     * @param newRent the new value for the rent
     */
    public void setRent( final double newRent ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.rent == null) && (newRent != 0)) {
            changed = true;
            oldValue = this.rent;
            this.rent = new BigDecimal(newRent);
            this.rent.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.rent != null) && (this.rent.doubleValue() != newRent)) {
            changed = true;
            oldValue = this.rent;

            if (newRent == 0) {
                this.rent = null;
            } else {
                this.rent = new BigDecimal(newRent);
                this.rent.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.RENT, oldValue, this.rent);
        }
    }

    /**
     * @param newType the type (can be <code>null</code> or empty)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            final Object oldValue = this.type;
            this.type = newType;
            firePropertyChange(Properties.TYPE, oldValue, this.type);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final HousingExpense from ) {
        setFirstMortgage(from.getFirstMortgage());
        setHazardInsurance(from.getHazardInsurance());
        setHomeOwnerAssociationDues(from.getHomeOwnerAssociationDues());
        setOther(from.getOther());
        setOtherMortgage(from.getOtherMortgage());
        setRealEstateTaxes(from.getRealEstateTaxes());
        setRent(from.getRent());
        setType(from.getType());
    }

    /**
     * A housing expense's property identifiers.
     */
    public interface Properties {

        /**
         * The housing expense's first mortgage amount property identifier.
         */
        String FIRST_MORTGAGE = PROPERTY_PREFIX + "first_mortgage"; //$NON-NLS-1$

        /**
         * The housing expense's hazard insurance amount property identifier.
         */
        String HAZARD_INSURANCE = PROPERTY_PREFIX + "hazard_insurance"; //$NON-NLS-1$

        /**
         * The housing expense's association dues amount property identifier.
         */
        String HOME_OWNER_ASSOCIATION_DUES = PROPERTY_PREFIX + "home_owner_association_dues"; //$NON-NLS-1$

        /**
         * The housing expense's other amount property identifier.
         */
        String OTHER = PROPERTY_PREFIX + "other"; //$NON-NLS-1$

        /**
         * The housing expense's other mortgage amount property identifier.
         */
        String OTHER_MORTGAGE = PROPERTY_PREFIX + "other_mortgage"; //$NON-NLS-1$

        /**
         * The housing expense's real estate taxes amount property identifier.
         */
        String REAL_ESTATE_TAXES = PROPERTY_PREFIX + "real_estate_taxes"; //$NON-NLS-1$

        /**
         * The housing expense's rent amount property identifier.
         */
        String RENT = PROPERTY_PREFIX + "rent"; //$NON-NLS-1$

        /**
         * The housing expense's property type property identifier.
         */
        String TYPE = PROPERTY_PREFIX + "type"; //$NON-NLS-1$

    }

}
