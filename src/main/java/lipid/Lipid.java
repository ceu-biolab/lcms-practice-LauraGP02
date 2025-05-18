package lipid;

import java.util.Objects;

/**
 *  This class holds the core attributes of a lipid molecule detected via mass spectrometry,
 *  including its compound ID, chemical formula, lipid classification (type),
 *  and structural features such as carbon chain length and number of double bonds.
 *
 * @author laura
 */
public class Lipid {
    private final int compoundId;
    private final String name;
    private final String formula;
    private final String lipidType;
    private final int carbonCount;
    private final int doubleBondsCount;


    /**
     * @param compoundId
     * @param name
     * @param formula
     * @param lipidType
     * @param carbonCount
     * @param doubleBondCount
     */
    public Lipid(int compoundId, String name, String formula, String lipidType, int carbonCount, int doubleBondCount) {
        this.compoundId = compoundId;
        this.name = name;
        this.formula = formula;
        this.lipidType = lipidType;
        this.carbonCount = carbonCount;
        this.doubleBondsCount = doubleBondCount;
    }

    public int getCompoundId() {
        return compoundId;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public String getLipidType() {
        return this.lipidType;
    }

    public int getCarbonCount() {
        return carbonCount;
    }

    public int getDoubleBondsCount() {
        return doubleBondsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lipid)) return false;
        Lipid lipid = (Lipid) o;
        return compoundId == lipid.compoundId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(compoundId);
    }

    @Override
    public String toString() {
        return "Lipid{" +
                "compoundId=" + compoundId +
                ", name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", lipidType='" + lipidType + '\'' +
                ", carbonCount=" + carbonCount +
                ", doubleBondCount=" + doubleBondsCount +
                '}';
    }
}
