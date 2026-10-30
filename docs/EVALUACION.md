<!-- evaluacion:inicio -->
# Evaluación — Stark-Industries123/PROYECTO-FINAL-EUYIN

> Documento **generado automáticamente** por la cátedra de Programación II (RepoSense + análisis de calidad POO/SOLID). **No editar a mano**: se regenera en cada evaluación.
> Rama evaluada: `main`. Análisis **heurístico** (regex + métricas): es una criba para orientar la revisión, **no una nota**.
> Generado: 2026-10-30.

## 1. Veredicto

| Aspecto | Resultado |
|---|---|
| Índice de calidad POO/SOLID | **5/100** |
| Código (LOC) | 4,795 |
| Archivos de código | 68 |
| Clases (bien usadas) | 80 (25 OK) |
| Código dentro de clases (%POO) | 87% |
| Funciones fuera de clases | 0 |
| Duplicación lógica | 5.6% |
| Señales S/O/L/I/D | 17/1/0/0/13 |

> Este informe **describe** lo que el análisis midió con los criterios ya establecidos de la materia. No es una nota.

### Semáforo de aprobación

Criterios de `CRITERIOS-CALIDAD.md`. **Aprobar** se juega con el perfil `estandar`; **destacar**, con el `exigente` (meta de excelencia).

| Semáforo | Resultado |
|---|---|
| Aprobar (`estandar`) | **no cumple** — 3/4 |
| Destacar (`exigente`) | **no cumple** — 1/5 |

**Criterios para aprobar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 85% | 87% | sí |
| Duplicación lógica | ≤ 10% | 5.6% | sí |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Complejidad alta, Acoplamiento alto (CBO), Switch sobre tipos (OCP) | no |

**Criterios para destacar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 95% | 87% | no |
| Duplicación lógica | ≤ 5% | 5.6% | no |
| Funciones fuera de clases | ≤ 1 | 0 | sí |
| Señales POO/SOLID críticas | ninguna | Clase demasiado grande (SRP), Baja cohesión (LCOM), Complejidad alta, Acoplamiento alto (CBO), Switch sobre tipos (OCP) | no |
| Índice de calidad POO/SOLID | ≥ 60 | 5/100 | no |

## 2. Autoría (RepoSense)

| Autor | Commits | Código (LOC) | Tests (LOC) | Docs (LOC) | Días activos | % del código |
|---|---:|---:|---:|---:|---:|---:|
| Eugenio Mosso | 14 | 3,088 | 2,382 | 2 | 10 | 100% |

> _El LOC de autoría lo calcula RepoSense sobre el historial y puede diferir del LOC de código del análisis de calidad (que mira solo el estado de `main`)._

## 3. Aspectos evaluados

Cada aspecto indica **qué mide**, **por qué importa** y **tu resultado**. Cuando el análisis reportó una señal, se agregan preguntas para **investigar la causa** (no hay recetas: entender el porqué es parte de la evaluación).

### 1. Índice de calidad POO/SOLID (indice)

- **Qué mide:** Un resumen numérico (0-100) que combina la riqueza de clases bien usadas con la penalización por señales POO/SOLID y duplicación, y luego aplica topes duros.
- **Por qué importa:** Ordena y prioriza la revisión: un índice bajo indica varias señales que conviene mirar juntas.
- **Tu resultado:** índice **5/100**

### 2. Duplicación lógica (DRY) (duplicacion) — con señales

- **Qué mide:** Porcentaje de bloques de LÓGICA repetidos. Quedan fuera imports/includes, anotaciones, declaraciones de campos y comentarios/Javadoc/docstrings: esos bloques son boilerplate, no lógica.
- **Por qué importa:** Cada copia de lógica se mantiene por separado: un arreglo hay que repetirlo y los defectos se multiplican.
- **Tu resultado:** **5.6%** de duplicación lógica
- **Para investigar la causa:**
    - ¿El trabajo se repartió por pantallas o archivos y se integró copiando y pegando bloques?
    - ¿Qué zonas del sistema cambiaron juntas más de una vez (señal de lógica compartida)?
    - ¿Hubo un lugar común acordado para la lógica repetida antes de empezar, o cada integrante resolvió lo suyo?

### 3. Adopción de POO (% de código en clases) (poo) — sin señales

- **Qué mide:** Qué porción del código vive dentro de clases (no de funciones sueltas).
- **Por qué importa:** El diseño orientado a objetos es el eje de la materia: el comportamiento debería vivir como método de una clase.
- **Tu resultado:** **87.1%** del código dentro de clases

