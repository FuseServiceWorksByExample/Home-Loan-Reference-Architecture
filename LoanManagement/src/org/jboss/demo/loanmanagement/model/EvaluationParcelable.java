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
import android.text.TextUtils;

/**
 * A representation of an {@link Evaluation} that can be passed into an intent extra.
 */
public final class EvaluationParcelable implements Parcelable {

    /**
     * Used to un-marshal or de-serialize an evaluation from a parcel.
     */
    public static final Creator<EvaluationParcelable> CREATOR = new Creator<EvaluationParcelable>() {

        /**
         * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
         */
        @Override
        public EvaluationParcelable createFromParcel( final Parcel in ) {
            return new EvaluationParcelable(in);
        }

        /**
         * @see android.os.Parcelable.Creator#newArray(int)
         */
        @Override
        public EvaluationParcelable[] newArray( final int size ) {
            return new EvaluationParcelable[size];
        }

    };

    /**
     * An intent key whose value is a list of available application evaluations.
     */
    public static final String EVALUATIONS = "evaluations"; //$NON-NLS-1$

    private final Evaluation evaluation;

    /**
     * @param evaluationModel the evaluation model (cannot be <code>null</code>)
     */
    public EvaluationParcelable( final Evaluation evaluationModel ) {
        this.evaluation = evaluationModel;
    }

    EvaluationParcelable( final Parcel in ) {
        final int ssn = in.readInt();
        final String name = in.readString();
        final int creditScore = in.readInt();
        final long creationDate = in.readLong();

        this.evaluation = new Evaluation(ssn, name, creditScore, creationDate);

        final double rate = in.readDouble();
        this.evaluation.setRate(rate);

        final double insuranceCost = in.readDouble();
        this.evaluation.setInsuranceCost(insuranceCost);

        final String explanation = in.readString();
        this.evaluation.setExplanation(explanation);

        final boolean approved = (in.readByte() != 0);
        this.evaluation.setApproved(approved);
    }

    /**
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /**
     * @return the evaluation model object (never <code>null</code>)
     */
    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    /**
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel( final Parcel dest,
                               final int flags ) {
        dest.writeInt(this.evaluation.getSsn());

        String name = this.evaluation.getApplicant();

        if (TextUtils.isEmpty(name)) {
            name = Util.EMPTY_STRING;
        }

        dest.writeString(name);

        dest.writeInt(this.evaluation.getCreditScore());
        dest.writeLong(this.evaluation.getCreationDate());
        dest.writeDouble(this.evaluation.getRate());
        dest.writeDouble(this.evaluation.getInsuranceCost());
        dest.writeByte((byte)(this.evaluation.isApproved() ? 1 : 0));

        String explanation = this.evaluation.getExplanation();

        if (TextUtils.isEmpty(explanation)) {
            explanation = Util.EMPTY_STRING;
        }

        dest.writeString(explanation);
    }

}
