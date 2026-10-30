package fisicas.fuerzas

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.abs

/**
 * Pruebas unitarias aisladas para [CalculadorArrastreAerodinamico] y el contrato [CalculadorFuerzaAerodinamica].
 */
class CalculadorArrastreAerodinamicoTest {

    private val delta = 1e-6

    @Test
    fun `calcula exactamente Fx segun formula cuadratica con viento en contra`() {
        val rho = 1.225
        val cd = 0.05
        val area = 0.5
        val calculador: CalculadorFuerzaAerodinamica = CalculadorArrastreAerodinamico(
            densidadAire = rho,
            coeficienteArrastre = cd,
            areaTransversal = area
        )

        // Viento en contra (-20 m/s) y proyectil avanzando (+10 m/s) -> (v_viento - vx) = -30 m/s
        val vViento = -20.0
        val vx = 10.0
        val fxEsperada = -0.5 * rho * (-30.0) * (-30.0) * cd * area

        val fxObtenida = calculador.calcularFuerzaArrastre(vViento, vx)

        assertEquals(fxEsperada, fxObtenida, delta)
        assertTrue(fxObtenida < 0.0, "La fuerza debe ser negativa para frenar e impulsar hacia -X")
    }

    @Test
    fun `determina aceleracion ax dividiendo Fx por la masa inyectada`() {
        val calculador: CalculadorFuerzaAerodinamica = CalculadorArrastreAerodinamico(
            densidadAire = 1.2,
            coeficienteArrastre = 0.1,
            areaTransversal = 0.5
        )

        val vViento = -25.0
        val vx = 5.0
        val masaLiviana = 0.25
        val masaPesada = 5.0

        val fx = calculador.calcularFuerzaArrastre(vViento, vx)
        val axLiviano = calculador.calcularAceleracion(vViento, vx, masaLiviana)
        val axPesado = calculador.calcularAceleracion(vViento, vx, masaPesada)

        assertEquals(fx / masaLiviana, axLiviano, delta)
        assertEquals(fx / masaPesada, axPesado, delta)
        assertTrue(abs(axLiviano) > abs(axPesado), "Menor masa debe producir mayor magnitud de aceleración ax")
    }

    @Test
    fun `velocidad relativa nula produce fuerza y aceleracion cero`() {
        val calculador = CalculadorArrastreAerodinamico()

        assertEquals(0.0, calculador.calcularFuerzaArrastre(12.0, 12.0), delta)
        assertEquals(0.0, calculador.calcularAceleracion(12.0, 12.0, 2.0), delta)
        assertEquals(0.0, calculador.calcularFuerzaArrastre(0.0), delta)
    }

    @Test
    fun `valida parametros fisicos invalidos`() {
        val calculador = CalculadorArrastreAerodinamico()
        assertThrows(IllegalArgumentException::class.java) {
            calculador.calcularAceleracion(0.0, 10.0, 0.0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            CalculadorArrastreAerodinamico(densidadAire = -1.0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            CalculadorArrastreAerodinamico(coeficienteArrastre = -0.1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            CalculadorArrastreAerodinamico(areaTransversal = -0.5)
        }
    }
}
