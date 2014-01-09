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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import android.text.TextUtils;

/**
 * A loan application status model object.
 */
public final class ApplicationStatus {

    private static final String APPROVED = "APPROVED"; //$NON-NLS-1$
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
     * An empty array.
     */
    public static final ApplicationStatus[] NO_STATUSES = new ApplicationStatus[0];

    /**
     * An empty list.
     */
    public static final ArrayList<ApplicationStatusParcelable> NO_STATUSES_LIST =
                    new ArrayList<ApplicationStatusParcelable>(0);

    private static final String PENDING = "PENDING"; //$NON-NLS-1$

    /**
     * A value indicating the rate has not be set.
     */
    public static final double RATE_NOT_SET = -1;

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
            return Double.compare(thisStatus.getRate(), thatStatus.getRate());
        }
    };

    private static final String REJECTED = "REJECTED"; //$NON-NLS-1$

    /**
     * A value indicating the SSN has not be set.
     */
    public static final int SSN_NOT_SET = -1;

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
     * Sorts application status by application status.
     */
    public static final Comparator<ApplicationStatus> STATUS_SORTER = new Comparator<ApplicationStatus>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final ApplicationStatus thisStatus,
                            final ApplicationStatus thatStatus ) {
            if (thisStatus.isApproved()) {
                if (thatStatus.isApproved()) {
                    return 0;
                }

                return (thatStatus.isRejected() ? -1 : -10);
            }

            if (thisStatus.isRejected()) {
                if (thatStatus.isRejected()) {
                    return 0;
                }

                return (thatStatus.isApproved() ? 1 : -1);
            }

            if (thatStatus.isPending()) {
                return 0;
            }

            return (thatStatus.isApproved() ? 10 : 1);
        }
    };

    private final String applicant;
    private final BigDecimal rate;
    private final int ssn;
    private final String status;

    /**
     * @param applicantSsn the SSN of the loan applicant
     * @param applicantName the name of the applicant
     * @param loanRate the rate of the loan
     * @param loanStatus the status of the loan
     */
    public ApplicationStatus( final int applicantSsn,
                              final String applicantName,
                              final double loanRate,
                              final String loanStatus ) {
        this.ssn = applicantSsn;
        this.applicant = applicantName;
        this.rate = new BigDecimal(loanRate);
        this.rate.setScale(2, RoundingMode.HALF_EVEN);
        this.status = (TextUtils.isEmpty(loanStatus) ? PENDING : loanStatus);
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
    public double getRate() {
        return this.rate.doubleValue();
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

    /**
     * @return <code>true</code> if the application has been approved
     */
    public boolean isApproved() {
        if (!TextUtils.isEmpty(this.status)) {
            if (APPROVED.equalsIgnoreCase(this.status)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return <code>true</code> if the application has been rejected
     */
    public boolean isPending() {
        return (!isApproved() && !isRejected());
    }

    /**
     * @return <code>true</code> if the application has been rejected
     */
    public boolean isRejected() {
        if (!TextUtils.isEmpty(this.status)) {
            if (REJECTED.equalsIgnoreCase(this.status)) {
                return true;
            }
        }

        return false;
    }

}
