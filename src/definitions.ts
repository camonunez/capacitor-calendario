export type Resultado = 'creado' | 'cancelado' | 'error';
export interface CalendarioPlugin {
  crearEvento(evento: Evento): Promise<{ resultado: Resultado }>;
}

export interface Evento {
  titulo: string;
  unixInicio: number;
  unixFin: number;

  timezone?: string;
  ubicacion?: string;
  descripcion?: string;
}
