import Capacitor
import Foundation
import EventKit
import EventKitUI

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */


@objc(CalendarioPlugin)
public class CalendarioPlugin: CAPPlugin, EKEventEditViewDelegate {
    // private let implementation = Calendario()
    private var currentCall: CAPPluginCall?

    @objc func crearEvento(_ call: CAPPluginCall) {
        self.currentCall = call
        
        DispatchQueue.main.async {
            let titulo = call.getString("titulo") ?? ""
            let descripcion = call.getString("descripcion") ?? ""
            let unixInicio = call.getDouble("unixInicio") ?? 0
            let unixFin = call.getDouble("unixFin") ?? 0
            let lugar = call.getString("lugar") ?? ""
            let direccion = call.getString("direccion") ?? ""
            let timezone = call.getString("timezone") ?? ""

            let eventStore = EKEventStore()

            let evento = EKEvent(eventStore: eventStore)
            evento.title = titulo
            evento.startDate = Date(timeIntervalSince1970: unixInicio)
            evento.endDate = Date(timeIntervalSince1970: unixFin)
            if (timezone.count > 0) {
                evento.timeZone = TimeZone(identifier: timezone)
            }
            evento.notes = descripcion
            evento.location = lugar + " - " + direccion
            evento.calendar = eventStore.defaultCalendarForNewEvents

            // Create a view controller
            let eventEditViewController = EKEventEditViewController()
            eventEditViewController.event = evento
            eventEditViewController.eventStore = eventStore
            eventEditViewController.editViewDelegate = self

            // Present the view controller on the main thread
            
            print("self.bridge", self.bridge ?? "IDK")
            
            self.setCenteredPopover(eventEditViewController)
            self.bridge?.viewController!.present(eventEditViewController, animated: true, completion: nil)
        }
    }


    public func eventEditViewController(_ controller: EKEventEditViewController, didCompleteWith action: EKEventEditViewAction) {
        // Dismiss the view controller on the main thread
        DispatchQueue.main.async {
            controller.dismiss(animated: true, completion: nil)
        }
        
        // Manejar el resultado (por ejemplo, si el usuario creó o canceló el evento)
        if action == .saved {
            // Evento guardado con éxito
            self.currentCall?.resolve(["resultado": "creado"])
        } else if action == .canceled {
            // Usuario canceló la edición del evento
            self.currentCall?.resolve(["resultado": "cancelado"])
        }
    }
}
