package fisicas.motor

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Pruebas unitarias para el objeto de configuración [ParametrosIntegracion].
 */
class ParametrosIntegracionTest {

    @Test
    fun testValoresPorDefectoYDerivadosNumericos() {
        val parametros = ParametrosIntegracion()
        assertEquals(0.75, parametros.coeficienteRestitucion, 1e-9)
        assertEquals(0.05, parametros.coeficienteArrastre, 1e-9)
        assertEquals(0.1, parametros.pasoTiempoMuestreo, 1e-9)
        assertEquals(100, parametros.calcularSubPasos())
        assertEquals(0.001, parametros.calcularDeltaTiempo(), 1e-9)
        assertEquals(0.025, parametros.calcularFactorArrastre(2.0), 1e-9)
    }

    @Test
    fun testPasoMenorAlSubPasoUsaAlMenosUnSubPaso() {
        val parametros = ParametrosIntegracion(pasoTiempoMuestreo = 0.0005)
        assertEquals(1, parametros.calcularSubPasos())
        assertEquals(0.0005, parametros.calcularDeltaTiempo(), 1e-9)
    }

    @Test
    fun testValidacionesRechazanParametrosInvalidos() {
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion(coeficienteRestitucion = -0.1) }
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion(coeficienteRestitucion = 1.1) }
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion(coeficienteArrastre = -0.01) }
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion(pasoTiempoMuestreo = 0.0) }
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion(tiempoMaximo = 0.0) }
        assertFailsWith<IllegalArgumentException> { ParametrosIntegracion().calcularFactorArrastre(0.0) }
    }
}
