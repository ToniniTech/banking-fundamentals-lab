# Mapa de aprendizaje

El proyecto crece de forma deliberadamente gradual:

```text
Unidad 1: Java puro (actual)
    Money, BankAccount, BigDecimal, inmutabilidad, igualdad y excepciones
        |
Unidad 2: Spring basico
    Application Context, beans, inyeccion, HTTP, DTO y validacion
        |
Unidad 3: JPA e Hibernate
        |
Unidad 4: Transacciones
        |
Unidad 5+: concurrencia, idempotencia, ledger y outbox
```

Cada concepto se trabaja mediante el mismo ciclo:

```text
Predecir -> ejecutar -> observar -> explicar -> modificar -> reparar
```

## Limite de la Unidad 1

No se agregan dependencias o anotaciones de frameworks. Los tests ejecutan el dominio como clases Java ordinarias. Esta restriccion permite atribuir cada comportamiento al lenguaje y no a Spring, Hibernate o una base de datos.
