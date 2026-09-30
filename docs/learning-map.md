# Mapa de aprendizaje

El proyecto sigue un crecimiento deliberadamente gradual:

```text
Unidad 1: Java puro
    Money, BankAccount, BigDecimal, inmutabilidad, igualdad y excepciones
        |
Unidad 2: Spring basico
    Application Context, beans, inyeccion, HTTP, DTO y validacion
        |
Unidad 3: JPA e Hibernate
    entidades, persistence context, dirty checking y flush
        |
Unidad 4: Transacciones
    proxy, commit, rollback y self-invocation
        |
Evaluacion antes de continuar
        |
Unidad 5+: concurrencia, idempotencia, ledger y outbox
```

Cada concepto se trabaja mediante el mismo ciclo:

```text
Predecir -> ejecutar -> observar -> explicar -> modificar -> reparar
```

## Limite de la Unidad 1

No se agregan dependencias o anotaciones de frameworks. Los tests deben ejecutar el dominio como clases Java ordinarias. Esta restriccion permite atribuir cada comportamiento al lenguaje y no a Spring, Hibernate o una base de datos.

## Limite de la Unidad 2

Spring conecta la aplicacion, pero la persistencia sigue siendo un mapa en memoria. No se usan `@Entity`, Spring Data, Hibernate, PostgreSQL ni `@Transactional`. Esto permite estudiar el contenedor y la capa HTTP sin mezclar sus errores con persistencia.

## Limite de la Unidad 3

Se agrega persistencia real con PostgreSQL, pero los servicios de aplicacion todavia
no declaran limites transaccionales. Los tests JPA usan la transaccion provista por
`@DataJpaTest` solamente como instrumento para observar el persistence context.
Propagacion, rollback, aislamiento, proxies y self-invocation pertenecen a la Unidad 4.

