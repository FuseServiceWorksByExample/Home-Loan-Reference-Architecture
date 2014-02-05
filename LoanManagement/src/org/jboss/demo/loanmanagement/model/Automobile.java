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
package org.jboss.demo.loanmanagement.model;


/**
 * An automobile model object.
 */
public final class Automobile extends Asset<Automobile> {

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#copy()
     */
    @Override
    public Automobile copy() {
        final Automobile copy = new Automobile();
        copy.setAmount(getAmount());
        copy.setDescription(getDescription());

        return copy;
    }

    /**
     * @see org.jboss.demo.loanmanagement.model.ModelObject#update(java.lang.Object)
     */
    @Override
    public void update( final Automobile from ) {
        setAmount(from.getAmount());
        setDescription(from.getDescription());
    }

}
