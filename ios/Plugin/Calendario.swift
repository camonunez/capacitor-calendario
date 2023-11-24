import Foundation
import EventKit
import EventKitUI

@objc public class Calendario: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }

    @objc public func crearEvento(titulo: String, descripcion: String, unixInicio: Double, unixFin: Double, ubicacion: String) -> EKEvent {
        print("titulo", titulo)
        print("descripcion", descripcion)
        print("unixInicio", unixInicio)
        print("unixFin", unixFin)
        print("ubicacion", ubicacion)

        // Usar EventKit para crear el evento
        // https://developer.apple.com/documentation/eventkit/ekeventstore


        var resultado = false
        
        let eventStore = EKEventStore()
        let evento = EKEvent(eventStore: eventStore)
        evento.title = titulo
        evento.startDate = Date(timeIntervalSince1970: unixInicio)
        evento.endDate = Date(timeIntervalSince1970: unixFin)
        evento.notes = descripcion
        evento.location = ubicacion
        evento.calendar = eventStore.defaultCalendarForNewEvents
        
        

        Task {
            do {
                // Intenta solicitar acceso a los eventos del calendario
                let accessGranted = try await eventStore.requestAccess(to: .event)

                if accessGranted {
                    print("Acceso concedido a eventos del calendario.")
                    // Puedes realizar operaciones relacionadas con eventos aquí
                    
                    try eventStore.save(evento, span: .thisEvent)
                    print("Evento guardado", evento, "\n")
                    
                    // Crea una URL para abrir la aplicación de Calendario con el evento específico
                    if evento.eventIdentifier != nil,
                       let calendarURL = URL(string: "calshow:\(evento.startDate.timeIntervalSinceReferenceDate)") {
                        
                        // Abre la aplicación de Calendario
                        await UIApplication.shared.open(calendarURL, options: [:], completionHandler: nil)
                    }
                } else {
                    print("Acceso denegado a eventos del calendario.")
                }
            } catch {
                // Manejar cualquier error que pueda ocurrir durante la solicitud de acceso
                print("Error al solicitar acceso: \(error)")
            }
        }

        return evento
    }
}
