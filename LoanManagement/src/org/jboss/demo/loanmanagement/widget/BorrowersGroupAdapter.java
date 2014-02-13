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

import java.util.List;
import org.jboss.demo.loanmanagement.R;
import org.jboss.demo.loanmanagement.model.Application;
import org.jboss.demo.loanmanagement.model.Borrower;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

final class BorrowersGroupAdapter implements GroupAdapter {

    static final int DESCRIPTION = R.string.item_borrowers_description;
    static final int TITLE = R.string.Borrowers;

    private Application application;
    private ViewGroup borrowersContainer;
    private Context context;
    private LayoutInflater inflater;

    Application accessApplication() {
        return this.application;
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#createView(android.view.ViewGroup,
     *      android.view.LayoutInflater)
     */
    @Override
    public View createView( final ViewGroup parent,
                            final LayoutInflater layoutInflater ) {
        this.inflater = layoutInflater;

        final View view = this.inflater.inflate(R.layout.borrowers, parent, false);
        this.borrowersContainer = (ViewGroup)view.findViewById(R.id.borrowers_container);

        { // add borrower
            final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_add_borrower);
            btn.setOnClickListener(new OnClickListener() {

                /**
                 * @see android.view.View.OnClickListener#onClick(android.view.View)
                 */
                @Override
                public void onClick( final View imageView ) {
                    handleAddBorrower();
                }
            });
        }

        refresh();
        return view;
    }

    private List<Borrower> getBorrowers() {
        return this.application.getBorrowers();
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

    void handleAddBorrower() {
        final BorrowerEditor editor = new BorrowerEditor(this.context, R.string.add_borrower_dialog_title, null);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                final Borrower newBorrower = editor.getBorrower();
                accessApplication().addBorrower(newBorrower);
            }
        });
        editor.show();
    }

    void handleDeleteBorrower( final Borrower deleteBorrower ) {
        final String msg =
                        this.context.getString(R.string.delete_borrower_msg,
                                               Borrower.constructBorrowerName(deleteBorrower));
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
                       accessApplication().removeBorrower(deleteBorrower);
                   }
               }).show();
    }

    void handleEditBorrower( final Borrower editBorrower ) {
        final BorrowerEditor editor =
                        new BorrowerEditor(this.context, R.string.edit_borrower_dialog_title, editBorrower);
        editor.setListener(new DialogInterface.OnClickListener() {

            /**
             * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
             */
            @Override
            public void onClick( final DialogInterface dialog,
                                 final int which ) {
                editBorrower.update(editor.getBorrower());
            }
        });
        editor.show();
    }

    /**
     * @see org.jboss.demo.loanmanagement.widget.GroupAdapter#refresh()
     */
    @Override
    public void refresh() {
        if (this.borrowersContainer == null) {
            return;
        }

        this.borrowersContainer.removeAllViews();

        for (final Borrower borrower : getBorrowers()) {
            final View view = this.inflater.inflate(R.layout.borrower_item, null);
            this.borrowersContainer.addView(view);

            { // name
                final TextView txt = (TextView)view.findViewById(R.id.txt_borrower_name);
                txt.setText(Borrower.constructBorrowerName(borrower));
            }

            { // type
                final TextView txt = (TextView)view.findViewById(R.id.txt_borrower_type);
                final String type = borrower.getType();
                String label = null;

                if (Borrower.BORROWER_TYPE[Borrower.BORROWER_INDEX].equals(type)) {
                    label = this.context.getString(R.string.Borrower);
                } else {
                    label = this.context.getString(R.string.CoBorrower);
                }

                txt.setText(label);
            }

            { // edit borrower
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_edit);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View editButton ) {
                        handleEditBorrower(borrower);
                    }
                });
            }

            { // delete borrower
                final ImageButton btn = (ImageButton)view.findViewById(R.id.btn_delete);
                btn.setOnClickListener(new OnClickListener() {

                    /**
                     * @see android.view.View.OnClickListener#onClick(android.view.View)
                     */
                    @Override
                    public void onClick( final View deleteButton ) {
                        handleDeleteBorrower(borrower);
                    }
                });
            }
        }
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
