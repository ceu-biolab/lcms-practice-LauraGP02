package adduct;

import adduct.AdductList;
import org.drools.core.phreak.EagerPhreakBuilder;

/**
 * Utility class for performing mass calculations related to adducts in mass spectrometry.
 * This class provides static methods to:
 *      Calculate the monoisotopic mass of a compound from an experimental m/z and adduct.
 *      Compute the parts-per-million (ppm) difference between experimental and theoretical masses.
 *      Estimate tolerance windows based on a ppm value.
 *
 * @author laura
 */
public class Adduct {

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass Mass measured by MS
     * @param theoreticalMass  Theoretical mass of the compound
     */
    public static int calculatePPMIncrement(Double experimentalMass, Double theoreticalMass) {
        int ppmIncrement;
        ppmIncrement = (int) Math.round(Math.abs((experimentalMass - theoreticalMass) * 1000000
                / theoreticalMass));
        return ppmIncrement;
    }

    /**
     * Returns the ppm difference between measured mass and theoretical mass
     *
     * @param experimentalMass Mass measured by MS
     * @param ppm          ppm of tolerance
     */
    public static double calculateDeltaPPM(Double experimentalMass, int ppm) {
        double deltaPPM;
        deltaPPM = Math.round(Math.abs((experimentalMass * ppm) / 1000000));
        return deltaPPM;

    }


    /**
     * Calculates the estimated monoisotopic mass for a given adduct.
     * General formula: M = m/z * Z + A (rearranged depending on the adduct type).
     *
     * @param mz     experimental m/z value
     * @param adduct name of the adduct (e.g., [M+H]+, [2M+Na]+, [M+2H]2+)
     * @return estimated monoisotopic mass
     */
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {
        /*
        if Adduct is single charge the formula is M = mz +- adductMass. Charge is 1 so it does not affect
        if Adduct is double or triple charged the formula is M =( mz - adductMass ) * charge
        if adduct is a dimer the formula is M =  (mz - adductMass) / numberOfMultimer
        return monoisotopicMass;
         */
        Double A = null;

        if (AdductList.MAPMZPOSITIVEADDUCTS.containsKey(adduct)) {
            A = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        } else if (AdductList.MAPMZNEGATIVEADDUCTS.containsKey(adduct)) {
            A = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        }

        if (A == null) {
            return null;
        }

        int multimer = 1;
        if (adduct.startsWith("[2M")) {
            multimer = 2;
        }

        int charge = 1;
        if (adduct.endsWith("2+") || adduct.endsWith("2-")) {
            charge = 2;
        }

        // mz = (nM + adductMass) / charge
        // (mz * charge) - adductMass = n * M
        // M = ((mz * z) − adductMas) / n
        if (A < 0){
            return ((mz * charge) + A) / multimer;
        } else {
            return ((mz * charge) + A) / multimer;
        }
    }
}


