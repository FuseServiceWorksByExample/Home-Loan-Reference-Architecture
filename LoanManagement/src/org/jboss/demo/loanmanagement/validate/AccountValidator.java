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
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.model.Account;
import org.jboss.demo.loanmanagement.model.Asset;

/**
 * An {@link Account account} validator.
 */
public final class AccountValidator extends AssetValidator<Account> {

    /**
     * @see org.jboss.demo.loanmanagement.validate.AssetValidator#validate(org.jboss.demo.loanmanagement.model.Asset)
     */
    @Override
    public List<ValidationError> validate( final Asset<Account> asset ) {
        List<ValidationError> errors = super.validate(asset);

        if (errors.contains(ValidationError.NULL)) {
            return errors;
        }

        final Account account = (Account)asset;
        boolean numberError = false;
        final boolean addressError = false;

        if ((account.getNumber() == null) || account.getNumber().trim().isEmpty()) {
            numberError = true;
        }

        // TODO call address validator here with account address

        if (numberError || addressError) {
            errors = new ArrayList<ValidationError>(errors);

            if (numberError) {
                errors.add(AccountError.NUMBER);
            }
        }

        return errors;
    }

    /**
     * Account validation errors.
     */
    public enum AccountError implements ValidationError {

        /**
         * An invalid number.
         */
        NUMBER(R.string.err_invalid_account_number);

        private final int msgId;

        private AccountError( final int errorMessageId ) {
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
