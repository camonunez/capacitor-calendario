package cl.pow.capacitor.calendario;

import android.Intent;
import android.util.Log;

public class Calendario {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }


    // implementation.crearEvento(titulo, descripcion, ubicacion, todoElDia, unixInicio, unixFin)
    public String crearEvento(String titulo, String descripcion, String ubicacion, Boolean todoElDia, Long unixInicio, Long unixFin) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, titulo)
            .putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacion)
            .putExtra(CalendarContract.Events.ALL_DAY, todoElDia)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, unixInicio)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, unixFin);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return true;
        }
        return false;
    }
}
