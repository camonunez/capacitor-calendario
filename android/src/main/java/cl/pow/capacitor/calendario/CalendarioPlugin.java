package cl.pow.capacitor.calendario;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.provider.CalendarContract;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

@CapacitorPlugin(
    name = "Calendario",
    permissions = { @Permission(strings = { "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR" }, alias = "calendar") }
)
public class CalendarioPlugin extends Plugin {

    @PluginMethod
    public void crearEvento(PluginCall call) {
        // Obtener el objeto completo con todas las propiedades del evento
        JSObject data = call.getObject("data");

        // Validar que los datos esenciales estén presentes
        if (!data.has("mseInicio") || !data.has("mseFin")) {
            call.reject("Faltan parámetros esenciales como 'mseInicio' o 'mseFin'.");
            return;
        }

        // Crear la intención para insertar un evento en el calendario
        Intent intent = new Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI);

        // Llenar la intención con las propiedades que el usuario proporciona
        ContentValues values = new ContentValues();

        // Iterar sobre todas las propiedades del objeto `data`
        for (String key : data.keys()) {
            // Verificar si la clave está presente en CalendarContract.Events
            switch (key) {
                case "titulo":
                    values.put(CalendarContract.Events.TITLE, data.getString(key));
                    break;
                case "descripcion":
                    values.put(CalendarContract.Events.DESCRIPTION, data.getString(key));
                    break;
                case "lugar":
                    values.put(CalendarContract.Events.EVENT_LOCATION, data.getString(key));
                    break;
                case "direccion":
                    // Si hay una dirección, concatenar con el lugar
                    if (data.has("lugar")) {
                        values.put(CalendarContract.Events.EVENT_LOCATION, data.getString("lugar") + " - " + data.getString("direccion"));
                    } else {
                        values.put(CalendarContract.Events.EVENT_LOCATION, data.getString("direccion"));
                    }
                    break;
                case "timezone":
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, data.getString(key));
                    break;
                case "mseInicio":
                    values.put(CalendarContract.Events.DTSTART, data.getInt(key));
                    break;
                case "mseFin":
                    values.put(CalendarContract.Events.DTEND, data.getInt(key));
                    break;
                case "organizadorEmail":
                    values.put(CalendarContract.Events.ORGANIZER, data.getString(key));
                    break;
                case "url":
                    values.put(CalendarContract.Events.EVENT_URL, data.getString(key));
                    break;
                default:
                    // Agregar otras propiedades que CalendarContract pueda soportar en el futuro
                    // Aquí no se debe hacer nada, ya que los datos específicos del CalendarContract se procesan arriba.
                    break;
            }
        }

        // Añadir los valores a la intención
        intent.putExtras(values);

        // Ejecutar la actividad
        startActivityForResult(call, intent, "resultadoEventoEnCalendario");
    }

    @ActivityCallback
    private void resultadoEventoEnCalendario(PluginCall call, ActivityResult result) {
        JSObject ret = new JSObject();
        if (result.getResultCode() == Activity.RESULT_CANCELED) {
            ret.put("resultado", "cancelado");
        } else if (result.getResultCode() == Activity.RESULT_OK) {
            ret.put("resultado", "creado");
        }
        call.resolve(ret);
    }
}
