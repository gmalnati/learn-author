import { useId, useState } from 'react';
import { describeStatus } from './status.ts';

// La pagina: il campo Codice e, sotto, la frase che lo spiega, aggiornata a ogni tasto.
export function App() {
  const [code, setCode] = useState('');
  const fieldId = useId();

  return (
    <main>
      <h1>I codici di stato</h1>
      <label htmlFor={fieldId}>Codice</label>
      <input
        id={fieldId}
        inputMode="numeric"
        autoComplete="off"
        value={code}
        onChange={(event) => setCode(event.target.value)}
      />
      <p role="status">{describeStatus(code)}</p>
    </main>
  );
}
