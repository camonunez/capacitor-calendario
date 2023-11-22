package cl.pow.capacitor.calendario;

import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
public class Calendario {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

//
//    public Boolean CrearEvento(Activity actividad, String titulo, String descripcion, String ubicacion, Boolean todoElDia, Long unixInicio, Long unixFin) {
////        Intent intent = new Intent(Intent.ACTION_INSERT)
////            .setData(CalendarContract.Events.CONTENT_URI)
////            .putExtra(CalendarContract.Events.TITLE, titulo)
////            .putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
////            .putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacion)
////            .putExtra(CalendarContract.Events.ALL_DAY, todoElDia)
////            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, unixInicio)
////            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, unixFin);
//
////        if (getActivity() != null) {
////            getActivity().startActivity(intent);
////            resultado = true;
////        }
//        return false;
//    }
}
