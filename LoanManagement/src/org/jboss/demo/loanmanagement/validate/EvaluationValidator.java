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
package org.jboss.demo.loanmanagement.validate;

import org.jboss.demo.loanmanagement.model.Evaluation;

/**
 * An evaluation validator.
 */
public final class EvaluationValidator {

    /**
     * @param evaluation the evaluation being validated (can be <code>null</code>)
     * @return <code>true</code> if not <code>null</code> and all fields are valid
     */
    public static boolean isValid( final Evaluation evaluation ) {
        if (evaluation == null) {
            return false;
        }

        if ((evaluation.getSsn() < 100000000) || (evaluation.getSsn() > 999999999)) {
            return false;
        }

        if ((evaluation.getApplicant() == null) || evaluation.getApplicant().isEmpty()) {
            return false;
        }

        if (evaluation.getRate() < 0.0f) {
            return false;
        }

        if ((evaluation.getCreditScore() < 0) || (evaluation.getCreditScore() > 999)) {
            return false;
        }

        return true;
    }

    /**
     * Don't allow public construction.
     */
    private EvaluationValidator() {
        // nothing to do
    }

}