### 4. Funciones fuera de clases (sueltas) — sin señales

- **Qué mide:** Funciones de nivel de módulo que no pertenecen a ninguna clase (en esta materia el comportamiento debe ser método de una clase).
- **Por qué importa:** Son la forma más directa de código procedural en un proyecto que debería ser orientado a objetos.
- **Tu resultado:** **0** función(es) fuera de clases

### 5. Clase demasiado grande (SRP) (god) — con señales

- **Qué mide:** Clases con demasiadas líneas o métodos de LÓGICA. No cuentan los getters/setters triviales ni la cantidad de campos por sí sola.
- **Por qué importa:** Una clase que hace de todo cambia por muchos motivos y cuesta entenderla y probarla.
- **Tu resultado:** **1** clase(s) demasiado grande(s)
- **Para investigar la causa:**
    - ¿La clase creció por acumulación de responsabilidades?
    - ¿Qué partes de la clase se usan siempre juntas y cuáles no?
    - ¿Se podría describir su responsabilidad en una sola frase?

### 6. Baja cohesión (LCOM) (cohesion) — con señales

- **Qué mide:** Métodos y campos de una clase que casi no se relacionan entre sí.
- **Por qué importa:** Una unidad que cambia por motivos distintos y comparte poca información suele esconder más de una responsabilidad.
- **Tu resultado:** **3** clase(s) con baja cohesión
- **Para investigar la causa:**
    - ¿Qué métodos de la clase no comparten estado con el resto?
    - ¿Cambian por motivos distintos según la tarea?
    - ¿Se pensó su responsabilidad antes de escribirla, o se fue agregando?

### 7. Complejidad alta (complejidad) — con señales

- **Qué mide:** Complejidad ciclomática: cantidad de caminos posibles dentro de un método o función (condicionales, bucles, casos).
- **Por qué importa:** Muchos caminos = difícil de seguir y de probar; suelen esconderse casos sin cubrir.
- **Tu resultado:** **3** método(s)/función(es) con complejidad alta
- **Para investigar la causa:**
    - ¿Se agregaron condiciones a medida que aparecían casos nuevos?
    - ¿Se probaron todos los caminos o solo el caso principal?
    - ¿Hay reglas de negocio mezcladas con detalles técnicos?

### 8. Métodos/funciones largos o con muchos parámetros (tamano) — con señales

- **Qué mide:** Tamaño de los métodos/funciones y cantidad de parámetros.
- **Por qué importa:** Una función que hace varias cosas cuesta leerla, nombrarla y reutilizarla.
- **Tu resultado:** **15** método(s)/función(es) largos o con muchos parámetros
- **Para investigar la causa:**
    - ¿La función creció por acumulación de pasos?
    - ¿Agrupa operaciones que podrían describirse por separado?
    - ¿Los parámetros representan una misma idea (un objeto) o cosas sueltas?

### 9. Acoplamiento alto (CBO) (acoplamiento) — con señales

- **Qué mide:** Cuánto depende una clase de otros tipos del proyecto.
- **Por qué importa:** Piezas muy enlazadas hacen que un cambio se propague y que sea difícil reemplazar partes.
- **Tu resultado:** **16** clase(s) muy acoplada(s)
- **Para investigar la causa:**
    - ¿La clase conoce detalles internos de muchas otras?
    - ¿Se puede cambiar una parte sin tocar las demás?
    - ¿Los colaboradores llegan como parámetros o se crean por dentro?

### 10. Herencia profunda (DIT) (herencia) — sin señales

- **Qué mide:** Profundidad de la cadena de herencia de una clase.
- **Por qué importa:** Jerarquías muy profundas vuelven difícil saber de dónde viene cada comportamiento.
- **Tu resultado:** **0** clase(s) con herencia profunda

### 11. Switch sobre tipos (OCP) (ocp) — con señales

- **Qué mide:** Chequeos repetidos de tipo (`instanceof`/`is`/`typeof`) que equivalen a un switch sobre el tipo.
- **Por qué importa:** Cada tipo nuevo obliga a tocar varios lugares, en vez de extender con una implementación nueva.
- **Tu resultado:** **1** clase(s) con switch sobre tipos
- **Para investigar la causa:**
    - ¿Qué operación se resuelve preguntando el tipo?
    - ¿Qué se toca cada vez que aparece un tipo o caso nuevo?
    - ¿El comportamiento podría vivir en la propia jerarquía de tipos?

