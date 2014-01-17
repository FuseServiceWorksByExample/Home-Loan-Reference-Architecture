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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower model object.
 */
public final class Borrower {

    /**
     * The borrowers marital status.
     */
    public static final String[] BORROWER_TYPE = new String[] {"Borrower", "Co_Borrower"}; //$NON-NLS-1$ //$NON-NLS-2$ 

    /**
     * The borrowers marital status.
     */
    public static final String[] MARITAL_STATUS = new String[] {"Married", "Not_Specified", "Separated", "Unmarried"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final int[] NO_DEP_AGES = new int[0];

    /**
     * An empty collection.
     */
    static final List<Borrower> NONE = Collections.emptyList();

    /**
     * @param original the borrower being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Borrower copy( final Borrower original ) {
        final Borrower copy = new Borrower();

        copy.setDeclarations(Declarations.copy(original.getDeclarations()));
        copy.setNumberOfDependents(original.dependents);
        copy.setDependentsAges(Util.copy(original.dependentsAges));
        copy.setDob(original.dob);
        copy.setEmploymentInformation(Employment.copy(original.getEmploymentInformation()));
        copy.setFirstName(original.firstName);
        copy.setMiddleName(original.middleName);
        copy.setLastName(original.lastName);
        copy.setMaritalStatus(original.maritalStatus);
        copy.setPhone(original.phone);
        copy.setSsn(original.ssn);
        copy.setTitle(original.title);
        copy.setType(original.type);
        copy.setYearsSchool(original.getYearsSchool());

        // addresses
        if (!original.getAddresses().isEmpty()) {
            for (final BorrowerAddress address : original.getAddresses()) {
                copy.addAddress(BorrowerAddress.copy(address));
            }
        }

        // dependent ages
        if (original.getDependentsAges().length != 0) {
            final int[] source = original.getDependentsAges();
            final int[] destination = new int[source.length];
            System.arraycopy(source, 0, destination, 0, source.length);
        }

        return copy;
    }

    private List<BorrowerAddress> addresses; // ordered, max 3
    private Declarations declarations;
    private int dependents = 0; // positive
    private int[] dependentsAges; // string max length 100
    private String dob; // xx/xx/xxxx
    private Employment employmentInformation;
    private String firstName; // max length 50
    private String lastName; // max length 50
    private String maritalStatus;
    private String middleName; // max length 50
    private String phone; // max length 20;
    private String ssn; // max length 50;
    private String title; // optional, max length 50
    private String type;
    private BigDecimal yearsSchool; // xx.xx

    /**
     * @param newAddress the address being added (cannot be <code>null</code>)
     */
    public void addAddress( final BorrowerAddress newAddress ) {
        if (newAddress == null) {
            throw new NullPointerException();
        }

        if (this.addresses == null) {
            this.addresses = new ArrayList<BorrowerAddress>();
        }

        this.addresses.add(newAddress);
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

        final Borrower that = (Borrower)obj;
        return (Util.equals(this.addresses, that.addresses) && Util.equals(this.declarations, that.declarations)
                        && Util.equals(this.dependents, that.dependents)
                        && Util.equals(this.dependentsAges, that.dependentsAges)
                        && Util.equals(this.dob, that.dob)
                        && Util.equals(this.employmentInformation, that.employmentInformation)
                        && Util.equals(this.firstName, that.firstName)
                        && Util.equals(this.middleName, that.middleName)
                        && Util.equals(this.lastName, that.lastName)
                        && Util.equals(this.maritalStatus, that.maritalStatus)
                        && Util.equals(this.phone, that.phone)
                        && Util.equals(this.ssn, that.ssn)
                        && Util.equals(this.title, that.title)
                        && Util.equals(this.type, that.type) && Util.equals(this.yearsSchool, that.yearsSchool));
    }

    /**
     * @return the addresses (never <code>null</code>)
     */
    public List<BorrowerAddress> getAddresses() {
        if (this.addresses == null) {
            return BorrowerAddress.NONE;
        }

        return this.addresses;
    }

    /**
     * @return the declarations (can be <code>null</code>)
     */
    public Declarations getDeclarations() {
        return this.declarations;
    }

    /**
     * @return the dependents ages (never <code>null</code>)
     */
    public int[] getDependentsAges() {
        if (this.dependentsAges == null) {
            return NO_DEP_AGES;
        }

        return this.dependentsAges;
    }

    /**
     * @return the date of birth (can be <code>null</code> or empty)
     */
    public String getDob() {
        return this.dob;
    }

    /**
     * @return the employment information (can be <code>null</code>)
     */
    public Employment getEmploymentInformation() {
        return this.employmentInformation;
    }

    /**
     * @return the first name (can be <code>null</code> or empty)
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the last name (can be <code>null</code> or empty)
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @return the marital status (can be <code>null</code> or empty)
     */
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    /**
     * @return the middle name (can be <code>null</code> or empty)
     */
    public String getMiddleName() {
        return this.middleName;
    }

    /**
     * @return the number of dependents
     */
    public int getNumberOfDependents() {
        return this.dependents;
    }

    /**
     * @return the phone (can be <code>null</code> or empty)
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * @return the SSN (can be <code>null</code> or empty)
     */
    public String getSsn() {
        return this.ssn;
    }

    /**
     * @return the title (can be <code>null</code> or empty)
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return the type (can be <code>null</code> or empty)
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return the years in school
     */
    public double getYearsSchool() {
        if (this.yearsSchool == null) {
            return 0;
        }

        return this.yearsSchool.doubleValue();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.addresses, this.declarations, this.dependents, this.dependentsAges,
                                             this.dob, this.employmentInformation, this.firstName, this.middleName,
                                             this.lastName, this.maritalStatus, this.phone, this.ssn, this.title,
                                             this.type, this.yearsSchool});
    }

    /**
     * @param newDeclarations the new value for the declarations
     */
    public void setDeclarations( final Declarations newDeclarations ) {
        if (!Util.equals(this.declarations, newDeclarations)) {
            this.declarations = newDeclarations;
        }
    }

    /**
     * @param newDependentsAges the new value for the dependentsAges
     */
    public void setDependentsAges( final int[] newDependentsAges ) {
        if (!Arrays.equals(this.dependentsAges, newDependentsAges)) {
            if (newDependentsAges.length == 0) {
                this.dependentsAges = null;
            } else {
                this.dependentsAges = newDependentsAges;
            }
        }
    }

    /**
     * @param newDob the new value for the dob
     */
    public void setDob( final String newDob ) {
        if (!Util.equals(this.dob, newDob)) {
            this.dob = newDob;
        }
    }

    /**
     * @param newEmploymentInformation the new value for the employmentInformation
     */
    public void setEmploymentInformation( final Employment newEmploymentInformation ) {
        if (!Util.equals(this.employmentInformation, newEmploymentInformation)) {
            this.employmentInformation = newEmploymentInformation;
        }
    }

    /**
     * @param newFirstName the new value for the firstName
     */
    public void setFirstName( final String newFirstName ) {
        if (!Util.equals(this.firstName, newFirstName)) {
            this.firstName = newFirstName;
        }
    }

    /**
     * @param newLastName the new value for the lastName
     */
    public void setLastName( final String newLastName ) {
        if (!Util.equals(this.lastName, newLastName)) {
            this.lastName = newLastName;
        }
    }

    /**
     * @param newMaritalStatus the new value for the maritalStatus
     */
    public void setMaritalStatus( final String newMaritalStatus ) {
        if (!Util.equals(this.maritalStatus, newMaritalStatus)) {
            this.maritalStatus = newMaritalStatus;
        }
    }

    /**
     * @param newMiddleName the new value for the middleName
     */
    public void setMiddleName( final String newMiddleName ) {
        if (!Util.equals(this.middleName, newMiddleName)) {
            this.middleName = newMiddleName;
        }
    }

    /**
     * @param newNumberOfDependents the new value for the number of dependents
     */
    public void setNumberOfDependents( final int newNumberOfDependents ) {
        if (this.dependents != newNumberOfDependents) {
            this.dependents = newNumberOfDependents;
        }
    }

    /**
     * @param newPhone the new value for the phone
     */
    public void setPhone( final String newPhone ) {
        if (!Util.equals(this.phone, newPhone)) {
            this.phone = newPhone;
        }
    }

    /**
     * @param newSsn the new value for the ssn
     */
    public void setSsn( final String newSsn ) {
        if (!Util.equals(this.ssn, newSsn)) {
            this.ssn = newSsn;
        }
    }

    /**
     * @param newTitle the new value for the title
     */
    public void setTitle( final String newTitle ) {
        if (!Util.equals(this.title, newTitle)) {
            this.title = newTitle;
        }
    }

    /**
     * @param newType the new value for the type
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            this.type = newType;
        }
    }

    /**
     * @param newYearsSchool the new value for the yearsSchool
     */
    public void setYearsSchool( final double newYearsSchool ) {
        if ((this.yearsSchool == null) || (this.yearsSchool.doubleValue() != newYearsSchool)) {
            this.yearsSchool = new BigDecimal(newYearsSchool);
            this.yearsSchool.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

}
