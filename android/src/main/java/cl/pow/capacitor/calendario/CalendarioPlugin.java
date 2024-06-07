package cl.pow.capacitor.calendario;

import android.app.Activity;
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
        // Requeridos
        String eventoID = call.getString("eventoID");
        String titulo = call.getString("titulo");
        Integer mseInicio = call.getInt("mseInicio");
        Integer mseFin = call.getInt("mseFin");
        String timezone = call.getString("timezone", "");

        // Opcionales
        String lugar = call.getString("lugar", null);
        String direccion = call.getString("direccion", null);
        String descripcion = call.getString("descripcion", null);
        String organizadorNombre = call.getString("organizadorNombre", null);
        String organizadorEmail = call.getString("organizadorEmail", null);
        String url = call.getString("url", null);

        Intent intent = new Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, titulo)
            .putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, lugar + (direccion != null ? " - " + direccion : ""))
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, (mseInicio.longValue()))
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (mseFin.longValue()))
            .putExtra(CalendarContract.Events.EVENT_TIMEZONE, timezone);

        // Add optional fields if they exist
        if (organizadorEmail != null) {
            intent.putExtra(CalendarContract.Events.ORGANIZER, organizadorEmail);
        }
        startActivityForResult(call, intent, "resultadoEventoEnCalendario");
    }

    @ActivityCallback
    private void resultadoEventoEnCalendario(PluginCall call, ActivityResult result) {
        JSObject ret = new JSObject();
        getActivity();
        if (result.getResultCode() == Activity.RESULT_CANCELED) {
            ret.put("resultado", "cancelado");
        } else if (result.getResultCode() == Activity.RESULT_OK) {
            ret.put("resultado", "creado");
        }
        call.resolve(ret);
    }
}
