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

import java.util.ArrayList;
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.ApplicationStatus;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Command that obtains the available statuses.
 */
public abstract class GetStatusesCommand extends AsyncTask<Void, Void, List<ApplicationStatus>> {

    private final Context context;
    private ProgressDialog dialog = null;
    private Exception error = null;

    /**
     * @param commandContext the app context (cannot be <code>null</code>)
     */
    public GetStatusesCommand( final Context commandContext ) {
        this.context = commandContext;
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @SuppressWarnings( "javadoc" )
    @Override
    protected List<ApplicationStatus> doInBackground( final Void... newParams ) {
        try {
            // TODO make call to get statuses
            Thread.sleep(2000);

            final List<ApplicationStatus> statuses = new ArrayList<ApplicationStatus>(4);
            statuses.add(new ApplicationStatus(111111111, "Johnny Marr", 5.0D, "PENDING"));
            statuses.add(new ApplicationStatus(333333333, "Evan Dando", 3.5D, "APPROVED"));
            statuses.add(new ApplicationStatus(222222222, "Joe Strummer", 2.5D, "REJECTED"));
            statuses.add(new ApplicationStatus(444444444, "Steven Morrissey", 8.0D, "PENDING"));

            return statuses;
        } catch (final InterruptedException ignore) {
            // user canceled
        } catch (final Exception e) {
            this.error = e;
            Log.e(GetStatusesCommand.class.getSimpleName(), this.context.getString(R.string.err_get_statuses_command),
                  this.error);
        }

        return ApplicationStatus.NO_STATUSES;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute( final List<ApplicationStatus> statuses ) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        if (this.error == null) {
            process(statuses);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(R.string.err_dialog_title)
                   .setIcon(R.drawable.ic_home)
                   .setMessage(this.context.getString(R.string.err_get_statuses_command,
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
                                                  this.context.getString(R.string.statuses_request_dialog_title),
                                                  this.context.getString(R.string.statuses_request_dialog_msg));
        this.dialog.show();
    }

    protected abstract void process( List<ApplicationStatus> statuses );

}
