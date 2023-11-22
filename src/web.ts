import { WebPlugin } from '@capacitor/core';

import type { CalendarioPlugin, Evento } from './definitions';

export class CalendarioWeb extends WebPlugin implements CalendarioPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }


  async crearEvento(evento: Evento): Promise<{ value: boolean }> {
    console.log('Calendario.Web.crearEvento', evento);
    return { value: false };
  }
}
