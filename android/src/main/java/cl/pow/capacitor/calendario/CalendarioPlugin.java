package cl.pow.capacitor.calendario;

import android.app.Activity;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.Calendar;

@CapacitorPlugin(name = "Calendario")
public class CalendarioPlugin extends Plugin {

	@PluginMethod
		public void crearEvento(PluginCall call) {
		String titulo = call.getString("titulo");
		String descripcion = call.getString("descripcion");

		if (!call.getData().has("unixInicio")) {
			call.reject("Se debe proveer unixInicio");
			return;
		}
		if (!call.getData().has("unixFin")) {
			call.reject("Se debe proveer unixFin");
			return;
		}
		Integer unixInicio = call.getInt("unixInicio");
		Integer unixFin = call.getInt("unixFin");

		String ubicacion = call.getString("ubicacion");
		String timezone = call.getString("timezone", "");

		Intent intent = new Intent(Intent.ACTION_INSERT)
			.setData(CalendarContract.Events.CONTENT_URI)
			.putExtra(CalendarContract.Events.TITLE, titulo)
			.putExtra(CalendarContract.Events.DESCRIPTION, descripcion)
			.putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacion)
			.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, (unixInicio.longValue() * 1000))
			.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, (unixFin.longValue() * 1000));

			// Mostrar intent en consola
			Log.d("cE intent.toString()", intent.toString());

		if (!timezone.isEmpty()) {
			intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, timezone);
		}

		startActivityForResult(call, intent, 2);  // Usa cualquier número de código de solicitud que desees
	}

	// Método para manejar el resultado de la actividad (por ejemplo, cuando el usuario cierra la actividad de calendario)
	@Override
	protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
				super.handleOnActivityResult(requestCode, resultCode, data);
				// Mostrar data en consola
				Log.d("hOAR data.toString()", data.toString());

		if (resultCode == Activity.RESULT_OK && requestCode == 2) {  // Cambia esto por el código de solicitud que uses
			Toast.makeText(getContext(), "Evento creado con éxito", Toast.LENGTH_SHORT).show();

			// Puedes devolver información adicional a JavaScript si es necesario
			JSObject ret = new JSObject();
			ret.put("resultado", "creado");
			notifyListeners("eventoCreado", ret);  // Puedes usar cualquier evento que hayas definido en tu JavaScript
		} else if (resultCode == Activity.RESULT_CANCELED && requestCode == 2) {  // Cambia esto por el código de solicitud que uses
			Toast.makeText(getContext(), "Usuario canceló la creación del evento", Toast.LENGTH_SHORT).show();

			// Puedes devolver información adicional a JavaScript si es necesario
			JSObject ret = new JSObject();
			ret.put("resultado", "cancelado");
			notifyListeners("creacionEventoCancelada", ret);  // Puedes usar cualquier evento que hayas definido en tu JavaScript
		}
	}
}