### 12. Interfaz con demasiados métodos (ISP) (isp) — sin señales

- **Qué mide:** Interfaces o clases abstractas con muchos métodos declarados.
- **Por qué importa:** Obliga a implementar operaciones que no se usan y acopla a los clientes a métodos que no necesitan.
- **Tu resultado:** **0** interfaz(es) con demasiados métodos

### 13. Override vacío o 'no implementado' (LSP) (lsp) — sin señales

- **Qué mide:** Métodos sobrescritos que quedan vacíos o lanzan 'no implementado'.
- **Por qué importa:** Suele indicar que la jerarquía no representa bien el problema y rompe la sustitución entre clases.
- **Tu resultado:** **0** override(s) vacío(s) o 'no implementado'

### 14. Dependencia de concretos y falta de abstracciones (DIP) (dip) — con señales

- **Qué mide:** Instanciación directa de clases concretas y ausencia de interfaces o clases abstractas pese al acoplamiento.
- **Por qué importa:** Depender de implementaciones concretas dificulta cambiar o probar una parte sin arrastrar las demás.
- **Tu resultado:** **13** dependencia(s) de concretos sin abstracciones
- **Para investigar la causa:**
    - ¿La clase crea por dentro a sus colaboradores en vez de recibirlos?
    - ¿Hay puntos de variación que convendría expresar como abstracción?
    - ¿Cómo se probaría esa clase cambiando una implementación?

### 15. Clase sin métodos (solo datos) (data) — con señales

- **Qué mide:** Clases que solo acumulan campos y no tienen comportamiento.
- **Por qué importa:** Separar datos de comportamiento reparte por el proyecto la lógica que debería estar cerca de esos datos.
- **Tu resultado:** **10** clase(s) sin métodos (solo datos)
- **Para investigar la causa:**
    - ¿Dónde vive el comportamiento que opera sobre esos datos?
    - ¿La clase representa un concepto del dominio o un contenedor técnico?
    - ¿Se evita la lógica de negocio dentro de la clase por comodidad?

### 16. Campos públicos (encapsulación) (encapsulacion) — con señales

- **Qué mide:** Clases que exponen campos sin control de acceso.
- **Por qué importa:** Expone el estado interno y hace que cualquier cambio dependa de quién lo modifica desde afuera.
- **Tu resultado:** **23** clase(s) con campos públicos
- **Para investigar la causa:**
    - ¿Se accede al campo desde otras clases para evitar un método?
    - ¿Qué reglas deberían respetarse al modificar ese estado?
    - ¿Los campos públicos se usan más como configuración que como estado?

### 17. Clase solo con métodos estáticos (utilitaria) (static) — sin señales

- **Qué mide:** Clases donde todos los métodos son estáticos.
- **Por qué importa:** Es comportamiento procedural disfrazado de clase: no se instancia ni participa del modelo de objetos.
- **Tu resultado:** **0** clase(s) solo con métodos estáticos

## 4. Anexo: dónde mirar

Archivos donde el análisis encontró cada señal (sin número de línea, a propósito):

**Clase demasiado grande (SRP) (god)** — 1 archivo(s):
- `app/src/test/kotlin/fisicas/motor/SimuladorFisicoTest.kt`

**Baja cohesión (LCOM) (cohesion)** — 2 archivo(s):
- `app/src/main/kotlin/condiciones/dominio/PuntoRosaVientos.kt`
- `app/src/main/kotlin/fisicas/motor/Simulador.kt`

**Complejidad alta (complejidad)** — 2 archivo(s):
- `app/src/main/kotlin/condiciones/io/LectorConsola.kt`
- `app/src/main/kotlin/fisicas/motor/Simulador.kt`

**Métodos/funciones largos o con muchos parámetros (tamano)** — 10 archivo(s):
- `app/src/main/kotlin/condiciones/servicio/CalculadorRumboRosa32Service.kt`
- `app/src/main/kotlin/condiciones/ui/PresentadorRosaVientosService.kt`
- `app/src/main/kotlin/fisicas/motor/Simulador.kt`
- `app/src/main/kotlin/org/example/App.kt`
- `app/src/test/kotlin/condiciones/dominio/PuntoRosaVientosTest.kt`
- `app/src/test/kotlin/condiciones/servicio/CalculadorRumboRosa32ServiceTest.kt`
- `app/src/test/kotlin/config/DireccionVientoSectorTest.kt`
- `app/src/test/kotlin/fisicas/motor/SimuladorFisicoTest.kt`
- `app/src/test/kotlin/org/example/AppTest.kt`
- `app/src/test/kotlin/ui/GraficadorConsolaMapeoTest.kt`

