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
import org.jboss.demo.loanmanagement.Util;

/**
 * A property address model object.
 */
public final class Declarations {

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
    private boolean permanentResident;
    private boolean primaryResidence;
    private boolean propertyForeclosed;
    private String propertyType;
    private String titled;
    private boolean usCitizen;

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
     * @param newAnyJudgments <code>true</code> if there are any judgements
     */
    public void setAnyJudgments( final boolean newAnyJudgments ) {
        if (this.anyJudgments != newAnyJudgments) {
            this.anyJudgments = newAnyJudgments;
        }
    }

    /**
     * @param newBorrowedDownPayment <code>true</code> if borrowed the down payment
     */
    public void setBorrowedDownPayment( final boolean newBorrowedDownPayment ) {
        if (this.borrowedDownPayment != newBorrowedDownPayment) {
            this.borrowedDownPayment = newBorrowedDownPayment;
        }
    }

    /**
     * @param newCoMakerNote <code>true</code> if there is a co-maker note
     */
    public void setCoMakerNote( final boolean newCoMakerNote ) {
        if (this.coMakerNote != newCoMakerNote) {
            this.coMakerNote = newCoMakerNote;
        }
    }

    /**
     * @param newDeclaredBankrupt <code>true</code> if declared bankruptcy
     */
    public void setDeclaredBankrupt( final boolean newDeclaredBankrupt ) {
        if (this.declaredBankrupt != newDeclaredBankrupt) {
            this.declaredBankrupt = newDeclaredBankrupt;
        }
    }

    /**
     * @param newDelinquent <code>true</code> if ever been delinquent
     */
    public void setDelinquent( final boolean newDelinquent ) {
        if (this.delinquent != newDelinquent) {
            this.delinquent = newDelinquent;
        }
    }

    /**
     * @param newLawsuit <code>true</code> if involved in a lawsuit
     */
    public void setLawsuit( final boolean newLawsuit ) {
        if (this.lawsuit != newLawsuit) {
            this.lawsuit = newLawsuit;
        }
    }

    /**
     * @param newObligatedOnAnyLoan <code>true</code> if obligated on any other loan
     */
    public void setObligatedOnAnyLoan( final boolean newObligatedOnAnyLoan ) {
        if (this.obligatedOnAnyLoan != newObligatedOnAnyLoan) {
            this.obligatedOnAnyLoan = newObligatedOnAnyLoan;
        }
    }

    /**
     * @param newObligatedToPayAlimony <code>true</code> if obligated to pay alimony
     */
    public void setObligatedToPayAlimony( final boolean newObligatedToPayAlimony ) {
        if (this.obligatedToPayAlimony != newObligatedToPayAlimony) {
            this.obligatedToPayAlimony = newObligatedToPayAlimony;
        }
    }

    /**
     * @param newOwnershipInterest <code>true</code> if ownership interest
     */
    public void setOwnershipInterest( final boolean newOwnershipInterest ) {
        if (this.ownershipInterest != newOwnershipInterest) {
            this.ownershipInterest = newOwnershipInterest;
        }
    }

    /**
     * @param newPermanentResident <code>true</code> if a permanent resident
     */
    public void setPermanentResident( final boolean newPermanentResident ) {
        if (this.permanentResident != newPermanentResident) {
            this.permanentResident = newPermanentResident;
        }
    }

    /**
     * @param newPrimaryResidence <code>true</code> if a primary residence
     */
    public void setPrimaryResidence( final boolean newPrimaryResidence ) {
        if (this.primaryResidence != newPrimaryResidence) {
            this.primaryResidence = newPrimaryResidence;
        }
    }

    /**
     * @param newPropertyForeclosed <code>true</code> if the property is foreclosed
     */
    public void setPropertyForeclosed( final boolean newPropertyForeclosed ) {
        if (this.propertyForeclosed != newPropertyForeclosed) {
            this.propertyForeclosed = newPropertyForeclosed;
        }
    }

    /**
     * @param newPropertyType the new value for the property type (can be <code>null</code>)
     */
    public void setPropertyType( final String newPropertyType ) {
        if (!Util.equals(this.propertyType, newPropertyType)) {
            this.propertyType = newPropertyType;
        }
    }

    /**
     * @param newTitled the new value for the titled (can be <code>null</code>)
     */
    public void setTitled( final String newTitled ) {
        if (!Util.equals(this.titled, newTitled)) {
            this.titled = newTitled;
        }
    }

    /**
     * @param newUsCitizen <code>true</code> if a U.S. citizen
     */
    public void setUsCitizen( final boolean newUsCitizen ) {
        if (this.usCitizen != newUsCitizen) {
            this.usCitizen = newUsCitizen;
        }
    }

}
