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
import org.jboss.demo.loanmanagement.model.Asset;

/**
 * An {@link Asset asset} validator.
 * 
 * @param <T> the asset type
 */
public abstract class AssetValidator<T> implements Validator<Asset<T>> {

    protected AssetValidator() {
        // nothing to do
    }

    /**
     * @see org.jboss.demo.loanmanagement.validate.Validator#validate(org.jboss.demo.loanmanagement.model.ModelObject)
     */
    @Override
    public List<ValidationError> validate( final Asset<T> asset ) {
        if (asset == null) {
            return Collections.singletonList(ValidationError.NULL);
        }

        boolean amountError = false;
        boolean descriptionError = false;

        if (asset.getAmount() <= 0) {
            amountError = true;
        }

        if ((asset.getDescription() == null) || asset.getDescription().trim().isEmpty()) {
            descriptionError = true;
        }

        if (amountError || descriptionError) {
            final List<ValidationError> errors = new ArrayList<ValidationError>(2);

            if (amountError) {
                errors.add(AssetError.AMOUNT);
            }

            if (descriptionError) {
                errors.add(AssetError.DESCRIPTION);
            }

            return errors;
        }

        return ValidationError.NO_ERRORS;
    }

    /**
     * Asset validation errors.
     */
    public enum AssetError implements ValidationError {

        /**
         * An invalid amount.
         */
        AMOUNT(R.string.err_invalid_amount),

        /**
         * An invalid description.
         */
        DESCRIPTION(R.string.err_invalid_description);

        private final int msgId;

        private AssetError( final int errorMessageId ) {
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
