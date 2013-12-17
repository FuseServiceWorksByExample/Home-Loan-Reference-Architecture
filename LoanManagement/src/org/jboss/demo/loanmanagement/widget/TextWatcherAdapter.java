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

import android.text.Editable;
import android.text.TextWatcher;

/**
 * A <code>TextWatcher</code> implementation that does nothing. Subclasses need only implement the methods they choose.
 */
public class TextWatcherAdapter implements TextWatcher {

    /**
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged( final Editable string ) {
        // nothing to do
    }

    /**
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void beforeTextChanged( final CharSequence string,
                                   final int start,
                                   final int count,
                                   final int after ) {
        // nothing to do
    }

    /**
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void onTextChanged( final CharSequence string,
                               final int start,
                               final int before,
                               final int count ) {
        // nothing to do
    }

}
