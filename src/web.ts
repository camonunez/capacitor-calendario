import { WebPlugin } from '@capacitor/core';

import type { CalendarioPlugin } from './definitions';

export class CalendarioWeb extends WebPlugin implements CalendarioPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
