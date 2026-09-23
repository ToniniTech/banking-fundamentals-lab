# Banking Fundamentals Lab

Proyecto educativo incremental para fortalecer fundamentos de Java y Spring mediante un dominio bancario pequeno.

## Unidad 1: Java Core

La primera unidad no utiliza Spring ni persistencia. El objetivo es atribuir cada comportamiento al lenguaje y al modelo de dominio.

- `Money`: value object inmutable basado en `BigDecimal` y `Currency`;
- `BankAccount`: entidad con identidad estable e invariantes de saldo;
- excepciones de dominio;
- laboratorios sobre igualdad, hashing, mutabilidad y dinero.

## Requisitos

- Java 21;
- PowerShell o una terminal compatible con Maven Wrapper.

## Ejecutar las pruebas

En Windows:

```powershell
.\mvnw.cmd test
```

Antes de ejecutar los laboratorios, lee y responde las preguntas de [`docs/unit-01-java-core.md`](docs/unit-01-java-core.md).

## Metodo de aprendizaje

```text
Predecir -> ejecutar -> observar -> explicar -> modificar -> reparar
```

No se avanza a Spring hasta poder explicar las decisiones de `Money` y `BankAccount` sin depender del framework.
