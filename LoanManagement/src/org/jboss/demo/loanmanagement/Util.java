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

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Loan management app utilities.
 */
public final class Util {

    /**
     * An empty string constant.
     */
    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    /**
     * @param context the app context (cannot be <code>null</code>)
     * @param title the dialog title
     * @param message the dialog message
     * @return the progress dialog (never <code>null</code>)
     */
    public static final ProgressDialog createProgressDialog( final Context context,
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
     * @param ssn the SSN being formatted
     * @return the formatted SSN (never <code>null</code>)
     */
    public static String formatSsn( final int ssn ) {
        final StringBuilder builder = new StringBuilder(Integer.toString(ssn));
        return builder.insert(5, '-').insert(3, '-').toString();
    }

    /**
     * Don't allow public construction.
     */
    private Util() {
        // nothing to do
    }

}
