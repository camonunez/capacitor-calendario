import Foundation
import EventKit

@objc public class Calendario: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func crearEvento(titulo: String, descripcion: String, unixInicio: Double, unixFin: Double, ubicacion: String) -> Bool {
        print(titulo)
        print(descripcion)
        print(unixInicio)
        print(unixFin)
        print(ubicacion)

        // Usar EventKit para crear el evento
        // https://developer.apple.com/documentation/eventkit/ekeventstore

        var eventStore = EKEventStore()

        eventStore.requestAccess(to: .event) { (granted, error) in
            print("granted \(granted)")
            print("error \(error)")
            if (!granted) {
                call.reject([error: "sinPermiso"])
            }
            else if (error != nil) {
                call.reject([error: "error"])
            }
            else {
                let event = EKEvent(eventStore: eventStore)
                event.title = titulo
                event.startDate = Date(timeIntervalSince1970: unixInicio)
                event.endDate = Date(timeIntervalSince1970: unixFin)
                event.notes = descripcion
                event.location = ubicacion
                event.calendar = eventStore.defaultCalendarForNewEvents

                do {
                    try eventStore.save(event, span: .thisEvent)
                    print("Evento guardado", event)
                    call.resolve([
                        "resultado": true
                    ])
                } catch let error as NSError {
                    print("falló el guardado del evento con el error : \(error)")
                    call.reject([error: "guardadoFallido"])
                }
            }
        }
    }
}
