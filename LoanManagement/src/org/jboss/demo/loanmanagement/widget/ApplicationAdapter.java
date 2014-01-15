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

import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.model.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * The loan application list adapter.
 */
public final class ApplicationAdapter extends BaseExpandableListAdapter {

    private static final int ADDRESS_INDEX = 0;
    private static final String[] GROUPS = new String[] {"Address", "Housing Expense"};
    private static final int HOUSING_EXPENSE_INDEX = 1;

    private final Application application;
    private final ExpandableListView expandableListView;
    private final LayoutInflater inflater;
    private int lastExpandedGroupIndex;

    /**
     * @param context the home loan main screen (cannot be <code>null</code>)
     * @param view the expandable list view (cannot be <code>null</code>)
     */
    public ApplicationAdapter( final Context context,
                               final ExpandableListView view ) {
        this.inflater = LayoutInflater.from(context);
        this.expandableListView = view;
        this.application = new Application();
    }

    /**
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild( final int groupIndex,
                            final int childIndex ) {
        return this.application;
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
    public int getChildrenCount( final int newGroupPosition ) {
        // TODO Auto-generated method stub
        return 0;
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
        View result = view;

        if (view == null) {
            if (ADDRESS_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.address, viewGroup, false);
            } else if (HOUSING_EXPENSE_INDEX == groupIndex) {
                result = this.inflater.inflate(R.layout.housing_expense, viewGroup, false);
            } else {
                result = this.inflater.inflate(R.layout.declarations, viewGroup, false);
            }
        }

        return result;
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
            result = this.inflater.inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
        }

        // set group name as tag so view can be found view later
        result.setTag(GROUPS[groupIndex]);

        final TextView textView = (TextView)result.findViewById(android.R.id.text1);
        textView.setText(GROUPS[groupIndex]);

        return result;
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

}
