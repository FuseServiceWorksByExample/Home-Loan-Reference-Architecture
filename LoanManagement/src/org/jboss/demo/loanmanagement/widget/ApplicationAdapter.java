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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.widget.SimpleExpandableListAdapter;

/**
 * The loan application list adapter.
 */
@SuppressWarnings( "serial" )
public final class ApplicationAdapter extends SimpleExpandableListAdapter {

    private static final List<Map<String, String>> GROUPINGS;
    private static final List<List<Map<String, String>>> CHILD_DATA;
    private static final List<Map<String, String>> BORROWER_GROUPING;
    private static final List<Map<String, String>> EMPLOYMENT_GROUPING;

    static {
        GROUPINGS = new ArrayList<Map<String, String>>() {

            {
                add(new HashMap<String, String>() {

                    {
                        put("ROOT_NAME", "Borrower");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("ROOT_NAME", "Employment");
                    }
                });
            }
        };

        BORROWER_GROUPING = new ArrayList<Map<String, String>>() {

            {
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 1 in group 1");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 2 in group 1");
                    }
                });
            }
        };

        EMPLOYMENT_GROUPING = new ArrayList<Map<String, String>>() {

            {
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 1 in group 2");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 2 in group 2");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 3 in group 2");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 4 in group 2");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 5 in group 2");
                    }
                });
                add(new HashMap<String, String>() {

                    {
                        put("CHILD_NAME", "child 6 in group 2");
                    }
                });
            }
        };

        CHILD_DATA = new ArrayList<List<Map<String, String>>>();
        CHILD_DATA.add(BORROWER_GROUPING);
        CHILD_DATA.add(EMPLOYMENT_GROUPING);
    }

    /**
     * @param mainScreen the home loan main screen (cannot be <code>null</code>)
     */
    public ApplicationAdapter( final Context mainScreen ) {
        super(mainScreen, GROUPINGS, android.R.layout.simple_expandable_list_item_1, new String[] {"ROOT_NAME"},
              new int[] {android.R.id.text1}, CHILD_DATA, android.R.layout.simple_expandable_list_item_2,
              new String[] {"CHILD_NAME", "CHILD_NAME"}, new int[] {android.R.id.text1, android.R.id.text2});
    }

}
