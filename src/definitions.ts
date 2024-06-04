export type Resultado = 'creado' | 'cancelado' | 'error';
export interface CalendarioPlugin {
	crearEvento(evento: Evento): Promise<{ resultado: Resultado }>;
}

export interface Evento {
	eventoID: string;
	titulo: string;
	unixInicio: number;
	unixFin: number;
	timezone: string;

	// Opcionales
	organizadorNombre?: string;
	organizadorEmail?: string;
	lugar?: string;
	direccion?: string;
	descripcion?: string;
	url?: string;
}
