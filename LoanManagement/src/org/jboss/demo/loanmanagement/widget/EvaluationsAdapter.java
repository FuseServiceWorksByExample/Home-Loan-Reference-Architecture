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

import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Evaluation;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * The available applicaiton evaluations screen list adapter.
 */
public final class EvaluationsAdapter extends ArrayAdapter<Evaluation> {

    private boolean sortBySsn = true;

    /**
     * @param screen the evaluations screen (cannot be <code>null</code>)
     * @param items the available evaluations (cannot be <code>null</code>)
     */
    public EvaluationsAdapter( final Context screen,
                               final Evaluation[] items ) {
        super(screen, R.layout.evaluations_screen, items);
        sort(Evaluation.SSN_SORTER);
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
            itemView = inflater.inflate(R.layout.evaluations_list_row, parent, false);
            holder = new ItemHolder();
            holder.firstView = (TextView)itemView.findViewById(R.id.text1);
            holder.secondView = (TextView)itemView.findViewById(R.id.text2);
            itemView.setTag(holder);
        } else {
            holder = (ItemHolder)view.getTag();
        }

        final Evaluation evaluation = getItem(position);
        final String ssn = Util.formatSsn(evaluation.getSsn());

        holder.firstView.setText(this.sortBySsn ? ssn : evaluation.getApplicant());
        holder.secondView.setText(this.sortBySsn ? evaluation.getApplicant() : ssn);

        return itemView;
    }

    /**
     * @return <code>true</code> if being sorted by SSN
     */
    public boolean isSortBySsn() {
        return this.sortBySsn;
    }

    /**
     * @param newSortBySsn <code>true</code> if sorting by SSN is desired
     */
    public void setSortBySsn( final boolean newSortBySsn ) {
        if (this.sortBySsn != newSortBySsn) {
            this.sortBySsn = newSortBySsn;
            sort(this.sortBySsn ? Evaluation.SSN_SORTER : Evaluation.NAME_SORTER);
        }
    }

    class ItemHolder {

        TextView firstView;
        TextView secondView;

    }

}
