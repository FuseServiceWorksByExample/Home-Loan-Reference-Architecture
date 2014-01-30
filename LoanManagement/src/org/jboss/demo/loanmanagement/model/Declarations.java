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
import java.util.Arrays;
import org.jboss.demo.loanmanagement.Util;

/**
 * A property address model object.
 */
public final class Declarations {

    protected static final String PROPERTY_PREFIX = Declarations.class.getSimpleName() + '.';

    /**
     * The types of property.
     */
    public static final String[] PROPERTY_TYPES =
                    new String[] {"Principal_Residence", "Second_Home", "Investment_Property", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ 

    /**
     * The names on the title.
     */
    public static final String[] TITLED_BY_TYPES = new String[] {"Solely_By_Yourself", "Jointly_With_Your_Spouse", //$NON-NLS-1$ //$NON-NLS-2$
                                                                 "Jointly_With_Another_Person", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * @param original the declarations being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Declarations copy( final Declarations original ) {
        final Declarations copy = new Declarations();

        copy.setAnyJudgments(original.anyJudgments);
        copy.setBorrowedDownPayment(original.borrowedDownPayment);
        copy.setCoMakerNote(original.coMakerNote);
        copy.setDeclaredBankrupt(original.declaredBankrupt);
        copy.setDelinquent(original.delinquent);
        copy.setLawsuit(original.lawsuit);
        copy.setObligatedOnAnyLoan(original.obligatedOnAnyLoan);
        copy.setObligatedToPayAlimony(original.obligatedToPayAlimony);
        copy.setOwnershipInterest(original.ownershipInterest);
        copy.setPermanentResident(original.permanentResident);
        copy.setPrimaryResidence(original.primaryResidence);
        copy.setPropertyForeclosed(original.propertyForeclosed);
        copy.setUsCitizen(original.usCitizen);
        copy.setPropertyType(original.propertyType);
        copy.setTitled(original.titled);

        return copy;
    }

    private boolean anyJudgments;
    private boolean borrowedDownPayment;
    private boolean coMakerNote;
    private boolean declaredBankrupt;
    private boolean delinquent;
    private boolean lawsuit;
    private boolean obligatedOnAnyLoan;
    private boolean obligatedToPayAlimony;
    private boolean ownershipInterest;
    private final PropertyChangeSupport pcs;
    private boolean permanentResident;
    private boolean primaryResidence;
    private boolean propertyForeclosed;
    private String propertyType;
    private String titled;
    private boolean usCitizen;

    /**
     * Constructs loan declarations.
     */
    public Declarations() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
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

        final Declarations that = (Declarations)obj;
        return ((this.anyJudgments == that.anyJudgments) && (this.borrowedDownPayment == that.borrowedDownPayment)
                        && (this.coMakerNote == that.coMakerNote)
                        && (this.declaredBankrupt == that.declaredBankrupt)
                        && (this.delinquent == that.delinquent)
                        && (this.lawsuit == that.lawsuit)
                        && (this.obligatedOnAnyLoan == that.obligatedOnAnyLoan)
                        && (this.obligatedToPayAlimony == that.obligatedToPayAlimony)
                        && (this.ownershipInterest == that.ownershipInterest)
                        && (this.permanentResident == that.permanentResident)
                        && (this.primaryResidence == that.primaryResidence)
                        && (this.propertyForeclosed == that.propertyForeclosed)
                        && (this.usCitizen == that.usCitizen)
                        && Util.equals(this.propertyType, that.propertyType) && Util.equals(this.titled, that.titled));
    }

    protected void firePropertyChange( final String name,
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
     * @return the property type (can be <code>null</code> or empty)
     */
    public String getPropertyType() {
        return this.propertyType;
    }

    /**
     * @return the titled (can be <code>null</code> or empty)
     */
    public String getTitled() {
        return this.titled;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.anyJudgments, this.borrowedDownPayment, this.coMakerNote,
                                             this.declaredBankrupt, this.delinquent, this.lawsuit,
                                             this.obligatedOnAnyLoan, this.obligatedToPayAlimony,
                                             this.ownershipInterest, this.permanentResident, this.primaryResidence,
                                             this.propertyForeclosed, this.propertyType, this.titled, this.usCitizen});
    }

    /**
     * @return <code>true</code> if there are any judgments
     */
    public boolean isAnyJudgments() {
        return this.anyJudgments;
    }

    /**
     * @return <code>true</code> if borrowed down payment
     */
    public boolean isBorrowedDownPayment() {
        return this.borrowedDownPayment;
    }

    /**
     * @return <code>true</code> if there is a co-maker note
     */
    public boolean isCoMakerNote() {
        return this.coMakerNote;
    }

    /**
     * @return <code>true</code> if declared bankruptcy
     */
    public boolean isDeclaredBankrupt() {
        return this.declaredBankrupt;
    }

    /**
     * @return <code>true</code> if been delinquent
     */
    public boolean isDelinquent() {
        return this.delinquent;
    }

    /**
     * @return <code>true</code> if been involved in a lawsuit
     */
    public boolean isLawsuit() {
        return this.lawsuit;
    }

