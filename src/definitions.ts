export type Resultado = 'creado' | 'cancelado' | 'error';
export interface CalendarioPlugin {
	crearEvento(evento: Evento): Promise<{ resultado: Resultado }>;
}

export interface Evento {
	eventoID: string;
	titulo: string;

	// mse: milisegundos desde el 1 de enero de 1970
	mseInicio: number;
	mseFin: number;
	timezone: string;

	// Opcionales
	organizadorNombre?: string;
	organizadorEmail?: string;
	lugar?: string;
	direccion?: string;
	descripcion?: string;
	url?: string;
}
