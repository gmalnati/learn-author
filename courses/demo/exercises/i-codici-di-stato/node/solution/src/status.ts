// I nomi dei codici più comuni: gli altri codici hanno solo la classe.
export const REASONS: Readonly<Record<number, string>> = {
  200: 'OK',
  201: 'Created',
  204: 'No Content',
  301: 'Moved Permanently',
  304: 'Not Modified',
  400: 'Bad Request',
  401: 'Unauthorized',
  403: 'Forbidden',
  404: 'Not Found',
  500: 'Internal Server Error',
  503: 'Service Unavailable',
};

// Le classi dei codici, nell'ordine della loro prima cifra (da 1 a 5).
const CLASSES = [
  'informativo',
  'successo',
  'redirezione',
  'errore del client',
  'errore del server',
];

// La frase che spiega il testo del campo Codice.
export function describeStatus(text: string): string {
  const code = text.trim();
  if (code === '') {
    return 'Scrivi un codice di tre cifre.';
  }
  // Tre cifre, da 100 a 599.
  if (!/^[1-5][0-9]{2}$/.test(code)) {
    return `${code} non è un codice di stato.`;
  }
  const status = Number(code);
  const kind = CLASSES[Math.floor(status / 100) - 1];
  const reason = REASONS[status];
  return reason ? `${status} ${reason}: ${kind}.` : `${status}: ${kind}.`;
}
