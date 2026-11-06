package fisicas.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProjectileConfigTest {

    @Test
    fun testCreacionValidaConfiguracionProyectil() {
        val config = ProjectileConfig(masa = 2.5, coeficienteArrastre = 0.08)
        assertEquals(2.5, config.masa)
        assertEquals(0.08, config.coeficienteArrastre)
    }

    @Test
    fun testMasaOCoeficienteInvalidoLanzaExcepcion() {
        assertThrows<IllegalArgumentException> { ProjectileConfig(masa = 0.0, coeficienteArrastre = 0.05) }
        assertThrows<IllegalArgumentException> { ProjectileConfig(masa = -1.0, coeficienteArrastre = 0.05) }
        assertThrows<IllegalArgumentException> { ProjectileConfig(masa = 1.0, coeficienteArrastre = -0.01) }
    }
}
