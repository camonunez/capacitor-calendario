package cl.pow.capacitor.calendario;

import android.content.Intent;
import android.provider.CalendarContract;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "Calendario")
public class CalendarioPlugin extends Plugin {

    private Calendario implementation = new Calendario();

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void crearEvento(PluginCall call) {
        // Extraer las variables 
        // titulo: string, descripcion?: string, ubicacion?: string, todoElDia: boolean, unixInicio: number, unixFin: number
        String titulo = call.getString("titulo");
        String descripcion = call.getString("descripcion");
        String ubicacion = call.getString("ubicacion");
        Boolean todoElDia = call.getBoolean("todoElDia");
        Long unixInicio = call.getLong("unixInicio");
        Long unixFin = call.getLong("unixFin");

        // Utilizar android Intent para insertar el evento en el calendario
        Intent intent = new Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, titulo)
            .putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacion)
            .putExtra(CalendarContract.Events.ALL_DAY, todoElDia);

        if (unixInicio != null) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, unixInicio);
        }
        if (unixFin != null) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, unixFin);
        }

        Boolean resultado = false;
        if (getActivity() != null) {
            getActivity().startActivity(intent);
            resultado = true;
        }

        // Retornar un JSObject con el resultado
        JSObject ret = new JSObject();
        ret.put("resultado", resultado);
        call.resolve(ret);
    }
}