    /**
     * @return <code>true</code> if obligated on another loan
     */
    public boolean isObligatedOnAnyLoan() {
        return this.obligatedOnAnyLoan;
    }

    /**
     * @return <code>true</code> if obligated to pay alimony
     */
    public boolean isObligatedToPayAlimony() {
        return this.obligatedToPayAlimony;
    }

    /**
     * @return <code>true</code> if ownership interest
     */
    public boolean isOwnershipInterest() {
        return this.ownershipInterest;
    }

    /**
     * @return <code>true</code> if a permanent resident
     */
    public boolean isPermanentResident() {
        return this.permanentResident;
    }

    /**
     * @return <code>true</code> if property will be a primary residence
     */
    public boolean isPrimaryResidence() {
        return this.primaryResidence;
    }

    /**
     * @return <code>true</code> if a foreclosed property
     */
    public boolean isPropertyForeclosed() {
        return this.propertyForeclosed;
    }

    /**
     * @return <code>true</code> if a U.S. citizen
     */
    public boolean isUsCitizen() {
        return this.usCitizen;
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param newAnyJudgments <code>true</code> if there are any judgments
     */
    public void setAnyJudgments( final boolean newAnyJudgments ) {
        if (this.anyJudgments != newAnyJudgments) {
            final Object oldValue = this.anyJudgments;
            this.anyJudgments = newAnyJudgments;
            firePropertyChange(Properties.ANY_JUDGMENTS, oldValue, this.anyJudgments);
        }
    }

    /**
     * @param newBorrowedDownPayment <code>true</code> if borrowed the down payment
     */
    public void setBorrowedDownPayment( final boolean newBorrowedDownPayment ) {
        if (this.borrowedDownPayment != newBorrowedDownPayment) {
            final Object oldValue = this.borrowedDownPayment;
            this.borrowedDownPayment = newBorrowedDownPayment;
            firePropertyChange(Properties.BORROWED_DOWN_PAYMENT, oldValue, this.borrowedDownPayment);
        }
    }

    /**
     * @param newCoMakerNote <code>true</code> if there is a co-maker note
     */
    public void setCoMakerNote( final boolean newCoMakerNote ) {
        if (this.coMakerNote != newCoMakerNote) {
            final Object oldValue = this.coMakerNote;
            this.coMakerNote = newCoMakerNote;
            firePropertyChange(Properties.CO_MAKER_NOTE, oldValue, this.coMakerNote);
        }
    }

    /**
     * @param newDeclaredBankrupt <code>true</code> if declared bankruptcy
     */
    public void setDeclaredBankrupt( final boolean newDeclaredBankrupt ) {
        if (this.declaredBankrupt != newDeclaredBankrupt) {
            final Object oldValue = this.declaredBankrupt;
            this.declaredBankrupt = newDeclaredBankrupt;
            firePropertyChange(Properties.DECLARED_BANKRUPTCY, oldValue, this.declaredBankrupt);
        }
    }

    /**
     * @param newDelinquent <code>true</code> if ever been delinquent
     */
    public void setDelinquent( final boolean newDelinquent ) {
        if (this.delinquent != newDelinquent) {
            final Object oldValue = this.delinquent;
            this.delinquent = newDelinquent;
            firePropertyChange(Properties.DELINQUENT, oldValue, this.delinquent);
        }
    }

    /**
     * @param newLawsuit <code>true</code> if involved in a lawsuit
     */
    public void setLawsuit( final boolean newLawsuit ) {
        if (this.lawsuit != newLawsuit) {
            final Object oldValue = this.lawsuit;
            this.lawsuit = newLawsuit;
            firePropertyChange(Properties.LAWSUIT, oldValue, this.lawsuit);
        }
    }

    /**
     * @param newObligatedOnAnyLoan <code>true</code> if obligated on any other loan
     */
    public void setObligatedOnAnyLoan( final boolean newObligatedOnAnyLoan ) {
        if (this.obligatedOnAnyLoan != newObligatedOnAnyLoan) {
            final Object oldValue = this.obligatedOnAnyLoan;
            this.obligatedOnAnyLoan = newObligatedOnAnyLoan;
            firePropertyChange(Properties.OBLIGATED_ON_ANY_LOAN, oldValue, this.obligatedOnAnyLoan);
        }
    }

    /**
     * @param newObligatedToPayAlimony <code>true</code> if obligated to pay alimony
     */
    public void setObligatedToPayAlimony( final boolean newObligatedToPayAlimony ) {
        if (this.obligatedToPayAlimony != newObligatedToPayAlimony) {
            final Object oldValue = this.obligatedToPayAlimony;
            this.obligatedToPayAlimony = newObligatedToPayAlimony;
            firePropertyChange(Properties.OBLIGATED_TO_PAY_ALIMONY, oldValue, this.obligatedToPayAlimony);
        }
    }

    /**
     * @param newOwnershipInterest <code>true</code> if ownership interest
     */
    public void setOwnershipInterest( final boolean newOwnershipInterest ) {
        if (this.ownershipInterest != newOwnershipInterest) {
            final Object oldValue = this.ownershipInterest;
            this.ownershipInterest = newOwnershipInterest;
            firePropertyChange(Properties.OWNERSHIP_INTEREST, oldValue, this.ownershipInterest);
        }
    }

    /**
     * @param newPermanentResident <code>true</code> if a permanent resident
     */
    public void setPermanentResident( final boolean newPermanentResident ) {
        if (this.permanentResident != newPermanentResident) {
            final Object oldValue = this.permanentResident;
            this.permanentResident = newPermanentResident;
            firePropertyChange(Properties.PERMANENT_RESIDENT, oldValue, this.permanentResident);
        }
    }

    /**
     * @param newPrimaryResidence <code>true</code> if a primary residence
     */
    public void setPrimaryResidence( final boolean newPrimaryResidence ) {
        if (this.primaryResidence != newPrimaryResidence) {
            final Object oldValue = this.primaryResidence;
            this.primaryResidence = newPrimaryResidence;
            firePropertyChange(Properties.PRIMARY_RESIDENCE, oldValue, this.primaryResidence);
        }
    }

    /**
     * @param newPropertyForeclosed <code>true</code> if the property is foreclosed
     */
    public void setPropertyForeclosed( final boolean newPropertyForeclosed ) {
        if (this.propertyForeclosed != newPropertyForeclosed) {
            final Object oldValue = this.propertyForeclosed;
            this.propertyForeclosed = newPropertyForeclosed;
            firePropertyChange(Properties.PROPERTY_FORECLOSED, oldValue, this.propertyForeclosed);
        }
    }

    /**
     * @param newPropertyType the new value for the property type (can be <code>null</code>)
     */
    public void setPropertyType( final String newPropertyType ) {
        if (!Util.equals(this.propertyType, newPropertyType)) {
            final Object oldValue = this.propertyType;
            this.propertyType = newPropertyType;
            firePropertyChange(Properties.PROPERTY_TYPE, oldValue, this.propertyType);
        }
    }

    /**
     * @param newTitled the new value for the titled (can be <code>null</code>)
     */
    public void setTitled( final String newTitled ) {
        if (!Util.equals(this.titled, newTitled)) {
            final Object oldValue = this.titled;
            this.titled = newTitled;
            firePropertyChange(Properties.TITLED, oldValue, this.titled);
        }
    }

    /**
     * @param newUsCitizen <code>true</code> if a U.S. citizen
     */
    public void setUsCitizen( final boolean newUsCitizen ) {
        if (this.usCitizen != newUsCitizen) {
            final Object oldValue = this.usCitizen;
            this.usCitizen = newUsCitizen;
            firePropertyChange(Properties.US_CITIZEN, oldValue, this.usCitizen);
        }
    }

    /**
     * A declaration's property identifiers
     */
    public interface Properties {

        /**
         * The declaration's any judgements indicator property identifier.
         */
        String ANY_JUDGMENTS = "any_judgments"; //$NON-NLS-1$

        /**
         * The declaration's borrowed down payment indicator property identifier.
         */
        String BORROWED_DOWN_PAYMENT = "borrowed_down_payment"; //$NON-NLS-1$

        /**
         * The declaration's co-maker note indicator property identifier.
         */
        String CO_MAKER_NOTE = "co_maker_note"; //$NON-NLS-1$

        /**
         * The declaration's declared bankruptcy indicator property identifier.
         */
        String DECLARED_BANKRUPTCY = "declared_bankruptcy"; //$NON-NLS-1$

        /**
         * The declaration's delinquent indicator property identifier.
         */
        String DELINQUENT = "delinquent"; //$NON-NLS-1$

        /**
         * The declaration's lawsuit indicator property identifier.
         */
        String LAWSUIT = "lawsuit"; //$NON-NLS-1$

        /**
         * The declaration's objlicated on any other loan indicator property identifier.
         */
        String OBLIGATED_ON_ANY_LOAN = "obligated_on_any_loan"; //$NON-NLS-1$

        /**
         * The declaration's obligated to pay aliminoy indicator property identifier.
         */
        String OBLIGATED_TO_PAY_ALIMONY = "obligated_to_pay_alimony"; //$NON-NLS-1$

        /**
         * The declaration's ownership interest indicator property identifier.
         */
        String OWNERSHIP_INTEREST = "ownership_interest"; //$NON-NLS-1$

        /**
         * The declaration's permanent residence indicator property identifier.
         */
        String PERMANENT_RESIDENT = "permanent_resident"; //$NON-NLS-1$

        /**
         * The declaration's primary residence indicator property identifier.
         */
        String PRIMARY_RESIDENCE = "primary_residence"; //$NON-NLS-1$

        /**
         * The declaration's property foreclosed indicator property identifier.
         */
        String PROPERTY_FORECLOSED = "property_foreclosed"; //$NON-NLS-1$

        /**
         * The declaration's property type property identifier.
         */
        String PROPERTY_TYPE = "property_type"; //$NON-NLS-1$

        /**
         * The declaration's titled by property identifier.
         */
        String TITLED = "titled"; //$NON-NLS-1$

        /**
         * The declaration's US citizen indicator property identifier.
         */
        String US_CITIZEN = "us_citizen"; //$NON-NLS-1$

    }

}
