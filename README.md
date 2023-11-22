# capacitor-calendario

Plugin para agregar eventos al calendario del sistema.

## Install

```bash
npm install capacitor-calendario
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`crearEvento(...)`](#crearevento)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### crearEvento(...)

```typescript
crearEvento(evento: Evento) => Promise<{ value: boolean; }>
```

| Param        | Type                                      |
| ------------ | ----------------------------------------- |
| **`evento`** | <code><a href="#evento">Evento</a></code> |

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### Interfaces


#### Evento

| Prop              | Type                |
| ----------------- | ------------------- |
| **`titulo`**      | <code>string</code> |
| **`unixInicio`**  | <code>number</code> |
| **`unixFin`**     | <code>number</code> |
| **`ubicacion`**   | <code>string</code> |
| **`descripcion`** | <code>string</code> |

</docgen-api>
