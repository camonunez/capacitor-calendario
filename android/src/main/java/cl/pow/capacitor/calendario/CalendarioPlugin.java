package cl.pow.capacitor.calendario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
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

    private static final String TAG = "CalendarioPlugin";

    @PluginMethod
    public void crearEvento(PluginCall call) {
        // Requeridos
        String eventoID = call.getString("eventoID");
        String titulo = call.getString("titulo");
		Long mseInicio = call.getLong("mseInicio");
		Long mseFin = call.getLong("mseFin");
        String timezone = call.getString("timezone", "");

        // Opcionales
        String lugar = call.getString("lugar", null);
        String direccion = call.getString("direccion", null);
        String descripcion = call.getString("descripcion", null);
        String organizadorNombre = call.getString("organizadorNombre", null);
        String organizadorEmail = call.getString("organizadorEmail", null);
        String url = call.getString("url", null);

        // Registro de datos recibidos
        Log.d(TAG, "Crear evento:");
        Log.d(TAG, "  eventoID: " + eventoID);
        Log.d(TAG, "  titulo: " + titulo);
        Log.d(TAG, "  mseInicio: " + mseInicio);
        Log.d(TAG, "  mseFin: " + mseFin);
        Log.d(TAG, "  timezone: " + timezone);
        Log.d(TAG, "  lugar: " + lugar);
        Log.d(TAG, "  direccion: " + direccion);
        Log.d(TAG, "  descripcion: " + descripcion);
        Log.d(TAG, "  organizadorNombre: " + organizadorNombre);
        Log.d(TAG, "  organizadorEmail: " + organizadorEmail);
        Log.d(TAG, "  url: " + url);

        if (titulo == null || titulo.isEmpty() || mseInicio == null || mseFin == null) {
            call.reject("Parámetros obligatorios faltantes.");
            return;
        }

        if (mseFin <= mseInicio) {
            call.reject("La hora de finalización debe ser posterior a la hora de inicio.");
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mseInicio)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mseFin)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                .putExtra(CalendarContract.Events.TITLE, titulo)
                .putExtra(CalendarContract.Events.DESCRIPTION, descripcion);

            String location = lugar;
            if (direccion != null && !direccion.isEmpty()) {
                location += (lugar != null && !lugar.isEmpty() ? " - " : "") + direccion;
            }
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);

            if (organizadorEmail != null) {
                intent.putExtra(CalendarContract.Events.ORGANIZER, organizadorEmail);
            }

            startActivityForResult(call, intent, "resultadoEventoEnCalendario");
        } catch (Exception e) {
            call.reject("Error al crear el evento: " + e.getMessage());
        }
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