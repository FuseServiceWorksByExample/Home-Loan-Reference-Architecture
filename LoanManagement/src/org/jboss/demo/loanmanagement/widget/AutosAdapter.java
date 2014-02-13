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

import java.text.ParseException;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.Util;
import org.jboss.demo.loanmanagement.model.AssetsAndLiabilities;
import org.jboss.demo.loanmanagement.model.Automobile;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

final class AutosAdapter implements AssetsAndLiabilitiesAdapter {

    private static final String TAB_ID = "tab_autos"; //$NON-NLS-1$

    static void doEditAuto( final AssetEditor<Automobile> editor,
                            final Automobile editAuto ) {
        try {
            editAuto.setAmount(Util.parseDouble(editor.getAmount()));
        } catch (final ParseException e) {
            Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
        }

        editAuto.setDescription(editor.getDescription());
    }

    private AssetsAndLiabilities assets;
    private ViewGroup autosContainer;
    private Context context;
    private LayoutInflater inflater;
    private boolean setup = false;

    AssetsAndLiabilities accessAssets() {
        return this.assets;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#createTab(android.widget.TabHost)
     */
    @Override
    public TabSpec createTab( final TabHost tabHost ) {
        final TabSpec tab = tabHost.newTabSpec(TAB_ID);
        tab.setContent(R.id.tab_autos);
        tab.setIndicator(this.context.getString(R.string.Automobiles));
        return tab;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#getTabId()
     */
    @Override
    public String getTabId() {
        return TAB_ID;
    }

    void handleAddAuto() {
        final AssetEditor<Automobile> editor =
                        new AssetEditor<Automobile>(this.context, R.string.add_auto_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final Automobile newAuto = new Automobile();

                try {
                    newAuto.setAmount(Util.parseDouble(editor.getAmount()));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    newAuto.setAmount(0);
                }

                newAuto.setDescription(editor.getDescription());
                accessAssets().addAutomobile(newAuto);
            }
        });
        editor.show();
    }

    void handleDeleteAuto( final Automobile deleteAuto ) {
        String msg = null;

        if (Util.isBlank(deleteAuto.getDescription())) {
            msg = this.context.getString(R.string.delete_auto_msg);
        } else {
            msg = this.context.getString(R.string.delete_auto_with_description_msg, deleteAuto.getDescription());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.delete_dialog_title).setIcon(R.drawable.ic_home).setMessage(msg)
               .setNegativeButton(android.R.string.cancel, null)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                   /**
                    * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                    */
                   @Override
                   public void onClick( final DialogInterface dialog,
                                        final int which ) {
                       accessAssets().removeAutomobile(deleteAuto);
                   }
               }).show();
    }

    void handleEditAuto( final Automobile editAuto ) {
        final AssetEditor<Automobile> editor =
                        new AssetEditor<Automobile>(this.context, R.string.edit_auto_dialog_title, editAuto);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                doEditAuto(editor, editAuto);
            }
        });
        editor.show();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#handleTabSelected(android.view.ViewGroup)
     */
    @Override
    public void handleTabSelected( final ViewGroup viewGroup ) {
        if (!this.setup) {
            final ViewGroup autosTab = (ViewGroup)viewGroup.findViewById(R.id.tab_autos);
            this.autosContainer = (ViewGroup)autosTab.findViewById(R.id.autos_container);

            { // add auto
                final ImageButton btn = (ImageButton)autosTab.findViewById(R.id.btn_add_auto);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View view ) {
                        handleAddAuto();
                    }
                });
            }

            refresh();
            this.setup = true;
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#refresh()
     */
    @Override
    public void refresh() {
        if (this.autosContainer == null) {
            return;
        }

        this.autosContainer.removeAllViews();

        for (final Automobile auto : this.assets.getAutomobiles()) {
            final View view = this.inflater.inflate(R.layout.asset_item, null);
            this.autosContainer.addView(view);

            { // description
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_description);
                txt.setText(auto.getDescription());
            }

            { // amount
                final TextView txt = (TextView)view.findViewById(R.id.txt_asset_amount);

                try {
                    txt.setText(Util.formatMoneyAmount(Double.toString(auto.getAmount())));
                } catch (final ParseException e) {
                    Log.e(ApplicationAdapter.class.getSimpleName(), e.getLocalizedMessage(), e);
                    txt.setError(this.context.getText(R.string.err_invalid_amount));
                }
            }

            { // edit auto
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditAuto(auto);
                    }
                });
            }

            { // delete auto
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteAuto(auto);
                    }
                });
            }
        }
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setContext(android.content.Context)
     */
    @Override
    public void setContext( final Context appContext ) {
        this.context = appContext;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setInflater(android.view.LayoutInflater)
     */
    @Override
    public void setInflater( final LayoutInflater layoutInflater ) {
        this.inflater = layoutInflater;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.AssetsAndLiabilitiesAdapter#setModel(org.jboss.demo.loanmanagement.model.AssetsAndLiabilities)
     */
    @Override
    public void setModel( final AssetsAndLiabilities model ) {
        this.assets = model;
    }

}
