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

// La frase che spiega il testo del campo Codice (vedi il README).
export function describeStatus(text: string): string {
  const code = text.trim();
  if (code === '') {
    return 'Scrivi un codice di tre cifre.';
  }
  // Da fare: il nome e la classe di un codice di stato, o la frase per ciò che non lo è.
  return code;
}
