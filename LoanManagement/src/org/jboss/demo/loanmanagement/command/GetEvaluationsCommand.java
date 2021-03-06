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
import java.util.Calendar;
import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Evaluation;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Command that obtains the available evaluations.
 */
public abstract class GetEvaluationsCommand extends AsyncTask<Void, Void, List<Evaluation>> {

    private final Context context;
    private ProgressDialog dialog = null;
    private Exception error = null;

    /**
     * @param commandContext the app context (cannot be <code>null</code>)
     */
    public GetEvaluationsCommand( final Context commandContext ) {
        this.context = commandContext;
    }

    /**
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @SuppressWarnings( "javadoc" )
    @Override
    protected List<Evaluation> doInBackground( final Void... newParams ) {
        try {
            // TODO make call to get evaluations
            Thread.sleep(2000);

            final List<Evaluation> evaluations = new ArrayList<Evaluation>(3);
            evaluations.add(new Evaluation(111111111, "Elvis Costello", 560,
                                           Calendar.getInstance().getTimeInMillis() - 50000));
            evaluations.add(new Evaluation(333333333, "Billy Bragg", 410, Calendar.getInstance().getTimeInMillis()));
            evaluations.add(new Evaluation(222222222, "Nick Lowe", 890,
                                           Calendar.getInstance().getTimeInMillis() - 600000));

            return evaluations;
        } catch (final InterruptedException ignore) {
            // user canceled
        } catch (final Exception e) {
            this.error = e;
            Log.e(GetEvaluationsCommand.class.getSimpleName(),
                  this.context.getString(R.string.err_get_evaluations_command), this.error);
        }

        return Evaluation.NO_EVALUATIONS;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute( final List<Evaluation> evaluations ) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        if (this.error == null) {
            process(evaluations);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(R.string.err_dialog_title)
                   .setIcon(R.drawable.ic_home)
                   .setMessage(this.context.getString(R.string.err_get_evaluations_command,
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
                                                  this.context.getString(R.string.evaluations_request_dialog_title),
                                                  this.context.getString(R.string.evaluations_request_dialog_msg));
        this.dialog.show();
    }

    protected abstract void process( List<Evaluation> evaluations );

}