**Acoplamiento alto (CBO) (acoplamiento)** — 16 archivo(s):
- `app/src/main/kotlin/condiciones/ConfiguradorCondicionesApp.kt`
- `app/src/main/kotlin/condiciones/servicio/CalculadorRumboRosa32Service.kt`
- `app/src/main/kotlin/condiciones/ui/SolicitadorEntradaService.kt`
- `app/src/main/kotlin/config/ConfigSimulacion.kt`
- `app/src/main/kotlin/config/DireccionVientoSector.kt`
- `app/src/main/kotlin/fisicas/motor/PhysicsEngineImpl.kt`
- `app/src/main/kotlin/org/example/App.kt`
- `app/src/test/kotlin/condiciones/servicio/CalculadorRumboRosa32ServiceTest.kt`
- `app/src/test/kotlin/config/DireccionVientoSectorTest.kt`
- `app/src/test/kotlin/fisicas/config/WindConfigTest.kt`
- `app/src/test/kotlin/fisicas/fuerzas/CalculadorFuerzasNewtonTest.kt`
- `app/src/test/kotlin/fisicas/motor/PhysicsEngineTest.kt`
- `app/src/test/kotlin/fisicas/motor/SimuladorFisicoTest.kt`
- `app/src/test/kotlin/org/example/AppTest.kt`
- `app/src/test/kotlin/ui/GraficadorConsolaVerticalTest.kt`
- `app/src/test/kotlin/ui/ValidadorEntradaTest.kt`

**Switch sobre tipos (OCP) (ocp)** — 1 archivo(s):
- `app/src/main/kotlin/ui/MenuConsola.kt`

**Dependencia de concretos y falta de abstracciones (DIP) (dip)** — 13 archivo(s):
- `app/src/main/kotlin/condiciones/ConfiguradorCondicionesApp.kt`
- `app/src/main/kotlin/condiciones/servicio/CalculadorRumboRosa32Service.kt`
- `app/src/main/kotlin/condiciones/ui/SolicitadorEntradaService.kt`
- `app/src/main/kotlin/config/ConfigSimulacion.kt`
- `app/src/main/kotlin/config/DireccionVientoSector.kt`
- `app/src/main/kotlin/fisicas/motor/PhysicsEngineImpl.kt`
- `app/src/main/kotlin/org/example/App.kt`
- `app/src/test/kotlin/fisicas/config/WindConfigTest.kt`
- `app/src/test/kotlin/fisicas/fuerzas/CalculadorFuerzasNewtonTest.kt`
- `app/src/test/kotlin/fisicas/motor/PhysicsEngineTest.kt`
- `app/src/test/kotlin/fisicas/motor/SimuladorFisicoTest.kt`
- `app/src/test/kotlin/org/example/AppTest.kt`
- `app/src/test/kotlin/ui/GraficadorConsolaVerticalTest.kt`

**Clase sin métodos (solo datos) (data)** — 10 archivo(s):
- `app/src/main/kotlin/fisicas/colision/ManejadorReboteSuelo.kt`
- `app/src/main/kotlin/fisicas/config/BounceConfig.kt`
- `app/src/main/kotlin/fisicas/config/ProjectileConfig.kt`
- `app/src/main/kotlin/fisicas/fuerzas/CalculadorFuerzasNewton.kt`
- `app/src/main/kotlin/fisicas/motor/PhysicsEngineImpl.kt`
- `app/src/main/kotlin/fisicas/motor/PuntoTrayectoria.kt`
- `app/src/test/kotlin/fisicas/colision/ManejadorReboteSueloTest.kt`
- `app/src/test/kotlin/fisicas/config/WindConfigTest.kt`
- `app/src/test/kotlin/fisicas/estado/ProjectileStateTest.kt`
- `app/src/test/kotlin/fisicas/vector/Vector2DTest.kt`

