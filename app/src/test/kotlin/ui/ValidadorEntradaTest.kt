package ui

import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * Pruebas unitarias directas para el componente de validación y parseo [ValidadorEntrada].
 */
class ValidadorEntradaTest {

    private val validador = ValidadorEntrada()

    @Test
    fun testParsearPlanetaValidoEInvalido() {
        assertIs<Tierra>(validador.parsearPlaneta("1"))
        assertIs<Luna>(validador.parsearPlaneta(" 2 "))
        assertIs<Mercurio>(validador.parsearPlaneta("3"))
        assertIs<Jupiter>(validador.parsearPlaneta("4"))

        assertNull(validador.parsearPlaneta("0"))
        assertNull(validador.parsearPlaneta("5"))
        assertNull(validador.parsearPlaneta("abc"))
        assertNull(validador.parsearPlaneta(null))
    }

    @Test
    fun testParsearVelocidad() {
        val exito = validador.parsearVelocidad("25.5")
        assertIs<ResultadoValidacion.Exito<Double>>(exito)
        assertEquals(25.5, exito.valor)

        val errorNoNumero = validador.parsearVelocidad("abc")
        assertIs<ResultadoValidacion.Error>(errorNoNumero)

        val errorCero = validador.parsearVelocidad("0")
        assertIs<ResultadoValidacion.Error>(errorCero)

        val errorNegativo = validador.parsearVelocidad("-5.0")
        assertIs<ResultadoValidacion.Error>(errorNegativo)
    }

    @Test
    fun testParsearAngulo() {
        val min = validador.parsearAngulo("0.0")
        assertIs<ResultadoValidacion.Exito<Double>>(min)
        assertEquals(0.0, min.valor)

        val max = validador.parsearAngulo("90.0")
        assertIs<ResultadoValidacion.Exito<Double>>(max)
        assertEquals(90.0, max.valor)

        assertIs<ResultadoValidacion.Error>(validador.parsearAngulo("-0.1"))
        assertIs<ResultadoValidacion.Error>(validador.parsearAngulo("90.1"))
        assertIs<ResultadoValidacion.Error>(validador.parsearAngulo("abc"))
    }

    @Test
    fun testParsearMasa() {
        val exito = validador.parsearMasa("2.5")
        assertIs<ResultadoValidacion.Exito<Double>>(exito)
        assertEquals(2.5, exito.valor)

        assertIs<ResultadoValidacion.Error>(validador.parsearMasa("0.0"))
        assertIs<ResultadoValidacion.Error>(validador.parsearMasa("-1.0"))
        assertIs<ResultadoValidacion.Error>(validador.parsearMasa("abc"))
    }

    @Test
    fun testParsearVientoYDireccion() {
        val vientoExito = validador.parsearViento("40.0")
        assertIs<ResultadoValidacion.Exito<Double>>(vientoExito)
        assertEquals(40.0, vientoExito.valor)

        assertIs<ResultadoValidacion.Error>(validador.parsearViento("-1.0"))
        assertIs<ResultadoValidacion.Error>(validador.parsearViento("abc"))

        val direccionExito = validador.parsearDireccion("180.0")
        assertIs<ResultadoValidacion.Exito<Double>>(direccionExito)
        assertEquals(180.0, direccionExito.valor)

        assertIs<ResultadoValidacion.Error>(validador.parsearDireccion("-1.0"))
        assertIs<ResultadoValidacion.Error>(validador.parsearDireccion("361.0"))
    }
}
