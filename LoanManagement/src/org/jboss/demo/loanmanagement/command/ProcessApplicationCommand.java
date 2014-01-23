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
package org.jboss.demo.loanmanagement.command;

import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Application;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Command that processes the loan application.
 */
public final class ProcessApplicationCommand extends AsyncTask<Application, Void, Application> {

    private final Context context;
    private ProgressDialog dialog = null;
    private Exception error = null;

    /**
     * @param commandContext the app context (cannot be <code>null</code>)
     */
    public ProcessApplicationCommand( final Context commandContext ) {
        this.context = commandContext;
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @SuppressWarnings( "javadoc" )
    @Override
    protected Application doInBackground( final Application... loanApplications ) {
        try {
            // TODO make call to process application
            Thread.sleep(2000);
            return loanApplications[0];
        } catch (final InterruptedException ignore) {
            // user canceled
        } catch (final Exception e) {
            this.error = e;
            Log.e(ProcessApplicationCommand.class.getSimpleName(),
                  this.context.getString(R.string.err_process_application_command), this.error);
        }

        return null;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute( final Application application ) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        if (this.error == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setIcon(R.drawable.ic_home)
                   .setMessage(this.context.getString(R.string.application_processed_successfully))
                   .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.ok, null).show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(R.string.err_dialog_title)
                   .setIcon(R.drawable.ic_home)
                   .setMessage(this.context.getString(R.string.err_process_application_command,
                                                      this.error.getLocalizedMessage()))
                   .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.ok, null).show();
        }
    }

    /**
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
        this.dialog =
                        Util.createProgressDialog(this.context,
                                                  this.context.getString(R.string.process_application_dialog_title),
                                                  this.context.getString(R.string.process_application_dialog_msg));
        this.dialog.show();
    }

}
