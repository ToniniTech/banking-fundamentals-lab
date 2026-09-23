# Unidad 1: Java Core

## Objetivos

Al finalizar esta unidad debes poder:

1. Explicar por que los importes monetarios no se representan con `double`.
2. Diferenciar `BigDecimal.equals` de `BigDecimal.compareTo`.
3. Explicar por que `Money` es inmutable y que beneficios aporta.
4. Describir el contrato entre `equals` y `hashCode`.
5. Explicar por que una clave mutable puede romper una busqueda en `HashSet` o `HashMap`.
6. Distinguir identidad de entidad y valor de un value object.
7. Mostrar como `BankAccount` protege sus invariantes sin setters publicos.
8. Explicar por que una operacion fallida no debe modificar parcialmente el estado del objeto.

## Laboratorio 1: construccion de BigDecimal

Archivo: `BigDecimalConstructionLabTest`

Antes de ejecutarlo, predice:

- El valor exacto de `new BigDecimal(0.1)`.
- Si sera igual a `new BigDecimal("0.1")`.
- Por que `BigDecimal.valueOf(0.1)` se comporta de otra manera.

## Laboratorio 2: equals frente a compareTo

Archivo: `BigDecimalEqualityLabTest`

Antes de ejecutarlo, predice:

- Si `new BigDecimal("10.0")` y `new BigDecimal("10.00")` son iguales con `equals`.
- Que devuelve `compareTo` para esos mismos valores.
- Que implicacion tendria guardar ambos en un `HashSet`.

## Laboratorio 3: clave mutable

Archivo: `MutableHashSetKeyLabTest`

Antes de ejecutarlo, predice:

- Que sucede si cambia un campo utilizado por `hashCode` despues de insertar el objeto.
- Si el conjunto puede seguir conteniendo fisicamente el objeto aunque `contains` responda `false`.
- Por que el identificador de `BankAccount` es inmutable.

## Laboratorio 4: cuenta sin invariantes

Archivo: `UnprotectedAccountInvariantLabTest`

Antes de ejecutarlo, predice:

- Que saldo produce un debito superior a los fondos disponibles.
- En que capa deberia protegerse esta regla.
- Por que no basta con validar solamente en un controller.

## Criterio de salida

No avances a Spring hasta poder responder las preguntas anteriores sin leer el codigo y hasta poder escribir una variante pequena de cada ejemplo.

