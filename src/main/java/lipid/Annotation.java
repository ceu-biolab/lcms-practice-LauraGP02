package lipid;

import adduct.Adduct;
import adduct.AdductList;

import java.util.*;

/**
 * Class to represent the annotation over a lipid
 * Contains information about the lipid, the mass spectrometry data,
 * ionization mode, inferred adduct, and score.
 *
 * @author laura
 */
public class Annotation {

    private final Lipid lipid;
    private double mz;
    private final double intensity; // intensity of the most abundant peak in the groupedPeaks
    private final double rtMin;
    private final IonizationMode ionizationMode;
    private String adduct;
    private final Set<Peak> groupedSignals;
    private int score;
    private int totalScoresApplied;

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IonizationMode ionizationMode) {
        this(lipid, mz, intensity, retentionTime, ionizationMode, Collections.emptySet());
    }

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param groupedSignals
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IonizationMode ionizationMode, Set<Peak> groupedSignals) {
        this.lipid = lipid;
        this.mz = mz;
        this.rtMin = retentionTime;
        this.intensity = intensity;
        this.ionizationMode = ionizationMode;
        this.groupedSignals = new TreeSet<>(groupedSignals);
        this.adduct = "unknown";
        this.score = 0;
        this.totalScoresApplied = 0;
        this.detectAdduct2();
    }


    public Lipid getLipid() {
        return lipid;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getRtMin() {
        return rtMin;
    }

    public IonizationMode getIonizationMode() {
        return ionizationMode;
    }

    public String getAdduct() {
        return adduct;
    }

    public void setAdduct(String adduct) {
        this.adduct = adduct;
    }

    public double getIntensity() {
        return intensity;
    }

    public Set<Peak> getGroupedSignals() {
        return Collections.unmodifiableSet(groupedSignals);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @param delta the score change (positive or negative)
     */
    public void addScore(int delta) {
        this.score += delta;
        this.totalScoresApplied++;
    }

    /**
     * @return  the normalized score, i.e., score divided by number of scoring events
     */
    public double getNormalizedScore() {
        return (double) this.score / this.totalScoresApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        Annotation that = (Annotation) o;
        return Double.compare(that.mz, mz) == 0 &&
                Double.compare(that.rtMin, rtMin) == 0 &&
                Objects.equals(lipid, that.lipid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lipid, mz, rtMin);
    }

    @Override
    public String toString() {
        return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid.getName(), mz, rtMin, adduct, intensity, score);
    }

    /**
     * Detects the most likely adduct associated with this annotation by comparing all possible
     * pairs of peaks and all valid adduct combinations within the ionization mode.
     * The method works as follows:
     *     Selects the appropriate adduct list depending on the ionization mode (positive or negative).
     *     For each pair of adduct candidates and peak combinations in {@code groupedSignals}:
     *     Calculates the monoisotopic mass for each peak assuming a given adduct.
     *     If the masses are nearly equal (within 10 ppm), it assumes both peaks belong to the same compound.
     *     Assigns the current adduct to the one matching this annotation’s m/z value.
     * If no valid adduct is found or the ionization mode is unknown, the adduct remains as "unknown".
     */
    public void detectAdduct2() {
        final double tolerance = 10.0; // margen de error para comparar masas monoisotópicas
        Map<String, Double> adductMap = null;

        if (ionizationMode == IonizationMode.POSITIVE) {
            adductMap = AdductList.MAPMZPOSITIVEADDUCTS;
        } else if (ionizationMode == IonizationMode.NEGATIVE) {
            adductMap = AdductList.MAPMZNEGATIVEADDUCTS;
        } else {
            this.adduct = "unknown";
            return;
        }
        //Recorro todas las comb posibles de adducts
        for (String adduct1 : adductMap.keySet()) {
            for (String adduct2 : adductMap.keySet()) {
                if (adduct1.equals(adduct2)) continue;
                //Recorro todas las comb posibles de Peaks
                for (Peak p1 : groupedSignals) {
                    for (Peak p2 : groupedSignals) {
                        if (p1.equals(p2)) continue; // evitamos comparar un pico consigo mismo
                        //M = f(m/z peak, adduct)
                        Double monoMass1 = Adduct.getMonoisotopicMassFromMZ(p1.getMz(), adduct1);
                        Double monoMass2 = Adduct.getMonoisotopicMassFromMZ(p2.getMz(), adduct2);
                        if (monoMass1 == null || monoMass2 == null) continue;

                        double errorPPM = Adduct.calculatePPMIncrement(monoMass1, monoMass2); // tolerancia de 10 ppm
                        if (errorPPM <= tolerance) {//diff masas practicamente 0, son adducts del mismo compound/lipid
                            //Una vez identificada mi pareja de adducts,escojo cual se corresponde a mi experimental signal(annotation)
                            this.adduct = adduct1;
                            return;
                        }
                    }
                }
            }
        }
    }


    /**
     * @return number of carbon atoms
     */
public int getCarbonCount() {
    return lipid.getCarbonCount();
}

    /**
     * @return number of double bonds
     */
public int getDoubleBondsCount() {
    return lipid.getDoubleBondsCount();
}

    /**
     * @return the lipid type (e.g., PC, PE, PI) of the associated lipid.
     */
public String getLipidType() {
    return lipid.getLipidType();
}

}
