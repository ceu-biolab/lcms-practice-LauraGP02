package lipid;

/**
 * Represents a peak in a mass spectrum with m/z and intensity values.
 *
 * This class models an individual signal detected in the spectrum,
 * characterized by its mass-to-charge ratio (m/z) and its relative intensity.
 * Peaks can be compared by m/z and are commonly used in grouping and
 * annotation steps to identify candidate compounds or adducts.
 *
 * The class implements {@link Comparable} based on m/z to allow sorting of peaks.
 * Equality and hash code are also based solely on m/z value.
 *
 * @author laura
 */
public class Peak implements Comparable<Peak>{

    private final double mz;
    private final double intensity;

    public Peak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public double getMz() {
        return mz;
    }

    public double getIntensity() {
        return intensity;
    }

    @Override
    public String toString() {
        return String.format("Peak(mz=%.4f, intensity=%.2f)", mz, intensity);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(mz) * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Peak)) return false;
        Peak other = (Peak) obj;
        return Double.compare(mz, other.mz) == 0;
    }

    /**
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Peak o) {
        return Double.compare( o.mz,this.mz);
    }

}
