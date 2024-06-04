import { registerPlugin } from '@capacitor/core';

import type { CalendarioPlugin } from './definitions';

const Calendario = registerPlugin<CalendarioPlugin>('Calendario', {
	web: () => import('./web').then(m => new m.CalendarioWeb()),
});

export * from './definitions';
export { Calendario };
