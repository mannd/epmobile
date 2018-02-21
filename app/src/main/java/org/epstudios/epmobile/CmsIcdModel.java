package org.epstudios.epmobile;

import static org.epstudios.epmobile.CmsIcdModel.Details.NONE;

/**
 * Copyright (C) 2018 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2/19/18.
 * <p>
 * This file is part of epmobile.
 * <p>
 * epmobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epmobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epmobile.  If not, see <http://www.gnu.org/licenses/>.
 */

public class CmsIcdModel {
    // Indications
    private Boolean susVT;
    private Boolean cardiacArrest;
    private Boolean priorMI;
    private Boolean icm;
    private Boolean nicm;
    private Boolean highRiskCondition;
    private Boolean icdAtEri;
    private Boolean transplantList;
    // EF and NYHA class
    private EF ef;
    private Nyha nyha;
    // Exclusions and waiting periods
    private Boolean cabgWithin3Months;
    private Boolean miWithin40Days;
    private Boolean candidateForRevasc;
    private Boolean cardiogenicShock;
    private Boolean nonCardiacDisease;
    private Boolean brainDamage;
    private Boolean uncontrolledSvt;

    public CmsIcdModel(Boolean susVT, Boolean cardiacArrest, Boolean priorMI,
                       Boolean icm, Boolean nicm, Boolean highRiskCondition,
                       Boolean icdAtEri, Boolean transplantList, EF ef, Nyha nyha,
                       Boolean cabgWithin3Months, Boolean miWithin40Days,
                       Boolean candidateForRevasc, Boolean cardiogenicShock,
                       Boolean nonCardiacDisease, Boolean brainDamage, Boolean uncontrolledSvt) {
        this.susVT = susVT;
        this.cardiacArrest = cardiacArrest;
        this.priorMI = priorMI;
        this.icm = icm;
        this.nicm = nicm;
        this.highRiskCondition = highRiskCondition;
        this.icdAtEri = icdAtEri;
        this.transplantList = transplantList;
        this.ef = ef;
        this.nyha = nyha;
        this.cabgWithin3Months = cabgWithin3Months;
        this.miWithin40Days = miWithin40Days;
        this.candidateForRevasc = candidateForRevasc;
        this.cardiogenicShock = cardiogenicShock;
        this.nonCardiacDisease = nonCardiacDisease;
        this.brainDamage = brainDamage;
        this.uncontrolledSvt = uncontrolledSvt;
    }
    // a "default" constructor for testing
    public CmsIcdModel() {
        this.susVT = false;
        this.cardiacArrest = false;
        this.priorMI = false;
        this.icm = false;
        this.nicm = false;
        this.highRiskCondition = false;
        this.icdAtEri = false;
        this.transplantList = false;
        this.ef = EF.NA;
        this.nyha = Nyha.NA;
        this.cabgWithin3Months = false;
        this.miWithin40Days = false;
        this.candidateForRevasc = false;
        this.cardiogenicShock = false;
        this.nonCardiacDisease = false;
        this.brainDamage = false;
        this.uncontrolledSvt = false;

    }

    // These setters make testing easier
    public void setSusVT(Boolean susVT) {
        this.susVT = susVT;
    }

    public void setCardiacArrest(Boolean cardiacArrest) {
        this.cardiacArrest = cardiacArrest;
    }

    public void setPriorMI(Boolean priorMI) {
        this.priorMI = priorMI;
    }

    public void setIcm(Boolean icm) {
        this.icm = icm;
    }

    public void setNicm(Boolean nicm) {
        this.nicm = nicm;
    }

    public void setHighRiskCondition(Boolean highRiskCondition) {
        this.highRiskCondition = highRiskCondition;
    }

    public void setIcdAtEri(Boolean icdAtEri) {
        this.icdAtEri = icdAtEri;
    }

    public void setTransplantList(Boolean transplantList) {
        this.transplantList = transplantList;
    }

    public void setEf(EF ef) {
        this.ef = ef;
    }

    public void setNyha(Nyha nyha) {
        this.nyha = nyha;
    }

    public void setCabgWithin3Months(Boolean cabgWithin3Months) {
        this.cabgWithin3Months = cabgWithin3Months;
    }

    public void setMiWithin40Days(Boolean miWithin40Days) {
        this.miWithin40Days = miWithin40Days;
    }

    public void setCandidateForRevasc(Boolean candidateForRevasc) {
        this.candidateForRevasc = candidateForRevasc;
    }

    public void setCardiogenicShock(Boolean cardiogenicShock) {
        this.cardiogenicShock = cardiogenicShock;
    }

    public void setNonCardiacDisease(Boolean nonCardiacDisease) {
        this.nonCardiacDisease = nonCardiacDisease;
    }

    public void setBrainDamage(Boolean brainDamage) {
        this.brainDamage = brainDamage;
    }

    public void setUncontrolledSvt(Boolean uncontrolledSvt) {
        this.uncontrolledSvt = uncontrolledSvt;
    }

