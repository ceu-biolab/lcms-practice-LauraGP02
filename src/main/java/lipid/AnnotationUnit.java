package lipid;

import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.DataSource;

/**
 * Rule unit wrapper class for Drools that provides a data context
 * containing lipid annotation facts to be used in a rule-based system.
 *
 * @author laura
 */
public class AnnotationUnit implements RuleUnitData {
    private final DataStore<Annotation> annotations;

    public AnnotationUnit() {
        this(DataSource.createStore());
    }

    public AnnotationUnit(DataStore<Annotation> annotations) {
        this.annotations = annotations;
    }

    public DataStore<Annotation> getAnnotations() {
        return annotations;
    }
}
