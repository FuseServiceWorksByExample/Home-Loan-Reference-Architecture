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
 * A home loan evaluation.
 */
public final class Evaluation {

    /**
     * An empty array.
     */
    public static final Evaluation[] NO_EVALUATIONS = new Evaluation[0];

    /**
     * An empty list.
     */
    public static final ArrayList<EvaluationParcelable> NO_EVALUATIONS_LIST = new ArrayList<EvaluationParcelable>(0);

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
    public static final Evaluation[] EMPTY_ARRAY = new Evaluation[0];

    /**
     * The value when a rate has not been set.
     */
    public static final float NOT_SET = -1.0f;

    private final int ssn;
    private final String applicant;
    private final int creditScore;
    private float rate = NOT_SET;
    private int insuranceCost;
    private String explanation;
    private boolean approved;

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
    public int getInsuranceCost() {
        return this.insuranceCost;
    }

    /**
     * @return the loan rate or {@link #NOT_SET} if rate has not been set
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
    public void setInsuranceCost( final int newInsuranceCost ) {
        this.insuranceCost = newInsuranceCost;
    }

    /**
     * @param newRate the new value for the loan rate
     */
    public void setRate( final float newRate ) {
        this.rate = newRate;
    }

}
