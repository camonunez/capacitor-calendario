import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(CalendarioPlugin)
public class CalendarioPlugin: CAPPlugin {
    private let implementation = Calendario()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }

    @objc func crearEvento(_ call: CAPPluginCall) {
        let titulo = call.getString("titulo") ?? ""
        let descripcion = call.getString("descripcion") ?? ""
        let unixInicio = call.getDouble("unixInicio") ?? 0
        let unixFin = call.getDouble("unixFin") ?? 0
        let ubicacion = call.getString("ubicacion") ?? ""

        let resultado = implementation.crearEvento(titulo: titulo, descripcion: descripcion, unixInicio: unixInicio, unixFin: unixFin, ubicacion: ubicacion)
        
        call.resolve([
            "resultado": resultado
        ])

    }
}
