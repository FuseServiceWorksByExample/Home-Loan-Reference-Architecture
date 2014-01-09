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
package org.jboss.demo.loanmanagement.widget;

import java.util.Comparator;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.Util.Prefs;
import org.jboss.demo.loanmanagement.model.Evaluation;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The available application evaluations screen list adapter.
 */
public final class EvaluationsAdapter extends ArrayAdapter<Evaluation> {

    private static final String DEFAULT_SORTER_ID = Prefs.SORT_BY_SSN;

    private Comparator<Evaluation> sorter;

    /**
     * @param context the evaluations screen (cannot be <code>null</code>)
     * @param appEvaluations the available evaluations (cannot be <code>null</code>)
     */
    public EvaluationsAdapter( final Context context,
                               final Evaluation[] appEvaluations ) {
        super(context, R.layout.evaluations_screen, appEvaluations);

        // setup sorter
        final String sortBy = Util.getPreferences(context).getString(Util.Prefs.EVALUATION_SORTER, DEFAULT_SORTER_ID);

        if (Prefs.SORT_BY_NAME.equals(sortBy)) {
            this.sorter = Evaluation.NAME_SORTER;
        } else {
            this.sorter = Evaluation.SSN_SORTER;
        }
    }

    /**
     * @param index the index of the evaluation being requested
     * @return the evaluation
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Evaluation getEvaluation( final int index ) {
        return getItem(index);
    }

    /**
     * @return the sorter identifier
     * @see Prefs#SORT_BY_NAME
     * @see Prefs#SORT_BY_SSN
     */
    public String getSorterId() {
        if (this.sorter == Evaluation.NAME_SORTER) {
            return Prefs.SORT_BY_NAME;
        }

        return Prefs.SORT_BY_SSN;
    }

    /**
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView( final int position,
                         final View view,
                         final ViewGroup parent ) {
        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = view;
        ItemHolder holder;

        if (null == view) {
            itemView = inflater.inflate(R.layout.evaluation_item, parent, false);
            holder = new ItemHolder();
            holder.nameView = (TextView)itemView.findViewById(R.id.app_eval_name);
            holder.ssnView = (TextView)itemView.findViewById(R.id.app_eval_ssn);
            itemView.setTag(holder);
        } else {
            holder = (ItemHolder)view.getTag();
        }

        final Evaluation evaluation = getItem(position);
        holder.nameView.setText(evaluation.getApplicant());

        { // ssn
            final int ssn = evaluation.getSsn();

            if (Evaluation.SSN_NOT_SET != ssn) {
                holder.ssnView.setText(Util.formatSsnWithMask(ssn));
            }
        }

        return itemView;
    }

    /**
     * @param newEvaluations the available application evaluations (can be <code>null</code>)
     */
    public void setEvaluations( final Evaluation[] newEvaluations ) {
        clear();
        addAll(newEvaluations);
        sort(this.sorter);
    }

    /**
     * @param sorterId the identifier of the new sorter (can be <code>null</code> if default sorter should be used)
     * @see Evaluation#NAME_SORTER
     * @see Evaluation#SSN_SORTER
     */
    public void setSorter( final String sorterId ) {
        String newId = sorterId;
        Comparator<Evaluation> evaluationSorter = null;

        if (TextUtils.isEmpty(newId)) {
            newId = DEFAULT_SORTER_ID;
        }

        if (Prefs.SORT_BY_NAME.equals(newId) && (this.sorter != Evaluation.NAME_SORTER)) {
            evaluationSorter = Evaluation.NAME_SORTER;
        } else if (Prefs.SORT_BY_SSN.equals(newId) && (this.sorter != Evaluation.SSN_SORTER)) {
            evaluationSorter = Evaluation.SSN_SORTER;
        }

        if (evaluationSorter != null) {
            this.sorter = evaluationSorter;
            sort(this.sorter);

            // save new preference value
            Util.getPreferenceEditor(getContext()).putString(Prefs.EVALUATION_SORTER, newId).apply();
        }
    }

    class ItemHolder {

        TextView nameView;
        TextView ssnView;

    }

}
