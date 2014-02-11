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
import org.jboss.demo.loanmanagement.model.Address;

/**
 * An {@link Address address} validator.
 */
public class AddressValidator implements Validator<Address> {

    /**
     * The shared {@link Address address} validator.
     */
    public static final AddressValidator SHARED = new AddressValidator();

    /**
     * Don't allow construction outside this class.
     */
    private AddressValidator() {
        // nothing to do
    }

    /**
     * @see org.jboss.demo.loanmanagement.validate.Validator#validate(org.jboss.demo.loanmanagement.model.ModelObject)
     */
    @Override
    public List<ValidationError> validate( final Address address ) {
        if (address == null) {
            return Collections.singletonList(ValidationError.NULL);
        }

        boolean cityError = false;

        { // city
            final String city = address.getCity();

            if ((city == null) || city.trim().isEmpty()) {
                cityError = true;
            }
        }

        boolean line1Error = false;

        { // line 1
            final String line1 = address.getLine1();

            if ((line1 == null) || line1.trim().isEmpty()) {
                line1Error = true;
            }
        }

        boolean stateError = false;

        { // state
            final String state = address.getState();

            if ((state == null) || state.trim().isEmpty()) {
                stateError = true;
            }
        }

        if (!cityError && !line1Error && !stateError) {
            return ValidationError.NO_ERRORS;
        }

        final List<ValidationError> errors = new ArrayList<ValidationError>(3);

        if (cityError) {
            errors.add(AddressError.CITY);
        }

        if (line1Error) {
            errors.add(AddressError.LINE_1);
        }

        if (stateError) {
            errors.add(AddressError.STATE);
        }

        return errors;
    }

    /**
     * Address validation errors.
     */
    public enum AddressError implements ValidationError {

        /**
         * An invalid city.
         */
        CITY(R.string.err_invalid_address_city),

        /**
         * An invalid line 1.
         */
        LINE_1(R.string.err_invalid_address_line_1),

        /**
         * An invalid line 1.
         */
        STATE(R.string.err_invalid_address_state);

        private final int msgId;

        private AddressError( final int errorMessageId ) {
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
