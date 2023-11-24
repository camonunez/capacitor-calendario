import { WebPlugin } from '@capacitor/core';

import type { CalendarioPlugin, Evento, Resultado } from './definitions';

function crearICS(evento: Evento): string {
  // Formato de fecha y hora para el iCalendar
  function formatearFechaHora(fecha: Date) {
    return fecha.toISOString().replace(/[:-]/g, '').split('.')[0] + 'Z';
  }

  const DTSTAMP = `TZID=${evento.timezone}:${formatearFechaHora(new Date())}`
  const DTSTART = `TZID=${evento.timezone}:${formatearFechaHora(new Date(evento.unixInicio))}`
  const DTEND = `TZID=${evento.timezone}:${formatearFechaHora(new Date(evento.unixFin))}`

  // Encabezado del archivo ICS
  let icsData = `BEGIN:VCALENDAR
VERSION:2.0
CALSCALE:GREGORIAN
BEGIN:VEVENT`;

  // Detalles del evento
  icsData += `
SUMMARY:${evento.titulo}
DESCRIPTION:${evento.descripcion}
LOCATION:${evento.ubicacion}
DTSTAMP:${DTSTAMP}
DTSTART:${DTSTART}
DTEND:${DTEND}
`;

  // Si hay información sobre la zona horaria, agrégala
  if (evento.timezone) {
    icsData += `TZID=${evento.timezone}\n`;
  }

  // Fin del evento y del archivo ICS
  icsData += `END:VEVENT
END:VCALENDAR`;

  return icsData;
}


export class CalendarioWeb extends WebPlugin implements CalendarioPlugin {
  async crearEvento(evento: Evento): Promise<{ resultado: Resultado }> {
    console.log('Calendario.Web.crearEvento', evento);

    const ics = crearICS(evento)
    const blob = new Blob([ics], { type: 'text/calendar;charset=utf-8' })

    const a = document.createElement('a')
    a.href = URL.createObjectURL(blob)
    a.download = 'event.ics'
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)

    return { resultado: 'creado' }
  }
}
