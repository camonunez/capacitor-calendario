export interface CalendarioPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
