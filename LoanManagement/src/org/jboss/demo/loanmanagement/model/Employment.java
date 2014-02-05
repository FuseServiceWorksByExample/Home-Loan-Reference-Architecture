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
import java.util.Arrays;
import java.util.List;
import org.jboss.demo.loanmanagement.Util;

/**
 * Employment information model object.
 */
public final class Employment implements ModelObject<Employment>, PropertyChangeListener {

    protected static final String PROPERTY_PREFIX = Employment.class.getSimpleName() + '.';

    private List<Employer> employers; // max 3
    private final PropertyChangeSupport pcs;
    private BigDecimal yearsInProfession;

    /**
     * Constructs an employment.
     */
    public Employment() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param listener the listener registering to receive property change events (cannot be <code>null</code>)
     */
    public void add( final PropertyChangeListener listener ) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param newEmployer the employer being added (cannot be <code>null</code>)
     */
    public void addEmployer( final Employer newEmployer ) {
        if (newEmployer == null) {
            throw new NullPointerException();
        }

        if (getEmployers().add(newEmployer)) {
            newEmployer.add(this);
            firePropertyChange(Properties.EMPLOYERS, null, newEmployer);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public Employment copy() {
        final Employment copy = new Employment();
        copy.setYearsInProfession(getYearsInProfession());

        // employers
        if ((this.employers != null) && !this.employers.isEmpty()) {
            for (final Employer employer : getEmployers()) {
                copy.addEmployer(employer.copy());
            }
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

        final Employment that = (Employment)obj;
        return (Util.equals(this.employers, that.employers) && Util.equals(this.yearsInProfession,
                                                                           that.yearsInProfession));
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
     * @return the employers (never <code>null</code>)
     */
    public List<Employer> getEmployers() {
        if (this.employers == null) {
            return Employer.NONE;
        }

        return this.employers;
    }

    /**
     * @return the years in profession
     */
    public double getYearsInProfession() {
        if (this.yearsInProfession == null) {
            return 0;
        }

        return this.yearsInProfession.doubleValue();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.employers, this.yearsInProfession});
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange( final PropertyChangeEvent event ) {
        firePropertyChange(Properties.EMPLOYERS, event.getOldValue(), event.getNewValue());
    }

    /**
     * @param listener the listener unregistering from receiving property change events (cannot be <code>null</code>)
     */
    public void remove( final PropertyChangeListener listener ) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param removeEmployer the employer being removed (cannot be <code>null</code>)
     */
    public void removeEmployer( final Employer removeEmployer ) {
        if (removeEmployer == null) {
            throw new NullPointerException();
        }

        if ((this.employers != null) && this.employers.remove(removeEmployer)) {
            firePropertyChange(Properties.EMPLOYERS, removeEmployer, null);
        }
    }

    /**
     * @param newYearsInProfession the new value for the years in current profession
     */
    public void setYearsInProfession( final double newYearsInProfession ) {
        boolean changed = false;
        Object oldValue = null;

        if ((this.yearsInProfession == null) && (newYearsInProfession != 0)) {
            changed = true;
            oldValue = this.yearsInProfession;
            this.yearsInProfession = new BigDecimal(newYearsInProfession);
            this.yearsInProfession.setScale(2, RoundingMode.HALF_EVEN);
        } else if ((this.yearsInProfession != null) && (this.yearsInProfession.doubleValue() != newYearsInProfession)) {
            changed = true;
            oldValue = this.yearsInProfession;

            if (newYearsInProfession == 0) {
                this.yearsInProfession = null;
            } else {
                this.yearsInProfession = new BigDecimal(newYearsInProfession);
                this.yearsInProfession.setScale(2, RoundingMode.HALF_EVEN);
            }
        }

        if (changed) {
            firePropertyChange(Properties.YEARS_IN_PROFESSION, oldValue, this.yearsInProfession);
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final Employment from ) {
        setYearsInProfession(from.getYearsInProfession());

        // employers
        final List<Employer> oldValue = this.employers;

        if ((oldValue != null) && !oldValue.isEmpty()) {
            // unregister
            for (final Employer employer : oldValue) {
                employer.remove(this);
            }

            oldValue.clear();
        }

        if ((from.employers == null) || from.employers.isEmpty()) {
            if ((oldValue != null) && !oldValue.isEmpty()) {
                this.employers = null;
                firePropertyChange(Properties.EMPLOYERS, oldValue, null);
            }
        } else {
            for (final Employer employer : from.getEmployers()) {
                addEmployer(employer.copy());
            }
        }
    }

    /**
     * An employment's property identifiers.
     */
    public interface Properties {

        /**
         * The employment's employers property identifier.
         */
        String EMPLOYERS = PROPERTY_PREFIX + "employers"; //$NON-NLS-1$

        /**
         * The employment's years in profession property identifier.
         */
        String YEARS_IN_PROFESSION = PROPERTY_PREFIX + "years_in_profession"; //$NON-NLS-1$

    }

}
