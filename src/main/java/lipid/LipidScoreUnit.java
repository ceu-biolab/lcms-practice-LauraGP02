package lipid;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import java.util.HashSet;

/**
 * Rule unit for scoring lipid annotations in a Drools-based rule engine.
 *
 * @author laura
 */
public class LipidScoreUnit implements RuleUnitData {

    private final DataStore<Annotation> annotations;

    public LipidScoreUnit() {
        this(DataSource.createStore());
    }

    public LipidScoreUnit(DataStore<Annotation> annotations) {
        this.annotations = annotations;

    }

    public DataStore<Annotation> getAnnotations() {
        return annotations;
    }

}
