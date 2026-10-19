package ui

import fisicas.motor.PuntoTrayectoria

/**
 * Contrato de abstracción para la visualización y dibujo de trayectorias físicas (DIP).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Los módulos de alto nivel dependen de esta abstracción
 *   y no de implementaciones concretas como la consola de texto o un lienzo gráfico.
 * - Open/Closed Principle (OCP): Permite extender hacia nuevos formatos de salida (SVG, GUI, web,
 *   logs o mocks de test) sin alterar el código consumidor.
 */
interface Graficador {

    /**
     * Dibuja o renderiza la trayectoria del proyectil a partir de la colección de puntos continuos.
     *
     * @param trayectoria Colección inmutable de [PuntoTrayectoria].
     */
    fun graficar(trayectoria: List<PuntoTrayectoria>)
}
