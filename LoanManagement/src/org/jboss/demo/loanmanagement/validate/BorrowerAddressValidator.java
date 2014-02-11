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
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Address;
import org.jboss.demo.loanmanagement.model.BorrowerAddress;

/**
 * An {@link BorrowerAddress address} validator.
 */
public final class BorrowerAddressValidator implements Validator<BorrowerAddress> {

    /**
     * The shared {@link Address address} validator.
     */
    public static final BorrowerAddressValidator SHARED = new BorrowerAddressValidator();

    /**
     * Don't allow construction outside this class.
     */
    private BorrowerAddressValidator() {
        // nothing to do
    }

    /**
     * @see org.jboss.demo.loanmanagement.validate.Validator#validate(org.jboss.demo.loanmanagement.model.ModelObject)
     * @see AddressValidator
     */
    @Override
    public List<ValidationError> validate( final BorrowerAddress address ) {
        List<ValidationError> errors = AddressValidator.SHARED.validate(address);

        if (errors.contains(ValidationError.NULL)) {
            return errors;
        }

        // validate type if one exists
        final String type = address.getType();

        if (!Util.isBlank(type)) {
            for (final String addressType : BorrowerAddress.ADDRESS_TYPES) {
                if (addressType.equals(type)) {
                    return errors;
                }
            }
        }

        errors = new ArrayList<ValidationError>(errors);
        errors.add(BorrowerAddressError.TYPE);

        return errors;
    }

    /**
     * Account validation errors.
     */
    public enum BorrowerAddressError implements ValidationError {

        /**
         * An invalid borrower address type.
         */
        TYPE(R.string.err_invalid_borrower_address_type);

        private final int msgId;

        private BorrowerAddressError( final int errorMessageId ) {
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
