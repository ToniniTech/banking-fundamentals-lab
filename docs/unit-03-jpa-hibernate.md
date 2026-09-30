# Unidad 3: JPA, Hibernate y PostgreSQL

## Pregunta central

Que ocurre entre llamar a un repositorio Java y encontrar una fila durable en PostgreSQL?

## Flujo que debes poder explicar

```text
BankAccount (dominio)
        |
JpaAccountRepositoryAdapter
        |
AccountJpaEntity (persistencia)
        |
Spring Data repository
        |
EntityManager / persistence context
        |
Hibernate genera SQL
        |
PostgreSQL valida y almacena la fila
```

## Decisiones intencionales

- `BankAccount` sigue libre de `@Entity`, `@Id` y constructores para frameworks.
- `AccountJpaEntity` representa la forma persistida, no el modelo de negocio.
- Flyway crea el esquema; Hibernate solamente lo valida con `ddl-auto=validate`.
- PostgreSQL es la base de datos de aprendizaje y de las pruebas; no se usa H2.
- El perfil `in-memory` conserva los ejercicios de la Unidad 2 sin iniciar JPA.
- Los servicios aun no usan `@Transactional`; esa frontera se estudiara en la Unidad 4.

## Orden de estudio

1. Compara `BankAccount` con `AccountJpaEntity` campo por campo.
2. Sigue `JpaAccountRepositoryAdapter.save` hasta `JpaRepository.save`.
3. Lee la migracion `V1__create_bank_account.sql` y relaciona cada restriccion con el mapeo.
4. Ejecuta primero `AccountJpaEntityMappingTest`, que no necesita Spring ni Docker.
5. Inicia Docker y ejecuta `PostgresAccountRepositoryTest`.
6. Realiza los cuatro experimentos de `PersistenceContextLabTest` por separado.
7. Activa el log SQL temporalmente y relaciona cada `flush` con el SQL observado.

## Laboratorio 1: dos identidades distintas

Antes de ejecutar `AccountJpaEntityMappingTest`, predice:

- Por que `BankAccount` y `AccountJpaEntity` no son la misma clase.
- Que identidad pertenece al negocio y cual pertenece al persistence context.
- Que se perderia si el dominio dependiera directamente de JPA.

## Laboratorio 2: identity map

Antes de ejecutar `twoFindsInsideOnePersistenceContextReturnTheSameJavaReference`, predice:

- Si dos consultas por el mismo `@Id` retornan referencias Java iguales con `==`.
- Cuantas sentencias `SELECT` necesita Hibernate.
- Que cambia despues de llamar a `clear()`.

## Laboratorio 3: dirty checking

Antes de ejecutar `flushPersistsChangesMadeToAManagedEntityWithoutCallingSaveAgain`, predice:

- Por que no se llama a `save` despues de cambiar el saldo.
- En que momento Hibernate detecta la diferencia.
- Si `flush` significa que la transaccion ya hizo commit.

## Laboratorio 4: estado detached

Antes de ejecutar `changingADetachedEntityIsNotDetected`, predice:

- Que responsabilidad deja de tener Hibernate despues de `detach`.
- Por que modificar el objeto sigue siendo Java valido pero no genera `UPDATE`.
- Que alternativas existen para volver a persistir el cambio.

## Laboratorio 5: restricciones y flush

Antes de ejecutar `aDatabaseConstraintCanRemainInvisibleUntilFlush`, predice:

- Por que `persist` puede no ejecutar inmediatamente el `INSERT`.
- Por que el error aparece al llamar a `flush`.
- Que regla protege el dominio y que regla protege la base de datos.

## Tabla de estados JPA

| Estado | Conocido por el persistence context | Dirty checking | Operacion tipica |
| --- | --- | --- | --- |
| Transient | No | No | `new` |
| Managed | Si | Si | `persist` o `find` |
| Detached | Ya no | No | `detach` o `clear` |
| Removed | Si, pendiente de borrado | No aplica | `remove` |

## Fallos dirigidos posteriores

Cuando puedas explicar el recorrido correcto:

1. Cambia temporalmente un nombre de columna en la entidad y observa `ddl-auto=validate`.
2. Elimina `entityManager.clear()` y observa como la cache de primer nivel puede ocultar la lectura real.
3. Quita una restriccion de la migracion y decide que proteccion se perdio.
4. Cambia `EnumType.STRING` por `ORDINAL` y explica el riesgo de reordenar el enum.
5. Intenta iniciar dos implementaciones de `AccountRepository` bajo el mismo perfil.

Realiza cada fallo de forma aislada y reviertelo despues de explicar el resultado.

## Criterio de salida

Debes poder explicar sin abrir el codigo:

1. Diferencia entre JPA, Hibernate y Spring Data JPA.
2. Para que existe `EntityManager`.
3. Que contiene un persistence context.
4. Estados transient, managed, detached y removed.
5. Por que dos `find` pueden retornar la misma referencia.
6. Como funciona dirty checking.
7. Diferencia entre `flush` y commit.
8. Diferencia entre `persist` y `merge`.
9. Por que Flyway y `ddl-auto=validate` cumplen responsabilidades distintas.
10. Por que el modelo de dominio no necesita depender de anotaciones JPA.
