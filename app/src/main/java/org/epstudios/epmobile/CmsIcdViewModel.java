package org.epstudios.epmobile;

import android.app.Activity;

import java.util.HashMap;

/**
 * Copyright (C) 2018 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 2/20/18.
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

public class CmsIcdViewModel {
    private static final String CR = "\n";

    private final Activity activity;
    private final CmsIcdModel model;
    private final HashMap<CmsIcdModel.Indication, String> indications = new HashMap<>();
    private final HashMap<CmsIcdModel.Approval, String> approvals = new HashMap<>();
    private final HashMap<CmsIcdModel.Details, String> details = new HashMap<>();

    public CmsIcdViewModel(Activity activity, Boolean susVT, Boolean cardiacArrest,
                           Boolean priorMI, Boolean icm, Boolean nicm,
                           Boolean highRiskCondition, Boolean icdAtEri,
                           Boolean transplantList, CmsIcdModel.EF ef,
                           CmsIcdModel.Nyha nyha, Boolean cabgWithin3Months,
                           Boolean miWithin40Days, Boolean candidateForRevasc,
                           Boolean cardiogenicShock, Boolean nonCardiacDisease,
                           Boolean brainDamage, Boolean uncontrolledSvt) {
        this.activity = activity;
        this.model = new CmsIcdModel(susVT, cardiacArrest, priorMI,
                icm, nicm, highRiskCondition, icdAtEri, transplantList, ef,
                nyha, cabgWithin3Months, miWithin40Days, candidateForRevasc, cardiogenicShock,
                nonCardiacDisease, brainDamage, uncontrolledSvt);
        indications.put(CmsIcdModel.Indication.PRIMARY,
                activity.getString(R.string.primary_prevention_label));
        indications.put(CmsIcdModel.Indication.SECONDARY,
                activity.getString(R.string.secondary_prevention_label));
        indications.put(CmsIcdModel.Indication.OTHER,
                activity.getString(R.string.other_indication_label));
        indications.put(CmsIcdModel.Indication.NONE, null);
        approvals.put(CmsIcdModel.Approval.APPROVED,
                activity.getString(R.string.icd_approved_text));
        approvals.put(CmsIcdModel.Approval.NOT_APPROVED,
                activity.getString(R.string.icd_not_approved_text));
        approvals.put(CmsIcdModel.Approval.APPROVAL_UNCLEAR,
                activity.getString(R.string.icd_approval_unclear_message));
        approvals.put(CmsIcdModel.Approval.NA, null);
        details.put(CmsIcdModel.Details.ABSOLUTE_EXCLUSION,
                activity.getString(R.string.absolute_exclusion));
        details.put(CmsIcdModel.Details.NO_INDICATIONS,
                activity.getString(R.string.icd_no_indications_message));
        details.put(CmsIcdModel.Details.NO_EF,
                activity.getString(R.string.icd_ef_not_measured_text));
        details.put(CmsIcdModel.Details.NO_NYHA,
                activity.getString(R.string.icd_no_nyha_message));
        details.put(CmsIcdModel.Details.WAITING_PERIOD_EXCEPTION,
                activity.getString(R.string.icd_exceptions_apply));
        details.put(CmsIcdModel.Details.NEEDS_WAITING_PERIOD,
                activity.getString(R.string.icd_approved_after_waiting_period));
        details.put(CmsIcdModel.Details.CANDIDATE_FOR_REVASC,
                activity.getString(R.string.icd_revasc_candidate));
        details.put(CmsIcdModel.Details.FORGOT_ICM,
                activity.getString(R.string.icd_forgot_to_check_cm_checkbox));
        details.put(CmsIcdModel.Details.FORGOT_MI,
                activity.getString(R.string.icd_possibly_forgot_to_check_mi_checkbox));
        details.put(CmsIcdModel.Details.BRIDGE_TO_TRANSPLANT,
                activity.getString(R.string.icd_bridge_to_transplant_message));
        details.put(CmsIcdModel.Details.NONE, null);
    }

    public String getMessage() {
        CmsIcdModel.Result result = model.getResult();
        String indication = indications.get(result.indication);
        String approval = approvals.get(result.approval);
        String detail = details.get(result.details);
        String sdmEncounter = null;
        if (result.needsSdmEncounter) {
            sdmEncounter = activity.getString(R.string.cms_icd_shared_decision_message);
        }
        StringBuilder sb = new StringBuilder();
        if (indication != null) {
           sb.append(indication);
        }
        if (approval != null) {
            sb.append(CR).append(approval);
        }
        if (detail != null) {
            sb.append(CR).append(detail);
        }
        if (sdmEncounter != null) {
            sb.append(" ");
            sb.append(sdmEncounter);
        }
        return sb.toString();
    }


}
