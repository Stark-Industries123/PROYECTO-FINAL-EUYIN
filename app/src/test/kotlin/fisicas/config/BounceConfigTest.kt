package fisicas.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BounceConfigTest {

    @Test
    fun testValoresPorDefectoConsistentes() {
        val config = BounceConfig()
        assertEquals(0.75, config.coeficienteRestitucion)
        assertEquals(1.0, config.friccionSuelo)
        assertEquals(0.05, config.umbralReposo)
    }

    @Test
    fun testCoeficienteRestitucionFueraDeRango() {
        assertThrows<IllegalArgumentException> { BounceConfig(coeficienteRestitucion = -0.1) }
        assertThrows<IllegalArgumentException> { BounceConfig(coeficienteRestitucion = 1.05) }
    }

    @Test
    fun testFriccionYUmbralFueraDeRango() {
        assertThrows<IllegalArgumentException> { BounceConfig(friccionSuelo = -0.01) }
        assertThrows<IllegalArgumentException> { BounceConfig(friccionSuelo = 1.5) }
        assertThrows<IllegalArgumentException> { BounceConfig(umbralReposo = -0.01) }
    }
}
