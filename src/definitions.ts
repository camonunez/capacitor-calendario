export interface CalendarioPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  crearEvento(evento: Evento): Promise<{ value: boolean }>;
}

export interface Evento {
  titulo: string;
  unixInicio: number;
  unixFin: number;

  ubicacion?: string;
  descripcion?: string;
}
