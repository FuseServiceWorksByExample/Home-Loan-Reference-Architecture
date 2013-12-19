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
package org.jboss.demo.loanmanagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;

/**
 * Loan management app utilities.
 */
public final class Util {

    /**
     * A data regex pattern.
     */
    public static final String DATE_PATTER = "[0-1]?[0-9]/[0-3]?[0-9]/[12][0-9][0-9][0-9]"; //$NON-NLS-1$

    /**
     * An empty string constant.
     */
    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private static InputFilter _currencyFilter = null;

    /**
     * @param context the app context (cannot be <code>null</code>)
     * @param title the dialog title
     * @param message the dialog message
     * @return the progress dialog (never <code>null</code>)
     */
    public static ProgressDialog createProgressDialog( final Context context,
                                                       final String title,
                                                       final String message ) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setIcon(R.drawable.ic_home);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);

        return dialog;
    }

    /**
     * @param thisObj the first object being compared (can be <code>null</code>)
     * @param thatObj the object being compared to the first object (can be <code>null</code>)
     * @return <code>true</code> if both objects are <code>null</code> or equal
     */
    public static boolean equals( final Object thisObj,
                                  final Object thatObj ) {
        if (thisObj == thatObj) {
            return true;
        }

        if (thisObj == null) {
            return (thatObj == null);
        }

        if (thatObj == null) {
            return false;
        }

        return thisObj.equals(thatObj);
    }

    /**
     * @param ssn the SSN being formatted
     * @return the formatted SSN (never <code>null</code>)
     */
    public static String formatSsn( final int ssn ) {
        final StringBuilder builder = new StringBuilder(Integer.toString(ssn));
        return builder.insert(5, '-').insert(3, '-').toString();
    }

    /**
     * @return a currency input filter (never <code>null</code>)
     */
    public static InputFilter getCurrencyFilter() {
        if (_currencyFilter == null) {
            _currencyFilter = new CurrencyFormatInputFilter();
        }

        return _currencyFilter;
    }

    /**
     * Don't allow public construction.
     */
    private Util() {
        // nothing to do
    }

    protected static class CurrencyFormatInputFilter implements InputFilter {

        Pattern pattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?"); //$NON-NLS-1$

        /**
         * @see android.text.InputFilter#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
         */
        @Override
        public CharSequence filter( final CharSequence source,
                                    final int start,
                                    final int end,
                                    final Spanned dest,
                                    final int dstart,
                                    final int dend ) {
            final String result =
                            dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
            final Matcher matcher = this.pattern.matcher(result);

            if (matcher.matches()) {
                return null;
            }

            return dest.subSequence(dstart, dend);
        }
    }

}
