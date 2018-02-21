package org.epstudios.epmobile;

import org.junit.Test;

import static org.junit.Assert.*;

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
public class CmsIcdResultTest {
    @Test
    public void testResult() {
        CmsIcdModel model = new CmsIcdModel();
        CmsIcdModel.Result result = model.getResult();
        assert(result.indication == CmsIcdModel.Indication.NONE);
        assert(result.approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(result.details == CmsIcdModel.Details.NO_INDICATIONS);
        assert(!result.needsSdmEncounter);
        runAbsoluteExclusionTests(model);
        // should be back to no indications
        assert(result.approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(model.getResult().details == CmsIcdModel.Details.NO_INDICATIONS);
        // now set something
        model.setCardiacArrest(true);
        // problem now is no ef set
        assert(result.approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(model.getResult().details == CmsIcdModel.Details.NO_EF);
        // Absolute exclusions still override No EF and No Indications so test it again
        runAbsoluteExclusionTests(model);
        // try setting something else with no EF
        model.setCardiacArrest(false);
        model.setIcdAtEri(true);
        assert(model.getResult().details == CmsIcdModel.Details.NO_EF);
        // reset model
        model = newModel();
        assert(model.getResult().details == CmsIcdModel.Details.NO_INDICATIONS);
        model.setSusVT(true);
        assert(model.getResult().details == CmsIcdModel.Details.NO_EF);
        model.setEf(CmsIcdModel.EF.MORE_THAN_35);
        assert(model.getResult().details == CmsIcdModel.Details.NONE);
        assert(model.getResult().indication == CmsIcdModel.Indication.SECONDARY);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVED);
        assert(!model.getResult().needsSdmEncounter);
        runAbsoluteExclusionTests(model);
        model.setSusVT(false);
        model.setHighRiskCondition(true);
        assert(model.getResult().details == CmsIcdModel.Details.NONE);
        assert(model.getResult().indication == CmsIcdModel.Indication.PRIMARY);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVED);
        assert(model.getResult().needsSdmEncounter);
        model.setHighRiskCondition(false);
        model.setIcdAtEri(true);
        // waiting period exception only occurs when there is a possible waiting period
        model.setMiWithin40Days(true);
        assert(model.getResult().details == CmsIcdModel.Details.WAITING_PERIOD_EXCEPTION);
        assert(model.getResult().indication == CmsIcdModel.Indication.OTHER);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVED);
        assert(!model.getResult().needsSdmEncounter);
        model.setMiWithin40Days(false);
        assert(model.getResult().details == CmsIcdModel.Details.NONE);
        model.setIcdAtEri(false);
        // MADIT II indication
        model.setPriorMI(true);
        assert(model.getResult().details == CmsIcdModel.Details.NO_NYHA);
        assert(model.getResult().indication == CmsIcdModel.Indication.PRIMARY);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVAL_UNCLEAR);
        assert(!model.getResult().needsSdmEncounter);
        model.setNyha(CmsIcdModel.Nyha.I);
        model.setEf(CmsIcdModel.EF.LESS_THAN_30);
        primaryPreventionApproved(model);
        // Class IV excluded form MADIT II
        model.setNyha(CmsIcdModel.Nyha.IV);
        primaryPreventionNotApproved(model);
        // ScdHeft
        model.setNyha(CmsIcdModel.Nyha.II);
        model.setPriorMI(false);
        model.setIcm(true);
        model.setEf(CmsIcdModel.EF.FROM_30_TO_35);
        primaryPreventionApproved(model);
        model.setNyha(CmsIcdModel.Nyha.III);
        primaryPreventionApproved(model);
        model.setIcm(false);
        model.setNicm(true);
        primaryPreventionApproved(model);
        // setting prior MI shouldn't make a difference
        model.setPriorMI(true);
        primaryPreventionApproved(model);
        // clear model
        model = newModel();
        // check forgot ICM in patient with prior MI
        model.setEf(CmsIcdModel.EF.FROM_30_TO_35);
        model.setPriorMI(true);
        model.setNyha(CmsIcdModel.Nyha.II);
        assert(model.getResult().approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(model.getResult().details == CmsIcdModel.Details.FORGOT_ICM);
        model.setNyha(CmsIcdModel.Nyha.III);
        assert(model.getResult().approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(model.getResult().details == CmsIcdModel.Details.FORGOT_ICM);
        model.setEf(CmsIcdModel.EF.MORE_THAN_35);
        primaryPreventionNotApproved(model);
        // check forgot MI in pt with ICM
        model = newModel();
        model.setEf(CmsIcdModel.EF.LESS_THAN_30);
        model.setIcm(true);
        model.setNyha(CmsIcdModel.Nyha.I);
        assert(model.getResult().approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(model.getResult().details == CmsIcdModel.Details.FORGOT_MI);
        // check transplant result
        model = newModel();
        model.setEf(CmsIcdModel.EF.LESS_THAN_30);
        model.setTransplantList(true);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVAL_UNCLEAR);
        assert(model.getResult().indication == CmsIcdModel.Indication.OTHER);
        assert(model.getResult().details == CmsIcdModel.Details.BRIDGE_TO_TRANSPLANT);
        runAbsoluteExclusionTests(model);
        // primary prevention overrides transplant
        model.setPriorMI(true);
        model.setNyha(CmsIcdModel.Nyha.I);
        primaryPreventionApproved(model);
        // but since primary prevention indication fails here, transplant result overrides
        model.setNyha(CmsIcdModel.Nyha.IV);
        assert(model.getResult().details == CmsIcdModel.Details.BRIDGE_TO_TRANSPLANT);
    }

    private void primaryPreventionApproved(CmsIcdModel model) {
        assert(model.getResult().details == CmsIcdModel.Details.NONE);
        assert(model.getResult().indication == CmsIcdModel.Indication.PRIMARY);
        assert(model.getResult().approval == CmsIcdModel.Approval.APPROVED);
        assert(model.getResult().needsSdmEncounter);
    }

    private void primaryPreventionNotApproved(CmsIcdModel model) {
        assert(model.getResult().details == CmsIcdModel.Details.NONE);
        assert(model.getResult().indication == CmsIcdModel.Indication.PRIMARY);
        assert(model.getResult().approval == CmsIcdModel.Approval.NOT_APPROVED);
        assert(!model.getResult().needsSdmEncounter);

    }

    private void runAbsoluteExclusionTests(CmsIcdModel model) {
        // assumes no absolute exclusions set on method entry
        model.setBrainDamage(true);
        assert(isAbsoluteExclusion(model));
        model.setBrainDamage(false);
        assert(!isAbsoluteExclusion(model));
        model.setCardiogenicShock(true);
        assert(isAbsoluteExclusion(model));
        model.setCardiogenicShock(false);
        model.setNonCardiacDisease(true);
        assert(isAbsoluteExclusion(model));
        model.setUncontrolledSvt(true);
        assert(isAbsoluteExclusion(model));
        model.setNonCardiacDisease(false);
        assert(isAbsoluteExclusion(model));
        model.setUncontrolledSvt(false);
        assert(!isAbsoluteExclusion(model));
        // no absolute exclusions set at end of this method
    }

    private Boolean isAbsoluteExclusion(CmsIcdModel model) {
        CmsIcdModel.Result result = model.getResult();
        return result.details == CmsIcdModel.Details.ABSOLUTE_EXCLUSION;
    }

    private CmsIcdModel newModel() {
        return new CmsIcdModel();
    }
}