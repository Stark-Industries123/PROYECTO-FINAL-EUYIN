package condiciones.io

import java.io.ByteArrayInputStream
import java.util.NoSuchElementException
import java.util.Scanner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * Pruebas unitarias para [LectorConsolaEstandar] y métodos auxiliares de [LectorConsola].
 */
class LectorConsolaEstandarTest {

    @Test
    fun testLeerLineaConScannerExitoso() {
        val entrada = "primera linea\nsegunda linea\n"
        val scanner = Scanner(ByteArrayInputStream(entrada.toByteArray()))
        val lector = LectorConsolaEstandar(scanner = scanner)

        assertEquals("primera linea", lector.leerLinea())
        assertEquals("segunda linea", lector.leerLinea())
        assertNull(lector.leerLinea())
    }

    @Test
    fun testLeerLineaConProveedorLambda() {
        val lineas = mutableListOf("dato1", "dato2")
        val lector = LectorConsolaEstandar(proveedor = { if (lineas.isNotEmpty()) lineas.removeAt(0) else null })

        assertEquals("dato1", lector.leerLinea())
        assertEquals("dato2", lector.leerLinea())
        assertNull(lector.leerLinea())
    }

    @Test
    fun testLeerLineaManejaExcepciones() {
        val lectorConFallo = LectorConsolaEstandar(proveedor = {
            throw NoSuchElementException("Simulación de fin de flujo")
        })

        assertNull(lectorConFallo.leerLinea())
    }

    @Test
    fun testLeerLineaValidaConBucle() {
        val lineas = mutableListOf("   ", "", "  texto valido  ")
        val lector = LectorConsolaEstandar(proveedor = { if (lineas.isNotEmpty()) lineas.removeAt(0) else null })

        val resultado = lector.leerLineaValida(reintentos = 5)
        assertEquals("texto valido", resultado)
    }

    @Test
    fun testLeerDoubleConBucleYValidacion() {
        val errores = mutableListOf<String>()
        val lineas = mutableListOf("no-numero", "-5.0", "42.5")
        val lector = LectorConsolaEstandar(proveedor = { if (lineas.isNotEmpty()) lineas.removeAt(0) else null })

        val resultado = lector.leerDouble(
            validador = { it > 0.0 },
            onEntradaInvalida = { errores.add(it) }
        )

        assertEquals(42.5, resultado)
        assertEquals(2, errores.size)
        assertEquals("Error: Debe ingresar un valor numérico válido.", errores[0])
    }
}
