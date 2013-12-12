/*
 * Copyright (c) 2013 Ebry Dobby, LLC. All rights reserved. http://ebrydobby.com
 */
package org.jboss.demo.loanmanagement.command;

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
public abstract class GetEvaluationsCommand extends AsyncTask<Void, Void, Evaluation[]> {

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
    @Override
    protected Evaluation[] doInBackground( final Void... newParams ) {
        try {
            // TODO make call to get evaluations
            Thread.sleep(5000);
            return new Evaluation[] {new Evaluation(111111111, "Francis Howell", 560),
                                     new Evaluation(333333333, "Alex Rodriguez", 410),
                                     new Evaluation(222222222, "Jonathon Dough", 890)};
        } catch (final InterruptedException ignore) {
            // user canceled
        } catch (final Exception e) {
            this.error = e;
            Log.e(GetEvaluationsCommand.class.getSimpleName(),
                  this.context.getString(R.string.evaluations_query_error_msg), this.error);
        }

        return Evaluation.NO_EVALUATIONS;
    }

    /**
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute( final Evaluation[] evaluations ) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        if (this.error == null) {
            process(evaluations);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setTitle(R.string.error_dialog_title)
                   .setIcon(R.drawable.ic_home)
                   .setMessage(this.context.getString(R.string.evaluations_query_error_msg,
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

    protected abstract void process( Evaluation[] evaluations );

}