**Campos públicos (encapsulación) (encapsulacion)** — 22 archivo(s):
- `app/src/main/kotlin/condiciones/dominio/CuadranteRosa.kt`
- `app/src/main/kotlin/condiciones/dominio/PuntoRosaVientos.kt`
- `app/src/main/kotlin/condiciones/dominio/Viento.kt`
- `app/src/main/kotlin/config/ConfigSimulacion.kt`
- `app/src/main/kotlin/fisicas/colision/ManejadorReboteSuelo.kt`
- `app/src/main/kotlin/fisicas/config/BounceConfig.kt`
- `app/src/main/kotlin/fisicas/config/ProjectileConfig.kt`
- `app/src/main/kotlin/fisicas/config/WindConfig.kt`
- `app/src/main/kotlin/fisicas/estado/ProjectileState.kt`
- `app/src/main/kotlin/fisicas/fuerzas/CalculadorFuerzasNewton.kt`
- `app/src/main/kotlin/fisicas/motor/PhysicsEngineImpl.kt`
- `app/src/main/kotlin/fisicas/motor/PuntoTrayectoria.kt`
- `app/src/main/kotlin/fisicas/motor/Simulador.kt`
- `app/src/main/kotlin/org/example/App.kt`
- `app/src/main/kotlin/ui/GraficadorConsola.kt`
- `app/src/main/kotlin/ui/MenuConsola.kt`
- `app/src/test/kotlin/fisicas/colision/ManejadorReboteSueloTest.kt`
- `app/src/test/kotlin/fisicas/config/WindConfigTest.kt`
- `app/src/test/kotlin/fisicas/estado/ProjectileStateTest.kt`
- `app/src/test/kotlin/fisicas/fuerzas/CalculadorFuerzasNewtonTest.kt`
- `app/src/test/kotlin/fisicas/motor/PhysicsEngineTest.kt`
- `app/src/test/kotlin/fisicas/vector/Vector2DTest.kt`

## 5. Evolución por commit

Métricas calculadas sobre el estado de `main` en **cada commit** (12 commits con trabajo), del primero al último. La calidad usa el perfil `exigente`; el aporte de código lo mide RepoSense. El índice puede **bajar** al crecer el proyecto: se acumulan señales.

```mermaid
xychart-beta
    title "Índice POO/SOLID a lo largo de los commits"
    x-axis ["5e46215", "8beeedf", "3be7fbe", "7284531", "1fcb049", "1a12f7b", "88970ff", "554234e", "f7ce108", "20e1594", "c50dc6f", "0933d54"]
    y-axis "Índice" 0 --> 100
    line [0, 0, 38, 33, 33, 17, 20, 18, 11, 13, 5, 5]
```

| # | Fecha | Commit | Autor | LOC | Índice | %POO | Duplic. | Sueltas |
|---|---|---|---|---:|---:|---:|---:|---:|
| 1 | 2026-08-21 | `5e46215` | euge | 0 | 0 | 0% | 0.0% | 0 |
| 2 | 2026-08-24 | `8beeedf` | dqmdz-etec | 0 | 0 | 0% | 0.0% | 0 |
| 3 | 2026-09-28 | `3be7fbe` | euyin | 105 | 38 | 30% | 0.0% | 1 |
| 4 | 2026-09-28 | `7284531` | euyin | 174 | 33 | 36% | 0.0% | 3 |
| 5 | 2026-10-09 | `1fcb049` | euyin | 231 | 33 | 38% | 0.0% | 3 |
| 6 | 2026-10-09 | `1a12f7b` | euyin | 430 | 17 | 39% | 0.0% | 7 |
| 7 | 2026-10-09 | `88970ff` | euyin | 393 | 20 | 43% | 0.0% | 5 |
| 8 | 2026-10-16 | `554234e` | euyin | 445 | 18 | 50% | 0.0% | 5 |
| 9 | 2026-10-16 | `f7ce108` | euyin | 718 | 11 | 52% | 0.0% | 9 |
| 10 | 2026-10-19 | `20e1594` | euyin | 1,845 | 13 | 92% | 1.3% | 0 |
| 11 | 2026-10-30 | `c50dc6f` | euyin | 4,652 | 5 | 86% | 5.8% | 0 |
| 12 | 2026-10-30 | `0933d54` | euyin | 4,795 | 5 | 87% | 5.6% | 0 |

---
_Informe generado por la cátedra. Los criterios y umbrales de medición corresponden al perfil `exigente` de `calidad.py`; el semáforo de aprobación, a `CRITERIOS-CALIDAD.md` (ver `README.md`). Ante dudas, revisá el archivo señalado._
<!-- evaluacion:fin -->
