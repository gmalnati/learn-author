import { describe, expect, it } from 'vitest';
import { describeStatus } from './status.ts';

describe('describeStatus', () => {
  it('chiede un codice quando il campo è vuoto', () => {
    expect(describeStatus('')).toBe('Scrivi un codice di tre cifre.');
    expect(describeStatus('   ')).toBe('Scrivi un codice di tre cifre.');
  });

  it('dà il nome e la classe ai codici di REASONS', () => {
    expect(describeStatus('200')).toBe('200 OK: successo.');
    expect(describeStatus('404')).toBe('404 Not Found: errore del client.');
  });

  it('dà la sola classe agli altri codici', () => {
    expect(describeStatus('101')).toBe('101: informativo.');
    expect(describeStatus('418')).toBe('418: errore del client.');
    expect(describeStatus('599')).toBe('599: errore del server.');
  });

  it('toglie gli spazi intorno al codice', () => {
    expect(describeStatus(' 301 ')).toBe('301 Moved Permanently: redirezione.');
  });

  it('rifiuta ciò che non è un codice di stato', () => {
    for (const text of ['abc', '99', '600', '1000', '4o4']) {
      expect(describeStatus(text)).toBe(`${text} non è un codice di stato.`);
    }
  });
});
