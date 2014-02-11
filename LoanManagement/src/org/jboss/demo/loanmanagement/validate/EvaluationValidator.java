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
package org.jboss.demo.loanmanagement.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Evaluation;

/**
 * An evaluation validator.
 */
public final class EvaluationValidator implements Validator<Evaluation> {

    /**
     * The shared {@link Evaluation} validator.
     */
    public static final EvaluationValidator SHARED = new EvaluationValidator();

    /**
     * Don't allow public construction.
     */
    private EvaluationValidator() {
        // nothing to do
    }

    /**
     * @see org.jboss.demo.loanmanagement.validate.Validator#validate(org.jboss.demo.loanmanagement.model.ModelObject)
     */
    @Override
    public List<ValidationError> validate( final Evaluation evaluation ) {
        if (evaluation == null) {
            return Collections.singletonList(ValidationError.NULL);
        }

        final List<ValidationError> result = new ArrayList<ValidationError>(5);

        if ((evaluation.getSsn() < 100000000) || (evaluation.getSsn() > 999999999)) {
            result.add(EvaluationError.SSN);
        }

        if (Util.isBlank(evaluation.getApplicant())) {
            result.add(EvaluationError.APPLICANT);
        }

        if ((evaluation.getCreditScore() < 0) || (evaluation.getCreditScore() > 999)) {
            result.add(EvaluationError.CREDIT_SCORE);
        }

        if (Util.isBlank(evaluation.getExplanation())) {
            result.add(EvaluationError.EXPLANATION);
        }

        if (evaluation.isApproved() && (evaluation.getRate() < 0.0f)) {
            result.add(EvaluationError.RATE);
        }

        if (evaluation.isApproved() && (evaluation.getInsuranceCost() < 0.0f)) {
            result.add(EvaluationError.INSURANCE_COST);
        }

        return result;
    }

    /**
     * Evaluation validation errors.
     */
    public enum EvaluationError implements ValidationError {

        /**
         * The applicant is not valid.
         */
        APPLICANT(R.string.err_eval_applicant),

        /**
         * The credit score is not valid.
         */
        CREDIT_SCORE(R.string.err_eval_credit_score),

        /**
         * The explanation is not valid.
         */
        EXPLANATION(R.string.err_eval_explanation),

        /**
         * The insurance cost is not valid.
         */
        INSURANCE_COST(R.string.err_eval_insurance_cost),

        /**
         * The evaluation is <code>null</code>.
         */
        NULL(R.string.err_null_model_object),

        /**
         * The loan rate is not valid.
         */
        RATE(R.string.err_eval_rate),

        /**
         * The SSN is not valid.
         */
        SSN(R.string.err_eval_ssn);

        private final int msgId;

        private EvaluationError( final int errorMessageId ) {
            this.msgId = errorMessageId;
        }

        /**
         * @see org.jboss.demo.loanmanagement.validate.ValidationError#getMessageId()
         */
        @Override
        public int getMessageId() {
            return this.msgId;
        }

    }

}
