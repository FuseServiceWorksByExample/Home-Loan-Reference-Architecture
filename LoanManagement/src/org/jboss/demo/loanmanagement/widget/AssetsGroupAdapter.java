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
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

final class AssetsGroupAdapter implements GroupAdapter {

    static final int DESCRIPTION = R.string.item_assets_and_liabilities_description;
    static final int TITLE = R.string.AssetsAndLiabilities;

    private AssetsAndLiabilitiesAdapter accountsAdapter;
    private Application application;
    private AssetsAndLiabilitiesAdapter autosAdapter;
    private Context context;
    private AssetsAndLiabilitiesAdapter depositsAdapter;
    private LayoutInflater inflater;

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#createView(android.view.ViewGroup,
     *      android.view.LayoutInflater)
     */
    @Override
    public View createView( final ViewGroup viewGroup,
                            final LayoutInflater layoutInflater ) {
        this.inflater = layoutInflater;
        final View view = this.inflater.inflate(R.layout.assets_liabilities, viewGroup, false);

        final TabHost tabHost = (TabHost)view.findViewById(R.id.assets_tab_host);
        tabHost.setup();

        { // accounts tab
            this.accountsAdapter = new AccountsAdapter();
            this.accountsAdapter.setContext(this.context);
            this.accountsAdapter.setInflater(this.inflater);
            this.accountsAdapter.setModel(this.application.getAssetsAndLiabilities());
            tabHost.addTab(this.accountsAdapter.createTab(tabHost));
        }

        { // autos tab
            this.autosAdapter = new AutosAdapter();
            this.autosAdapter.setContext(this.context);
            this.autosAdapter.setInflater(this.inflater);
            this.autosAdapter.setModel(this.application.getAssetsAndLiabilities());
            tabHost.addTab(this.autosAdapter.createTab(tabHost));
        }

        { // cash deposits tab
            this.depositsAdapter = new DepositsAdapter();
            this.depositsAdapter.setContext(this.context);
            this.depositsAdapter.setInflater(this.inflater);
            this.depositsAdapter.setModel(this.application.getAssetsAndLiabilities());
            tabHost.addTab(this.depositsAdapter.createTab(tabHost));
        }

        { // completed by
            final RadioGroup group = (RadioGroup)view.findViewById(R.id.grp_completed_by);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                /**
                 * @see android.widget.RadioGroup.OnCheckedChangeListener#onCheckedChanged(android.widget.RadioGroup,
                 *      int)
                 */
                @Override
                public void onCheckedChanged( final RadioGroup radioGroup,
                                              final int btnId ) {
                    String completedBy = null;

                    if (btnId == R.id.btn_jointly) {
                        completedBy = AssetsAndLiabilities.COMPLETED_BY[AssetsAndLiabilities.JOINTLY_INDEX];
                    } else if (btnId == R.id.btn_jointly) {
                        completedBy = AssetsAndLiabilities.COMPLETED_BY[AssetsAndLiabilities.NOT_JOINTLY_INDEX];
                    }

                    assert !Util.isBlank(completedBy);
                    getAssetsAndLiabilities().setCompletedType(completedBy);
                }
            });
        }

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            /**
             * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
             */
            @Override
            public void onTabChanged( final String tabId ) {
                handleTabChanged(tabId, tabHost);
            }
        });

        // call this manually since no event is generated when tabs first displayed
        this.accountsAdapter.handleTabSelected(tabHost);

        return view;
    }

    AssetsAndLiabilities getAssetsAndLiabilities() {
        return this.application.getAssetsAndLiabilities();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#getDescription()
     */
    @Override
    public int getDescription() {
        return DESCRIPTION;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#getTitle()
     */
    @Override
    public int getTitle() {
        return TITLE;
    }

    void handleTabChanged( final String tabId,
                           final ViewGroup tabHost ) {
        if (this.accountsAdapter.getTabId().equals(tabId)) {
            this.accountsAdapter.handleTabSelected(tabHost);
        } else if (this.autosAdapter.getTabId().equals(tabId)) {
            this.autosAdapter.handleTabSelected(tabHost);
        } else if (this.depositsAdapter.getTabId().equals(tabId)) {
            this.depositsAdapter.handleTabSelected(tabHost);
        } else {
            assert false;
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#refresh()
     */
    @Override
    public void refresh() {
        this.accountsAdapter.refresh();
        this.autosAdapter.refresh();
        this.depositsAdapter.refresh();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#setContext(android.content.Context)
     */
    @Override
    public void setContext( final Context appContext ) {
        this.context = appContext;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#setModel(org.jboss.demo.loanmanagement.model.Application)
     */
    @Override
    public void setModel( final Application model ) {
        this.application = model;
    }

}
