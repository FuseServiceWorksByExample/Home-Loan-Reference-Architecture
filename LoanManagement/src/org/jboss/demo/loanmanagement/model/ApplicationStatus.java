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

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A loan application status model object.
 */
public final class ApplicationStatus {

    /**
     * Sorts application status by applicant name.
     */
    public static final Comparator<ApplicationStatus> NAME_SORTER = new Comparator<ApplicationStatus>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ApplicationStatus thisStatus,
                            final ApplicationStatus thatStatus ) {
            return thisStatus.getApplicant().compareTo(thatStatus.getApplicant());
        }
    };

    /**
     * Sorts application status by SSN.
     */
    public static final Comparator<ApplicationStatus> SSN_SORTER = new Comparator<ApplicationStatus>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ApplicationStatus thisStatus,
                            final ApplicationStatus thatStatus ) {
            return (thisStatus.getSsn() - thatStatus.getSsn());
        }
    };

    /**
     * Sorts application status by loan rate.
     */
    public static final Comparator<ApplicationStatus> RATE_SORTER = new Comparator<ApplicationStatus>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ApplicationStatus thisStatus,
                            final ApplicationStatus thatStatus ) {
            return Float.compare(thisStatus.getRate(), thatStatus.getRate());
        }
    };

    /**
     * Sorts application status by application status.
     */
    public static final Comparator<ApplicationStatus> STATUS_SORTER = new Comparator<ApplicationStatus>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ApplicationStatus thisStatus,
                            final ApplicationStatus thatStatus ) {
            return thisStatus.getStatus().compareTo(thatStatus.getStatus());
        }
    };

    /**
     * An empty array.
     */
    public static final ApplicationStatus[] NO_STATUSES = new ApplicationStatus[0];

    /**
     * An empty list.
     */
    public static final ArrayList<ApplicationStatusParcelable> NO_STATUSES_LIST =
                    new ArrayList<ApplicationStatusParcelable>(0);

    private final int ssn;
    private final String applicant;
    private final float rate;
    private final String status;

    /**
     * @param applicantSsn the SSN of the loan applicant
     * @param applicantName the name of the applicant
     * @param loanRate the rate of the loan
     * @param loanStatus the status of the loan
     */
    public ApplicationStatus( final int applicantSsn,
                              final String applicantName,
                              final float loanRate,
                              final String loanStatus ) {
        this.ssn = applicantSsn;
        this.applicant = applicantName;
        this.rate = loanRate;
        this.status = loanStatus;
    }

    /**
     * @return the applicant name
     */
    public String getApplicant() {
        return this.applicant;
    }

    /**
     * @return the rate
     */
    public float getRate() {
        return this.rate;
    }

    /**
     * @return the SSN
     */
    public int getSsn() {
        return this.ssn;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

}
