package cl.pow.capacitor.calendario;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

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
	permissions = {
		@Permission(
			strings = { "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR" },
			alias = "calendar"
		)
	}
)
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

		Log.d("crearEvento", "startActivityForResult");
		startActivityForResult(call, intent, "resultadoEventoEnCalendario");
	}

	// Método para manejar el resultado de la actividad (por ejemplo, cuando el usuario cierra la actividad de calendario)
	@ActivityCallback
	private void resultadoEventoEnCalendario(PluginCall call, ActivityResult result) {
		// Mostrar data en consola
		System.out.println("resultadoEventoEnCalendario result.toString()" + result.toString());

		// Mostrar intent en consola
		Log.d("cE result.toString()", result.toString());
		Log.d("cE r.getResultCode()", String.valueOf(result.getResultCode()));

		JSObject ret = new JSObject();

		// Si el resultCode es igual a Activity.RESULT_CANCELED, entonces el usuario canceló la creación del evento
		if (result.getResultCode() == Activity.RESULT_CANCELED) {
			Toast.makeText(getContext(), "Usuario canceló la creación del evento", Toast.LENGTH_SHORT).show();

			// Puedes devolver información adicional a JavaScript si es necesario
			ret.put("resultado", "cancelado");
			notifyListeners("creacionEventoCancelada", ret); // Puedes usar cualquier evento que hayas definido en tu JavaScript
		}
		
		// Si el resultCode es igual a Activity.RESULT_OK, entonces el usuario creó el evento
		if (result.getResultCode() == Activity.RESULT_OK) {
			Toast.makeText(getContext(), "Evento creado con éxito", Toast.LENGTH_SHORT).show();

			// Puedes devolver información adicional a JavaScript si es necesario
			ret.put("resultado", "creado");
			notifyListeners("eventoCreado", ret); // Puedes usar cualquier evento que hayas definido en tu JavaScript
		}

		call.resolve(ret);
	}
}
