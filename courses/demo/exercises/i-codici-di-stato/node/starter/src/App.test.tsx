import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, expect, it } from 'vitest';
import { App } from './App.tsx';

describe('App', () => {
  it('ha il titolo della pagina', () => {
    render(<App />);
    expect(screen.getByRole('heading', { level: 1 })).toHaveTextContent('I codici di stato');
  });

  it('spiega il codice scritto nel campo', async () => {
    const user = userEvent.setup();
    render(<App />);
    expect(screen.getByRole('status')).toHaveTextContent('Scrivi un codice di tre cifre.');

    await user.type(screen.getByLabelText('Codice'), '201');
    expect(screen.getByRole('status')).toHaveTextContent('201 Created: successo.');
  });
});
