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
        // Obtener el objeto con todas las propiedades del evento
        JSObject data = call.getObject("data");

        // Validar que los parámetros esenciales estén presentes
        if (!data.has("mseInicio") || !data.has("mseFin")) {
            call.reject("Faltan parámetros esenciales como 'mseInicio' o 'mseFin'.");
            return;
        }

        // Crear la intención para insertar un evento en el calendario
        Intent intent = new Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI);

        // Llenar la intención con las propiedades que el usuario proporciona
        ContentValues values = new ContentValues();

        // Asignar los campos del evento de CalendarContract
        if (data.has("titulo")) {
            values.put(CalendarContract.Events.TITLE, data.getString("titulo"));
        }
        if (data.has("descripcion")) {
            values.put(CalendarContract.Events.DESCRIPTION, data.getString("descripcion"));
        }
        if (data.has("lugar")) {
            values.put(CalendarContract.Events.EVENT_LOCATION, data.getString("lugar"));
        }
        if (data.has("direccion")) {
            // Concatenar lugar y direccion si ambos existen
            String lugarDireccion = data.has("lugar") 
                ? data.getString("lugar") + " - " + data.getString("direccion")
                : data.getString("direccion");
            values.put(CalendarContract.Events.EVENT_LOCATION, lugarDireccion);
        }
        if (data.has("timezone")) {
            values.put(CalendarContract.Events.EVENT_TIMEZONE, data.getString("timezone"));
        }
        if (data.has("mseInicio")) {
            values.put(CalendarContract.Events.DTSTART, data.getInt("mseInicio"));
        }
        if (data.has("mseFin")) {
            values.put(CalendarContract.Events.DTEND, data.getInt("mseFin"));
        }
        if (data.has("organizadorEmail")) {
            values.put(CalendarContract.Events.ORGANIZER, data.getString("organizadorEmail"));
        }
        if (data.has("url")) {
            values.put(CalendarContract.Events.EVENT_URL, data.getString("url"));
        }

        // Agregar cualquier otro campo desconocido para CalendarContract.Events
        // Esto permite que el plugin sea extensible si Android soporta más campos en el futuro

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
