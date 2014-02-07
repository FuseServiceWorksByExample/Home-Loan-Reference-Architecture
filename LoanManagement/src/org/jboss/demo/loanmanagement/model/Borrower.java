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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * A borrower model object.
 */
public final class Borrower implements ModelObject<Borrower>, PropertyChangeListener {

    /**
     * The {@link #BORROWER_TYPE} index for the borrower type.
     */
    public static final int BORROWER_INDEX = 0;

    /**
     * The borrower type.
     */
    public static final String[] BORROWER_TYPE = new String[] {"Borrower", "Co_Borrower"}; //$NON-NLS-1$ //$NON-NLS-2$ 

    /**
     * The {@link #BORROWER_TYPE} index for the co-borrower type.
     */
    public static final int CO_BORROWER_INDEX = 1;

    /**
     * The borrowers marital status.
     */
    public static final String[] MARITAL_TYPE = new String[] {"Married", "Unmarried", "Separated", "Not_Specified"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final int[] NO_DEP_AGES = new int[0];
    protected static final String PROPERTY_PREFIX = Borrower.class.getSimpleName() + '.';

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
    private BigDecimal numYearsSchool; // xx.xx
    private final PropertyChangeSupport pcs;
    private String phone; // max length 20;
    private String ssn; // max length 50;
    private String title; // optional, max length 50
    private String type;

    /**
     * Constructs a borrower.
     */
    public Borrower() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param newAddress the address being added (cannot be <code>null</code>)
     */
    public void addAddress( final BorrowerAddress newAddress ) {
        if (newAddress == null) {
            throw new NullPointerException();
        }

        if (getAddresses().add(newAddress)) {
            firePropertyChange(Properties.ADDRESSES, null, newAddress);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public Borrower copy() {
        final Borrower copy = new Borrower();

        if (this.declarations != null) {
            copy.setDeclarations(getDeclarations().copy());
        }

        if (this.employmentInformation != null) {
            copy.setEmploymentInformation(getEmploymentInformation().copy());
        }

        copy.setNumberOfDependents(getNumberOfDependents());
        copy.setDob(getDob());
        copy.setFirstName(getFirstName());
        copy.setMiddleName(getMiddleName());
        copy.setLastName(getLastName());
        copy.setMaritalStatus(getMaritalStatus());
        copy.setPhone(getPhone());
        copy.setSsn(getSsn());
        copy.setTitle(getTitle());
        copy.setType(getType());
        copy.setYearsSchool(getYearsSchool());

        // addresses
        if ((this.addresses != null) && !getAddresses().isEmpty()) {
            for (final BorrowerAddress address : getAddresses()) {
                copy.addAddress((BorrowerAddress)address.copy());
            }
        }

        // dependent ages
        if (getDependentsAges().length != 0) {
            final int[] source = getDependentsAges();
            final int[] destination = new int[source.length];
            System.arraycopy(source, 0, destination, 0, source.length);
            copy.setDependentsAges(destination);
        }

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
                        && Util.equals(this.type, that.type) && Util.equals(this.numYearsSchool, that.numYearsSchool));
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
     * @return the addresses (never <code>null</code> but can be empty)
     */
    public List<BorrowerAddress> getAddresses() {
        if (this.addresses == null) {
            this.addresses = new ArrayList<BorrowerAddress>();
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
        if (this.numYearsSchool == null) {
            return 0;
        }

        return this.numYearsSchool.doubleValue();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.addresses, this.declarations, this.dependents, this.dependentsAges,
                                             this.dob, this.employmentInformation, this.firstName, this.middleName,
                                             this.lastName, this.maritalStatus, this.phone, this.ssn, this.title,
                                             this.type, this.numYearsSchool});
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        if (event.getSource() instanceof Declarations) {
            firePropertyChange(Properties.DECLARATIONS, event.getOldValue(), event.getNewValue());
        } else if (event.getSource() instanceof Employment) {
            firePropertyChange(Properties.EMPLOYMENT_INFORMATION, event.getOldValue(), event.getNewValue());
        }
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param removeAddress the borrower's address being removed (cannot be <code>null</code>)
     */
    public void removeAddress( final Employer removeAddress ) {
        if (removeAddress == null) {
            throw new NullPointerException();
        }

        if ((this.addresses != null) && this.addresses.remove(removeAddress)) {
            firePropertyChange(Properties.ADDRESSES, removeAddress, null);
        }
    }

    /**
     * @param newDeclarations the new value for the declarations (can be <code>null</code>)
     */
    public void setDeclarations( final Declarations newDeclarations ) {
        if (!Util.equals(this.declarations, newDeclarations)) {
            final Declarations oldValue = this.declarations;
            this.declarations = newDeclarations;
            firePropertyChange(Properties.DECLARATIONS, oldValue, this.declarations);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.declarations != null) {
                this.declarations.add(this);
            }
        }
    }

    /**
     * @param newDependentsAges the new value for the dependentsAges (can be <code>null</code>)
     */
    public void setDependentsAges( final int[] newDependentsAges ) {
        if (!Arrays.equals(this.dependentsAges, newDependentsAges)) {
            final Object oldValue = this.dependentsAges;

            if (newDependentsAges.length == 0) {
                this.dependentsAges = null;
            } else {
                this.dependentsAges = newDependentsAges;
            }

            firePropertyChange(Properties.DEPENDENTS_AGES, oldValue, this.dependentsAges);
        }
    }

    /**
     * @param newDob the new value for the date of birth (can be <code>null</code> or empty)
     */
    public void setDob( final String newDob ) {
        if (!Util.equals(this.dob, newDob)) {
            final Object oldValue = this.dob;
            this.dob = newDob;
            firePropertyChange(Properties.DECLARATIONS, oldValue, this.declarations);
        }
    }

    /**
     * @param newEmploymentInformation the new value for the employment information (can be <code>null</code>)
     */
    public void setEmploymentInformation( final Employment newEmploymentInformation ) {
        if (!Util.equals(this.employmentInformation, newEmploymentInformation)) {
            final Employment oldValue = this.employmentInformation;
            this.employmentInformation = newEmploymentInformation;
            firePropertyChange(Properties.EMPLOYMENT_INFORMATION, oldValue, this.employmentInformation);

            if (oldValue != null) {
                oldValue.remove(this);
            }

            if (this.employmentInformation != null) {
                this.employmentInformation.add(this);
            }
        }
    }

    /**
     * @param newFirstName the new value for the first name (can be <code>null</code> or empty)
     */
    public void setFirstName( final String newFirstName ) {
        if (!Util.equals(this.firstName, newFirstName)) {
            final Object oldValue = this.firstName;
            this.firstName = newFirstName;
            firePropertyChange(Properties.FIRST_NAME, oldValue, this.firstName);
        }
    }

    /**
     * @param newLastName the new value for the lastName (can be <code>null</code> or empty)
     */
    public void setLastName( final String newLastName ) {
        if (!Util.equals(this.lastName, newLastName)) {
            final Object oldValue = this.lastName;
            this.lastName = newLastName;
            firePropertyChange(Properties.LAST_NAME, oldValue, this.lastName);
        }
    }

    /**
     * @param newMaritalStatus the new value for the maritalStatus (can be <code>null</code> or empty)
     */
    public void setMaritalStatus( final String newMaritalStatus ) {
        if (!Util.equals(this.maritalStatus, newMaritalStatus)) {
            final Object oldValue = this.maritalStatus;
            this.maritalStatus = newMaritalStatus;
            firePropertyChange(Properties.MARITAL_STATUS, oldValue, this.maritalStatus);
        }
    }

    /**
     * @param newMiddleName the new value for the middle name (can be <code>null</code> or empty)
     */
    public void setMiddleName( final String newMiddleName ) {
        if (!Util.equals(this.middleName, newMiddleName)) {
            final Object oldValue = this.middleName;
            this.middleName = newMiddleName;
            firePropertyChange(Properties.MIDDLE_NAME, oldValue, this.middleName);
        }
    }

    /**
     * @param newNumberOfDependents the new value for the number of dependents
     */
    public void setNumberOfDependents( final int newNumberOfDependents ) {
        if (this.dependents != newNumberOfDependents) {
            final Object oldValue = this.dependents;
            this.dependents = newNumberOfDependents;
            firePropertyChange(Properties.NUM_DEPENDENTS, oldValue, this.dependents);
        }
    }

    /**
     * @param newPhone the new value for the phone (can be <code>null</code> or empty)
     */
    public void setPhone( final String newPhone ) {
        if (!Util.equals(this.phone, newPhone)) {
            final Object oldValue = this.phone;
            this.phone = newPhone;
            firePropertyChange(Properties.PHONE, oldValue, this.phone);
        }
    }

    /**
     * @param newSsn the new value for the SSN (can be <code>null</code> or empty)
     */
    public void setSsn( final String newSsn ) {
        if (!Util.equals(this.ssn, newSsn)) {
            final Object oldValue = this.ssn;
            this.ssn = newSsn;
            firePropertyChange(Properties.SSN, oldValue, this.ssn);
        }
    }

    /**
     * @param newTitle the new value for the title (can be <code>null</code> or empty)
     */
    public void setTitle( final String newTitle ) {
        if (!Util.equals(this.title, newTitle)) {
            final Object oldValue = this.title;
            this.title = newTitle;
            firePropertyChange(Properties.TITLE, oldValue, this.title);
        }
    }

    /**
     * @param newType the new value for the type (can be <code>null</code> or empty)
     */
    public void setType( final String newType ) {
        if (!Util.equals(this.type, newType)) {
            final Object oldValue = this.type;
            this.type = newType;
            firePropertyChange(Properties.TYPE, oldValue, this.type);
        }
    }

    /**
     * @param newYearsSchool the new value for the yearsSchool
     */
    public void setYearsSchool( final double newYearsSchool ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.numYearsSchool == null) && (newYearsSchool != 0)) {
            changed = true;
            oldValue = this.numYearsSchool;
            this.numYearsSchool = new BigDecimal(newYearsSchool);
            this.numYearsSchool.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.numYearsSchool != null) && (this.numYearsSchool.doubleValue() != newYearsSchool)) {
            changed = true;
            oldValue = this.numYearsSchool;

            if (newYearsSchool == 0) {
                this.numYearsSchool = null;
            } else {
                this.numYearsSchool = new BigDecimal(newYearsSchool);
                this.numYearsSchool.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.NUM_YEARS_SCHOOL, oldValue, this.numYearsSchool);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final Borrower from ) {
        if (from.declarations == null) {
            setDeclarations(null);
        } else {
            setDeclarations(from.getDeclarations());
        }

        if (from.employmentInformation == null) {
            setEmploymentInformation(null);
        } else {
            setEmploymentInformation(from.getEmploymentInformation().copy());
        }

        setNumberOfDependents(from.getNumberOfDependents());
        setDob(from.getDob());
        setFirstName(from.getFirstName());
        setMiddleName(from.getMiddleName());
        setLastName(from.getLastName());
        setMaritalStatus(from.getMaritalStatus());
        setPhone(from.getPhone());
        setSsn(from.getSsn());
        setTitle(from.getTitle());
        setType(from.getType());
        setYearsSchool(from.getYearsSchool());

        { // addresses
            final List<BorrowerAddress> oldValue = this.addresses;

            if ((oldValue != null) && !oldValue.isEmpty()) {
                // unregister
                for (final BorrowerAddress address : oldValue) {
                    address.remove(this);
                }

                oldValue.clear();
            }

            if ((from.addresses == null) || from.addresses.isEmpty()) {
                if ((oldValue != null) && !oldValue.isEmpty()) {
                    this.addresses = null;
                    firePropertyChange(Properties.ADDRESSES, oldValue, null);
                }
            } else {
                for (final BorrowerAddress address : getAddresses()) {
                    addAddress((BorrowerAddress)address.copy());
                }
            }
        }

        // dependent ages
        if (getDependentsAges().length != 0) {
            final int[] source = from.getDependentsAges();
            final int[] destination = new int[source.length];
            System.arraycopy(source, 0, destination, 0, source.length);
            setDependentsAges(destination);
        }
    }

