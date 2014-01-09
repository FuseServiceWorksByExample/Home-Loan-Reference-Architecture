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
import java.util.Arrays;
import java.util.Comparator;
import org.jboss.demo.loanmanagement.Util;

/**
 * A home loan evaluation model object.
 */
public final class Evaluation {

    /**
     * Sorts evaluations by applicant name.
     */
    public static final Comparator<Evaluation> NAME_SORTER = new Comparator<Evaluation>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final Evaluation thisEval,
                            final Evaluation thatEval ) {
            return thisEval.getApplicant().compareTo(thatEval.getApplicant());
        }
    };

    /**
     * An empty array.
     */
    public static final Evaluation[] NO_EVALUATIONS = new Evaluation[0];

    /**
     * An empty list.
     */
    public static final ArrayList<EvaluationParcelable> NO_EVALUATIONS_LIST = new ArrayList<EvaluationParcelable>(0);

    /**
     * A value indicating the SSN has not be set.
     */
    public static final int SSN_NOT_SET = -1;

    /**
     * Sorts evaluations by SSN.
     */
    public static final Comparator<Evaluation> SSN_SORTER = new Comparator<Evaluation>() {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare( final Evaluation thisEval,
                            final Evaluation thatEval ) {
            return (thisEval.getSsn() - thatEval.getSsn());
        }
    };

    /**
     * @param original the evaluation being copied (cannot be <code>null</code>)
     * @return the copy (never <code>null</code>)
     */
    public static Evaluation copy( final Evaluation original ) {
        final Evaluation copy = new Evaluation(original.ssn, original.applicant, original.creditScore);
        copy.insuranceCost = original.insuranceCost;
        copy.explanation = original.explanation;
        copy.approved = original.approved;

        return copy;
    }

    private final String applicant;
    private boolean approved;
    private final int creditScore;
    private String explanation;
    private BigDecimal insuranceCost;
    private BigDecimal rate;
    private final int ssn;

    /**
     * @param evaluatonSsn the applicant's SSN
     * @param evaluationApplicant the applicant's name
     * @param evaluationCreditScore the applicant's credit score
     */
    public Evaluation( final int evaluatonSsn,
                       final String evaluationApplicant,
                       final int evaluationCreditScore ) {
        this.applicant = evaluationApplicant;
        this.ssn = evaluatonSsn;
        this.creditScore = evaluationCreditScore;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj ) {
        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        final Evaluation that = (Evaluation)obj;
        return (Util.equals(this.ssn, that.ssn) && Util.equals(this.applicant, that.applicant)
                        && Util.equals(this.creditScore, that.creditScore)
                        && Util.equals(this.rate, that.rate)
                        && Util.equals(this.insuranceCost, that.insuranceCost)
                        && Util.equals(this.explanation, that.explanation) && Util.equals(this.approved, that.approved));
    }

    /**
     * @return the applicant
     */
    public String getApplicant() {
        return this.applicant;
    }

    /**
     * @return the credit score
     */
    public int getCreditScore() {
        return this.creditScore;
    }

    /**
     * @return the explanation
     */
    public String getExplanation() {
        return this.explanation;
    }

    /**
     * @return the insurance cost
     */
    public double getInsuranceCost() {
        if (this.insuranceCost == null) {
            return 0;
        }

        return this.insuranceCost.doubleValue();
    }

    /**
     * @return the loan rate
     */
    public double getRate() {
        if (this.rate == null) {
            return 0;
        }

        return this.rate.doubleValue();
    }

    /**
     * @return the SSN
     */
    public int getSsn() {
        return this.ssn;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {this.ssn, this.applicant, this.creditScore, this.rate, this.insuranceCost,
                                             this.explanation, this.approved

        });
    }

    /**
     * @return <code>true</code> if approved
     */
    public boolean isApproved() {
        return this.approved;
    }

    /**
     * @param newApproved the new value for the approved
     */
    public void setApproved( final boolean newApproved ) {
        this.approved = newApproved;

        if (!this.approved) {
            setInsuranceCost(0);
            setRate(0);
        }
    }

    /**
     * @param newExplanation the new value for the explanation
     */
    public void setExplanation( final String newExplanation ) {
        this.explanation = newExplanation;
    }

    /**
     * @param newInsuranceCost the new value for the insurance cost
     */
    public void setInsuranceCost( final double newInsuranceCost ) {
        if ((this.insuranceCost == null) || (this.insuranceCost.doubleValue() != newInsuranceCost)) {
            if (newInsuranceCost == 0) {
                this.insuranceCost = null;
            } else {
                this.insuranceCost = new BigDecimal(newInsuranceCost);
                this.insuranceCost.setScale(2, RoundingMode.HALF_EVEN);
            }
        }
    }

    /**
     * @param newRate the new value for the loan rate
     */
    public void setRate( final double newRate ) {
        if ((this.rate == null) || (this.rate.doubleValue() != newRate)) {
            if (newRate == 0) {
                this.rate = null;
            } else {
                this.rate = new BigDecimal(newRate);
                this.rate.setScale(4, RoundingMode.HALF_EVEN);
            }
        }
    }

}
