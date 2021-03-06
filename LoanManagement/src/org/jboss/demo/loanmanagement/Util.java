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
package org.jboss.demo.loanmanagement;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Spinner;

/**
 * Loan management app utilities.
 */
public final class Util {

    private static InputFilter _currencyFilter = null;

    /**
     * A data regex pattern.
     */
    public static final String DATE_PATTERN = "[0-1]?[0-9]/[0-3]?[0-9]/[12][0-9][0-9][0-9]"; //$NON-NLS-1$

    /**
     * An empty string constant.
     */
    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    private static final DecimalFormat FORMATTER = new DecimalFormat("#.00"); //$NON-NLS-1$

    static final Pattern MONEY_PATTERN = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?"); //$NON-NLS-1$

    private static final String PREFS_NAME = "loanmanagement"; //$NON-NLS-1$

    /**
     * @param original the source array (cannot be <code>null</code>)
     * @return the array copy (never <code>null</code>)
     */
    public static int[] copy( final int[] original ) {
        final int[] copy = new int[original.length];

        for (int i = 0; i < original.length; ++i) {
            copy[i] = original[i];
        }

        return copy;
    }

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
     * @param amount the amount being formatted
     * @return the text in the form of xx.xx (never <code>null</code>)
     */
    public static String formatMoneyAmount( final double amount ) {
        return FORMATTER.format(amount);
    }

    /**
     * @param text the text being formatted (can be <code>null</code> or empty)
     * @return the text in the form of xx.xx (never <code>null</code>)
     * @throws ParseException if input parameter cannot be parsed as a double
     */
    public static String formatMoneyAmount( final String text ) throws ParseException {
        return formatMoneyAmount(parseDouble(text));
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
     * @param ssn the SSN being formatted
     * @return the formatted SSN with all characters but the last 4 being masked (never <code>null</code>)
     */
    public static String formatSsnWithMask( final int ssn ) {
        final StringBuilder builder = new StringBuilder(Integer.toString(ssn));
        return builder.replace(0, 5, "*****").insert(5, '-').insert(3, '-').toString(); //$NON-NLS-1$
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
     * @param context the context whose preferences editor is being requested (cannot be <code>null</code>)
     * @return the preferences editor (never <code>null</code>)
     */
    public static SharedPreferences.Editor getPreferenceEditor( final Context context ) {
        return getPreferences(context).edit();
    }

    /**
     * This can be used when just querying the preferences.
     * 
     * @param context the context whose preferences is being requested (cannot be <code>null</code>)
     * @return the preferences (never <code>null</code>)
     */
    public static SharedPreferences getPreferences( final Context context ) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * @param text the text being checked (can be <code>null</code> or empty)
     * @return <code>true</code> if text is <code>null</code>, empty, or empty when trimmed
     */
    public static boolean isBlank( final String text ) {
        if (TextUtils.isEmpty(text)) {
            return true;
        }

        return text.trim().isEmpty();
    }

    /**
     * @param text the text being parsed (if <code>null</code> or empty the value will be zero)
     * @return the double value
     * @throws ParseException if text cannot be parsed as a double value
     */
    public static double parseDouble( final String text ) throws ParseException {
        if (Util.isBlank(text)) {
            return 0;
        }

        final NumberFormat format = NumberFormat.getInstance();
        final Number number = format.parse(text);
        return number.doubleValue();
    }

    /**
     * @param text the text being parsed (if <code>null</code> or empty the value will be zero)
     * @return the double value
     * @throws ParseException if text cannot be parsed as an integer value
     */
    public static int parseInt( final String text ) throws ParseException {
        if (Util.isBlank(text)) {
            return 0;
        }

        final NumberFormat format = NumberFormat.getInstance();
        final Number number = format.parse(text);
        return number.intValue();
    }

    /**
     * @param spinner the spinner whose item is being selected (cannot be <code>null</code>)
     * @param value the value to select in the spinner (can be <code>null</code> or empty)
     * @param validValues a list of valid values (cannot be <code>null</code>)
     */
    public static void selectSpinnerItem( final Spinner spinner,
                                          final String value,
                                          final String[] validValues ) {
        if (!Util.isBlank(value)) {
            int index = -1;
            int i = 0;

            for (final String validValue : validValues) {
                if (validValue.equals(value)) {
                    index = i;
                    break;
                }

                ++i;
            }

            if (index != -1) {
                spinner.setSelection(index);
            }
        }
    }

    /**
     * Don't allow public construction.
     */
    private Util() {
        // nothing to do
    }

    protected static class CurrencyFormatInputFilter implements InputFilter {

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
            final Matcher matcher = MONEY_PATTERN.matcher(result);

            if (matcher.matches()) {
                return null;
            }

            return dest.subSequence(dstart, dend);
        }
    }

    /**
     * Constants associated with preferences.
     */
    public interface Prefs {

        /**
         * The preference for sorting application evaluations.
         */
        String EVALUATION_SORTER = "evaluation.sorter"; //$NON-NLS-1$

        /**
         * A value that indicates the data should be sorted by date.
         */
        String SORT_BY_DATE = "sort_by_date"; //$NON-NLS-1$

        /**
         * A value that indicates the data should be sorted by name.
         */
        String SORT_BY_NAME = "sort_by_name"; //$NON-NLS-1$

        /**
         * A value that indicates the data should be sorted by rate.
         */
        String SORT_BY_RATE = "sort_by_rate"; //$NON-NLS-1$

        /**
         * A value that indicates the data should be sorted by SSN.
         */
        String SORT_BY_SSN = "sort_by_ssn"; //$NON-NLS-1$

        /**
         * A value that indicates the data should be sorted by status.
         */
        String SORT_BY_STATUS = "sort_by_status"; //$NON-NLS-1$

        /**
         * The preference for sorting application statuses.
         */
        String STATUS_SORTER = "status.sorter"; //$NON-NLS-1$

    }

}
