import Capacitor
import Foundation
import EventKit
import EventKitUI

@objc(CalendarioPlugin)
public class CalendarioPlugin: CAPPlugin, EKEventEditViewDelegate {
    private var currentCall: CAPPluginCall?

    @objc func crearEvento(_ call: CAPPluginCall) {
        self.currentCall = call

        DispatchQueue.main.async {
            // Obtener los parámetros
            let eventoID = call.getString("eventoID") ?? ""
            let titulo = call.getString("titulo") ?? ""
            let unixInicio = call.getDouble("unixInicio") ?? 0
            let unixFin = call.getDouble("unixFin") ?? 0

            let descripcion = call.getString("descripcion")
            let lugar = call.getString("lugar")
            let direccion = call.getString("direccion")
            let timezone = call.getString("timezone")
            let urlString = call.getString("url")

            let eventStore = EKEventStore()
            let evento = EKEvent(eventStore: eventStore)
            evento.calendar = eventStore.defaultCalendarForNewEvents

            // Requeridos
            evento.title = titulo
            evento.startDate = Date(timeIntervalSince1970: unixInicio)
            evento.endDate = Date(timeIntervalSince1970: unixFin)
            evento.timeZone = TimeZone(identifier: timezone ?? "")

            // Opcionales
            if let descripcion = descripcion {
                evento.notes = descripcion
            }

            if let urlString = urlString, let url = URL(string: urlString) {
                evento.url = url
            }

            if let lugar = lugar, let direccion = direccion {
                evento.location = direccion
                evento.structuredLocation = EKStructuredLocation(title: lugar)
            }

            // Preparar un controlador de vista
            let eventEditViewController = EKEventEditViewController()
            eventEditViewController.event = evento
            eventEditViewController.eventStore = eventStore
            eventEditViewController.editViewDelegate = self

            // Presentar el controlador de vista
            self.setCenteredPopover(eventEditViewController)
            self.bridge?.viewController!.present(eventEditViewController, animated: true, completion: nil)
        }
    }

    public func eventEditViewController(_ controller: EKEventEditViewController, didCompleteWith action: EKEventEditViewAction) {
        // El controlador de vista deja el hilo principal
        DispatchQueue.main.async {
            controller.dismiss(animated: true, completion: nil)
        }

        // Manejar el resultado (el usuario creó o canceló el evento?)
        if action == .saved {
            self.currentCall?.resolve(["resultado": "creado"])
        } else if action == .canceled {
            self.currentCall?.resolve(["resultado": "cancelado"])
        }
    }
}