    public Result getResult() {
        Result result = new Result();
        // Note that result fields are filled only when they differ
        // from the default fields.
        if (absoluteExclusion()) {
            result.approval = Approval.NOT_APPROVED;
            result.details = Details.ABSOLUTE_EXCLUSION;
        }
        else if (noIndications()) {
            result.approval = Approval.NOT_APPROVED;
            result.details = Details.NO_INDICATIONS;
        }
        else if (noEF()) {
            result.approval = Approval.NOT_APPROVED;
            result.details = Details.NO_EF;
        }
        else if (secondaryPrevention()) {
            result.details = Details.NONE;
            result.approval = Approval.APPROVED;
            result.indication = Indication.SECONDARY;
        }
        else if (highRiskCondition) {
            result.details = Details.NONE;
            result.approval = Approval.APPROVED;
            result.indication = Indication.PRIMARY;
            result.needsSdmEncounter = true;
        }
        else if (icdAtEri) {
            if (waitingPeriod()) {
                result.details = Details.WAITING_PERIOD_EXCEPTION;
            }
            result.approval = Approval.APPROVED;
            result.indication = Indication.OTHER;
        }
        else if (primaryPrevention() && noNyha()) {
            result.approval = Approval.APPROVAL_UNCLEAR;
            result.indication = Indication.PRIMARY;
            result.details = Details.NO_NYHA;
        }
        else if (madit2Indication() || scdHeftIndication()) {
            result.indication = Indication.PRIMARY;
            result.details = getExclusionDetails();
            result.needsSdmEncounter = !temporaryExclusion();
            result.approval = temporaryExclusion() ? Approval.NOT_APPROVED : Approval.APPROVED;
        }
        else if (forgotIcm()) {
            result.approval = Approval.NOT_APPROVED;
            result.indication = Indication.PRIMARY;
            result.details = Details.FORGOT_ICM;
        }
        else if (forgotMI()) {
            result.approval = Approval.NOT_APPROVED;
            result.indication = Indication.PRIMARY;
            result.details = Details.FORGOT_MI;
        }
        else if (transplantList) {
            result.approval = Approval.APPROVAL_UNCLEAR;
            result.indication = Indication.OTHER;
            result.details = Details.BRIDGE_TO_TRANSPLANT;
        }
        else {
            result.indication = Indication.PRIMARY;
            result.approval = Approval.NOT_APPROVED;
        }
        return result;
    }

    private Boolean madit2Indication() {
        return ef == EF.LESS_THAN_30 && priorMI && nyha != Nyha.IV;
    }

    private Boolean scdHeftIndication() {
        return (ef == EF.LESS_THAN_30 || ef == EF.FROM_30_TO_35)
                && (icm || nicm)
                && (nyha == Nyha.II || nyha == Nyha.III);
    }

    private Boolean forgotIcm() {
        return ef == EF.FROM_30_TO_35
                && !(icm || nicm)
                && priorMI
                && (nyha == Nyha.II || nyha == Nyha.III);
    }

    private Boolean forgotMI() {
        return icm && !priorMI && nyha == Nyha.I && ef == EF.LESS_THAN_30;
    }

    private Details getExclusionDetails() {
        if (candidateForRevasc) {
            return Details.CANDIDATE_FOR_REVASC;
        }
        else if (waitingPeriod()) {
            return Details.NEEDS_WAITING_PERIOD;
        }
        else {
            return Details.NONE;
        }
    }

    private Boolean absoluteExclusion() {
        return cardiogenicShock || brainDamage
                || nonCardiacDisease || uncontrolledSvt;
    }

    private Boolean temporaryExclusion() {
        return waitingPeriod() || candidateForRevasc;
    }

    private Boolean waitingPeriod() {
        return miWithin40Days || cabgWithin3Months;
    }

    private Boolean noIndications() {
        return !(secondaryPrevention() || primaryPrevention() || otherIndication());
    }

    private  Boolean secondaryPrevention() {
        return cardiacArrest || susVT;
    }

    private Boolean primaryPrevention() {
        return priorMI || icm || nicm || highRiskCondition;
    }

    private Boolean otherIndication() {
        return icdAtEri || transplantList;
    }

    private Boolean noEF() {
        return ef == EF.NA;
    }

    private Boolean noNyha() {
        return nyha == Nyha.NA;
    }

    public enum EF {
        MORE_THAN_35,
        FROM_30_TO_35,
        LESS_THAN_30,
        NA
    }

    public enum Nyha {
        I,
        II,
        III,
        IV,
        NA
    }

    // Result enums
    public enum Indication {
        PRIMARY,
        SECONDARY,
        OTHER,
        NONE
    }

    public enum Approval {
        APPROVED,
        NOT_APPROVED,
        APPROVAL_UNCLEAR,
        NA
    }

    public enum Details {
        ABSOLUTE_EXCLUSION,
        NO_INDICATIONS,
        NO_EF,
        NO_NYHA,
        WAITING_PERIOD_EXCEPTION,
        NEEDS_WAITING_PERIOD,
        CANDIDATE_FOR_REVASC,
        FORGOT_ICM,
        FORGOT_MI,
        BRIDGE_TO_TRANSPLANT,
        NONE
    }

    public class Result {
        public Indication indication = Indication.NONE;
        public Approval approval = Approval.NA;
        public Details details = NONE;
        public Boolean needsSdmEncounter = false;
    }
}
