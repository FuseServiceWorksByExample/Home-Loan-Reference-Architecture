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
package org.jboss.demo.loanmanagement.widget;

import java.beans.PropertyChangeEvent;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.model.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * The loan application list adapter.
 */
public final class ApplicationAdapter extends BaseExpandableListAdapter {

    /**
     * The index of the assets view in the expandable list.
     */
    public static final int ASSETS_INDEX = 3;

    /**
     * The index of the borrowers view in the expandable list.
     */
    public static final int BORROWERS_INDEX = 1;

    private static final int[] GROUP_DESCRIPTIONS = new int[] {LoanDetailsGroupAdapter.DESCRIPTION,
                                                               BorrowersGroupAdapter.DESCRIPTION,
                                                               HousingExpenseGroupAdapter.DESCRIPTION,
                                                               AssetsGroupAdapter.DESCRIPTION};

    private static final int[] GROUPS = new int[] {LoanDetailsGroupAdapter.TITLE, BorrowersGroupAdapter.TITLE,
                                                   HousingExpenseGroupAdapter.TITLE, AssetsGroupAdapter.TITLE};
    /**
     * The index of the housing expense view in the expandable list.
     */
    public static final int HOUSING_EXPENSE_INDEX = 2;

    /**
     * The index of the loan view in the expandable list.
     */
    public static final int LOAN_INDEX = 0;

    private final Application application;
    private final GroupAdapter assetsAdapter;
    private final GroupAdapter borrowersAdapter;
    private final Context context;
    private final ExpandableListView expandableListView;
    private final GroupAdapter housingExpenseAdapter;
    private final LayoutInflater inflater;
    private int lastExpandedGroupIndex = AdapterView.INVALID_POSITION;
    private final GroupAdapter loanDetailsAdapter;

    /**
     * @param screen the home loan main screen (cannot be <code>null</code>)
     * @param view the expandable list view (cannot be <code>null</code>)
     * @param loanApplication the loan application (cannot be <code>null</code>)
     */
    public ApplicationAdapter( final Context screen,
                               final ExpandableListView view,
                               final Application loanApplication ) {
        this.context = screen;
        this.inflater = LayoutInflater.from(this.context);
        this.expandableListView = view;
        this.application = loanApplication;

        this.loanDetailsAdapter = new LoanDetailsGroupAdapter();
        this.loanDetailsAdapter.setContext(this.context);
        this.loanDetailsAdapter.setModel(this.application);

        this.borrowersAdapter = new BorrowersGroupAdapter();
        this.borrowersAdapter.setContext(this.context);
        this.borrowersAdapter.setModel(this.application);

        this.housingExpenseAdapter = new HousingExpenseGroupAdapter();
        this.housingExpenseAdapter.setContext(this.context);
        this.housingExpenseAdapter.setModel(this.application);

        this.assetsAdapter = new AssetsGroupAdapter();
        this.assetsAdapter.setContext(this.context);
        this.assetsAdapter.setModel(this.application);
    }

    /**
     * Collapses the last group that was expanded.
     */
    public void collapseAllGroups() {
        if (this.lastExpandedGroupIndex != AdapterView.INVALID_POSITION) {
            this.expandableListView.collapseGroup(this.lastExpandedGroupIndex);
        }
    }

    Application getApplication() {
        return this.application;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild( final int groupIndex,
                            final int childIndex ) {
        return ((groupIndex * 10) + childIndex);
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId( final int groupIndex,
                            final int childIndex ) {
        return childIndex;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount( final int groupIndex ) {
        return 1;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#getChildType(int, int)
     */
    @Override
    public int getChildType( final int groupIndex,
                             final int childIndex ) {
        return groupIndex;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#getChildTypeCount()
     */
    @Override
    public int getChildTypeCount() {
        return GROUPS.length;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getChildView( final int groupIndex,
                              final int childIndex,
                              final boolean isLastChild,
                              final View view,
                              final ViewGroup viewGroup ) {
        final View result = view;

        if (view == null) {
            if (LOAN_INDEX == groupIndex) {
                return this.loanDetailsAdapter.createView(viewGroup, this.inflater);
            }

            if (BORROWERS_INDEX == groupIndex) {
                return this.borrowersAdapter.createView(viewGroup, this.inflater);
            }

            if (HOUSING_EXPENSE_INDEX == groupIndex) {
                return this.housingExpenseAdapter.createView(viewGroup, this.inflater);
            }

            if (ASSETS_INDEX == groupIndex) {
                return this.assetsAdapter.createView(viewGroup, this.inflater);
            }
        }

        return result;
    }

    Context getContext() {
        return this.context;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup( final int index ) {
        return GROUPS[index];
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return GROUPS.length;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId( final int index ) {
        return index;
    }

    /**
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getGroupView( final int groupIndex,
                              final boolean isExpanded,
                              final View view,
                              final ViewGroup viewGroup ) {
        View result = view;

        if (view == null) {
            result = this.inflater.inflate(android.R.layout.simple_expandable_list_item_2, viewGroup, false);
            result.setBackgroundResource(R.drawable.gradient);
        }

        // set group name as tag so view can be found view later
        result.setTag(GROUPS[groupIndex]);

        { // group title
            final TextView textView = (TextView)result.findViewById(android.R.id.text1);
            textView.setText(GROUPS[groupIndex]);
        }

        { // group description
            final TextView textView = (TextView)result.findViewById(android.R.id.text2);
            textView.setText(GROUP_DESCRIPTIONS[groupIndex]);
        }

        return result;
    }

    Application getLoanApplication() {
        return this.application;
    }

    /**
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable( final int newGroupPosition,
                                      final int newChildPosition ) {
        return false;
    }

    /**
     * @see android.widget.BaseExpandableListAdapter#onGroupExpanded(int)
     */
    @Override
    public void onGroupExpanded( final int groupIndex ) {
        if (groupIndex != this.lastExpandedGroupIndex) {
            this.expandableListView.collapseGroup(this.lastExpandedGroupIndex);
            this.lastExpandedGroupIndex = groupIndex;
        }

        super.onGroupExpanded(groupIndex);
    }

    /**
     * @param event the property change event that is being processed during the refresh (cannot be <code>null</code>)
     */
    public void refresh( final PropertyChangeEvent event ) {
        final String propName = event.getPropertyName();

        if (Application.Properties.ASSETS_LIABILITIES.equals(propName)) {
            this.assetsAdapter.refresh();
        } else if (Application.Properties.BORROWERS.equals(propName)) {
            this.borrowersAdapter.refresh();
        }
    }

}
