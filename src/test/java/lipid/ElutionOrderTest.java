package lipid;

import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Description of the class.
 *
 * @author laura
 */
public class ElutionOrderTest {
    static final Logger LOG = LoggerFactory.getLogger(ElutionOrderTest.class);
    /*@Test
    public void shouldScoreBasedOnCarbonCountAndRT() {
        // Creamos algunos lípidos y anotaciones con diferentes RT y carbonos
        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3); // MZ of [M+H]+=885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", "TG", 52, 3); // MZ of [M+H]+=857.75927

        Annotation a1 = new Annotation(lipid1, 885.79056, 10E6, 10d, Set.of(new Peak(700.0, 100000)));
        Annotation a2 = new Annotation(lipid2, 857.7593, 10E7, 9d, Set.of(new Peak(710.0, 100000)));

        LipidScoreUnit unit = new LipidScoreUnit();
        unit.getAnnotations().add(a1);//PASO FACTS
        unit.getAnnotations().add(a2);

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
        try {
            instance.fire();

            // Si las reglas se aplican correctamente, ambos deberían tener score 1
            assertEquals(1, a1.getScore(),0.01);
            assertEquals(1, a2.getScore(),0.01);
        } finally {
            instance.close();
        }
    }

    @Test
    public void shouldScoreMultiplePairs() {
        Lipid l1 = new Lipid(1, "L1", "C52H104O6", "TG", 52, 3);
        Lipid l2 = new Lipid(2, "L2", "C54H108O6", "TG", 54, 3);
        Lipid l3 = new Lipid(3, "L3", "C56H112O6", "TG", 56, 3);

        Annotation a1 = new Annotation(l1, 850.0, 1e6, 5.0, Set.of(new Peak(850.0, 1e6)));
        Annotation a2 = new Annotation(l2, 870.0, 1e6, 6.0, Set.of(new Peak(870.0, 1e6)));
        Annotation a3 = new Annotation(l3, 890.0, 1e6, 7.0, Set.of(new Peak(890.0, 1e6)));

        LipidScoreUnit unit = new LipidScoreUnit();
        unit.getAnnotations().add(a1);
        unit.getAnnotations().add(a2);
        unit.getAnnotations().add(a3);

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
        try {
            instance.fire();

            assertEquals(2, a1.getScore(), 0.01);
            assertEquals(2, a2.getScore(), 0.01);
            assertEquals(2, a3.getScore(), 0.01);

        } finally {
            instance.close();
        }
    }

    @Test
    public void shouldApplyPositiveAndNegativeScore() {
        Lipid l1 = new Lipid(1, "L1", "C52H104O6", "TG", 52, 3);
        Lipid l2 = new Lipid(2, "L2", "C54H108O6", "TG", 54, 3);

        // L2 (más carbonos) eluye antes (RT=5), lo cual es incorrecto
        Annotation a1 = new Annotation(l1, 850.0, 1e6, 6.0, Set.of(new Peak(850.0, 1e6))); // RT más tarde
        Annotation a2 = new Annotation(l2, 870.0, 1e6, 5.0, Set.of(new Peak(870.0, 1e6))); // RT más pronto

        LipidScoreUnit unit = new LipidScoreUnit();
        unit.getAnnotations().add(a1);
        unit.getAnnotations().add(a2);

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(unit);
        try {
            instance.fire();

            assertEquals(-1, a1.getScore(), 0.01);
            assertEquals(-1, a2.getScore(), 0.01);

        } finally {
            instance.close();
        }
    }*/


    @Test
    public void score1BasedOnRTCarbonNumbers() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", "TG", 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", "TG", 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }

    }

    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void score1BasedOnRTDoubleBonds() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", "TG", 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", "TG", 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 11d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void score1BasedOnLipidType() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", "PI", 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", "PG", 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", "PC", 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 11d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(1.0, annotation1.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation2.getNormalizedScore(), 0.01);
            assertEquals(1.0, annotation3.getNormalizedScore(), 0.01);

        }
        finally {
            instance.close();
        }
    }

    // !! TODO. PART II OF THE PRACTICE. The negative evidence.
    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     * The RT order of lipids with the same number of carbons and double bonds is the same
     * -> PG < PE < PI < PA < PS << PC.
     */
    @Test
    public void negativeScoreBasedOnRTNumberOfCarbons() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "PI 34:0", "C43H83O13P", "PI", 54, 0); // MZ of [M+H]+ = 839.56441
        Lipid lipid2 = new Lipid(2, "PG 34:0", "C40H79O10P", "PG", 54, 0); // MZ of [M+H]+ = 751.54836
        Lipid lipid3 = new Lipid(3, "PC 34:0", "C42H84NO8P", "PC", 54, 0); // MZ of [M+H]+ = 762.60073
        Annotation annotation1 = new Annotation(lipid1, 839.5644179056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 751.54836, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }


    /**
     * !!TODO Test to check the elution order of the lipids. The elution order is based on the number of double bonds if the lipid type and the number of
     * carbons is the same. The higher the number of double bonds, the shorter the RT.
     */
    @Test
    public void negativeScoreBasedOnRTDoubleBonds() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 54:4", "C57H102O6", "TG", 54, 4); // MZ of [M+H]+ = 883.77492
        Lipid lipid3 = new Lipid(3, "TG 54:2", "C57H106O6", "TG", 54, 2); // MZ of [M+H]+ = 887.80622
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 883.77492, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 887.80622, 10E5, 8d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1

            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself

        }
        finally {
            instance.close();
        }
    }

    /**
     * Test to check the elution order of the lipids. The elution order is based on the number of carbons if the lipid type and the number of
     * double bonds is the same. The larger the number of carbons, the longer the RT.
     */
    @Test
    public void negativeScoreBasedOnLipidType() {
        // Assume lipids already annotated
        LOG.info("Creating RuleUnit");
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        // TODO CHECK THE Monoisotopic MASSES OF THE COMPOUNDS IN https://chemcalc.org/

        Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3); // MZ of [M+H]+ = 885.79057
        Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", "TG", 52, 3); // MZ of [M+H]+ = 857.75927
        Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", "TG", 56, 3); // MZ of [M+H]+ = 913.82187
        Annotation annotation1 = new Annotation(lipid1, 885.79056, 10E6, 10d, IonizationMode.POSITIVE);
        Annotation annotation2 = new Annotation(lipid2, 857.7593, 10E7, 9d, IonizationMode.POSITIVE);
        Annotation annotation3 = new Annotation(lipid3, 913.822, 10E5, 8d, IonizationMode.POSITIVE);

        LOG.info("Insert data");

        try {
            lipidScoreUnit.getAnnotations().add(annotation1);
            lipidScoreUnit.getAnnotations().add(annotation2);
            lipidScoreUnit.getAnnotations().add(annotation3);

            LOG.info("Run query. Rules are also fired");
            instance.fire();

            // Here the logic that we expect. In this case we expect the full 3 annotations to have a positive score of 1


            assertEquals(0d, annotation1.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(0d, annotation2.getNormalizedScore(), 0.01); // !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself
            assertEquals(-1.0, annotation3.getNormalizedScore(), 0.01); // !! !! TODO the scores should be between -1 and 1. It is done, but check it out for yourself


        }
        finally {
            instance.close();
        }

    }

}

