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

import org.jboss.demo.loanmanagement.Util;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A representation of an {@link ApplicationStatus} that can be passed into an intent extra.
 */
public final class ApplicationStatusParcelable implements Parcelable {

    /**
     * Used to un-marshal or de-serialize a status from a parcel.
     */
    public static final Creator<ApplicationStatusParcelable> CREATOR = new Creator<ApplicationStatusParcelable>() {

        /**
         * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
         */
        @Override
        public ApplicationStatusParcelable createFromParcel( final Parcel in ) {
            return new ApplicationStatusParcelable(in);
        }

        /**
         * @see android.os.Parcelable.Creator#newArray(int)
         */
        @Override
        public ApplicationStatusParcelable[] newArray( final int size ) {
            return new ApplicationStatusParcelable[size];
        }

    };

    /**
     * An intent key whose value is a list of available application statuses.
     */
    public static final String STATUSES = "statuses"; //$NON-NLS-1$

    private final ApplicationStatus appStatus;

    /**
     * @param appStatusModel the application status model (cannot be <code>null</code>)
     */
    public ApplicationStatusParcelable( final ApplicationStatus appStatusModel ) {
        this.appStatus = appStatusModel;
    }

    ApplicationStatusParcelable( final Parcel in ) {
        final int ssn = in.readInt();
        final String name = in.readString();
        final double rate = in.readDouble();
        final String status = in.readString();

        this.appStatus = new ApplicationStatus(ssn, name, rate, status);
    }

    /**
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * @return the application status model object (never <code>null</code>)
     */
    public ApplicationStatus getStatus() {
        return this.appStatus;
    }

    /**
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel( final Parcel dest,
                               final int flags ) {
        dest.writeInt(this.appStatus.getSsn());

        { // applicant name
            String name = this.appStatus.getApplicant();

            if (Util.isBlank(name)) {
                name = Util.EMPTY_STRING;
            }

            dest.writeString(name);
        }

        dest.writeDouble(this.appStatus.getRate());

        { // application status
            String status = this.appStatus.getStatus();

            if (Util.isBlank(status)) {
                status = Util.EMPTY_STRING;
            }

            dest.writeString(status);
        }
    }

}
