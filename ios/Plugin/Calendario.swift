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

        let eventStore = EKEventStore()

        var resultado = false
        eventStore.requestAccess(to: .event) { (granted, error) in
            print("granted \(granted)")
            print("error \(String(describing: error))")
            if (!granted) {
                // call.reject([error: "sinPermiso"])
                print("sinPermiso")
                resultado = false
            } else if (error != nil) {
                // call.reject([error: "error"])
                print("error")
                resultado = false
            } else {
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
                    // call.resolve([
                    //     "resultado": true
                    // ])
                    resultado = true
                } catch let error as NSError {
                    print("guardadoFallido error : \(error)")
                    // call.reject([error: "guardadoFallido"])
                    resultado = false
                }
            }
        }
        return resultado
    }
}
