package lipid;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdductDetectionTest {
    Lipid lipid;
    Peak mH;
    Peak mNa;
    Peak p1;
    Peak p2;


    @Before
    public void setup() {
        //Lipid:theorical model of lipid (compoundId, name, formula,lipidType,carbonCount,doubleBondsCount)
        lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", "PC", 34, 1);

        // Peak [M+H]+
        mH = new Peak(700.500, 100000.0);

        // Peak [M+Na]+
        mNa = new Peak(722.482, 80000.0);
        //Crea two peaks with ~21.98 Da(Daltons) difference
        double monoMass = 723.0;
        double mz_MplusH = monoMass + 1.007276;
        double mz_MplusNa = monoMass + 22.989218;
        p1 = new Peak(mz_MplusH, 100000.0); // menor m/z
        p2 = new Peak(mz_MplusNa, 80000.0);
    }
// !!TODO For the adduct detection both regular algorithms or drools can be used as far the tests are passed.



    @Test
    public void shouldDetectAdductBasedOnMzDifference() {

        // Given two peaks with ~21.98 Da difference (e.g., [M+H]+ and [M+Na]+)
        Peak mH = new Peak(700.500, 100000.0); // [M+H]+
        Peak mNa = new Peak(722.482, 80000.0);  // [M+Na]+
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", "PC", 34, 1);

        double annotationMZ = 700.49999d;
        double annotationIntensity = 80000.0;
        double annotationRT = 6.5d;
        Annotation annotation = new Annotation(lipid, annotationMZ, annotationIntensity, annotationRT, IonizationMode.POSITIVE, Set.of(mH, mNa));


        // Then we should call the algorithmic/knowledge system rules fired to detect the adduct and Set it!
        //
        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }


    @Test
    public void shouldDetectLossOfWaterAdduct() {
        Peak mh = new Peak(700.500, 90000.0);        // [M+H]+
        Peak mhH2O = new Peak(682.4894, 70000.0);     // [M+H–H₂O]+, ~18.0106 Da less

        Lipid lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", "PE", 36, 2);
        Annotation annotation = new Annotation(lipid, mh.getMz(), mh.getIntensity(), 7.5d, IonizationMode.POSITIVE, Set.of(mh, mhH2O));



        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }

    @Test
    public void shouldDetectDoublyChargedAdduct() {
        // Assume real M = (700.500 - 1.0073) = 699.4927
        // So [M+2H]2+ = (M + 2.0146) / 2 = 350.7536
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.754, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3, "TG 54:3", "C57H104O6", "TG", 54, 3);
        Annotation annotation = new Annotation(lipid, singlyCharged.getMz(), singlyCharged.getIntensity(), 10d, IonizationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }
}