    /**
     * A borrower's property identifiers.
     */
    public interface Properties {

        /**
         * The borrower's addresses property identifier.
         */
        String ADDRESSES = PROPERTY_PREFIX + "addresses"; //$NON-NLS-1$

        /**
         * The borrower's date of birth property identifier.
         */
        String DATE_OF_BIRTH = PROPERTY_PREFIX + "date_of_birth"; //$NON-NLS-1$

        /**
         * The borrower's declarations property identifier.
         */
        String DECLARATIONS = PROPERTY_PREFIX + "declarations"; //$NON-NLS-1$

        /**
         * The borrower's dependent ages property identifier.
         */
        String DEPENDENTS_AGES = PROPERTY_PREFIX + "dependents_ages"; //$NON-NLS-1$

        /**
         * The borrower's employment information property identifier.
         */
        String EMPLOYMENT_INFORMATION = PROPERTY_PREFIX + "employment_information"; //$NON-NLS-1$

        /**
         * The borrower's first name property identifier.
         */
        String FIRST_NAME = PROPERTY_PREFIX + "first_name"; //$NON-NLS-1$

        /**
         * The borrower's last name property identifier.
         */
        String LAST_NAME = PROPERTY_PREFIX + "last_name"; //$NON-NLS-1$

        /**
         * The borrower's marital status property identifier.
         */
        String MARITAL_STATUS = PROPERTY_PREFIX + "marital_status"; //$NON-NLS-1$

        /**
         * The borrower's middle name property identifier.
         */
        String MIDDLE_NAME = PROPERTY_PREFIX + "middle_name"; //$NON-NLS-1$

        /**
         * The borrower's dependents property identifier.
         */
        String NUM_DEPENDENTS = PROPERTY_PREFIX + "num_dependents"; //$NON-NLS-1$

        /**
         * The borrower's number of years in school property identifier.
         */
        String NUM_YEARS_SCHOOL = PROPERTY_PREFIX + "num_years_school"; //$NON-NLS-1$

        /**
         * The borrower's phone number property identifier.
         */
        String PHONE = PROPERTY_PREFIX + "phone"; //$NON-NLS-1$

        /**
         * The borrower's SSN property identifier.
         */
        String SSN = PROPERTY_PREFIX + "ssn"; //$NON-NLS-1$

        /**
         * The borrower's title property identifier.
         */
        String TITLE = PROPERTY_PREFIX + "title"; //$NON-NLS-1$

        /**
         * The borrower's type property identifier.
         */
        String TYPE = PROPERTY_PREFIX + "type"; //$NON-NLS-1$

    }

}
