package adduct;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
/**
 * Description of the class.
 *
 * @author laura
 */
public class AducctTest {
    @Test
    public void testGetMonoisotopicMassFromMZ_WithNaAdduct() {
        // Dado un pico de m/z 700.5 con aducto [M+Na]+
        double mz = 700.5;
        String adduct = "[M+Na]+";

        // Cuando se calcula la masa monoisotópica
        Double result = Adduct.getMonoisotopicMassFromMZ(mz, adduct);

        // Entonces el resultado esperado es 700.5 + (-22.989218) = 677.510782
        Double expected = 677.510782;

        // Verificamos que la diferencia sea pequeña (por posibles redondeos)
        assertEquals(expected, result, 0.0001); // margen de error de 0.0001
    }
}
