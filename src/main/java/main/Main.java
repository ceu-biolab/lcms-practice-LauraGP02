package main;

import lipid.Lipid;
import lipid.Annotation;
import lipid.LipidScoreUnit;
import lipid.IonizationMode;
import org.drools.ruleunits.api.RuleUnitInstance;
import org.drools.ruleunits.api.RuleUnitProvider;


/**
 * This class initializes a {@link LipidScoreUnit} with sample {@link Annotation} data,
 * wraps it in a {@link RuleUnitInstance}, and triggers the rule evaluation engine.
 *
 * @author laura
 */
public class Main {

    public static void main(String[] args) {
        //RULE UNIT
        LipidScoreUnit lipidScoreUnit = new LipidScoreUnit();

        //EXECUTION INSTANCE
        RuleUnitInstance<LipidScoreUnit> instance = RuleUnitProvider.get().createRuleUnitInstance(lipidScoreUnit);

        try {
            Lipid lipid1 = new Lipid(1, "TG 54:3", "C57H104O6", "TG", 54, 3);
            Lipid lipid2 = new Lipid(2, "TG 52:3", "C55H100O6", "TG", 52, 3);
            Lipid lipid3 = new Lipid(3, "TG 56:3", "C59H108O6", "TG", 56, 3);

            Annotation a1 = new Annotation(lipid1, 885.79056, 1e7, 10d, IonizationMode.POSITIVE);
            Annotation a2 = new Annotation(lipid2, 857.7593, 1e7, 9d, IonizationMode.POSITIVE);
            Annotation a3 = new Annotation(lipid3, 913.822, 1e7, 11d, IonizationMode.POSITIVE);

            lipidScoreUnit.getAnnotations().add(a1);
            lipidScoreUnit.getAnnotations().add(a2);
            lipidScoreUnit.getAnnotations().add(a3);

            instance.fire();

        } finally {
            instance.close();
        }
    }
}
