// I test nel browser: Chromium apre l'app compilata e la usa come una persona, trovando gli
// elementi per ruolo ed etichetta.
import { expect, test } from '@playwright/test';

test.describe('la pagina dei codici di stato', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('ha il titolo e il campo Codice', async ({ page }) => {
    await expect(page.getByRole('heading', { level: 1, name: 'I codici di stato' })).toBeVisible();
    await expect(page.getByLabel('Codice')).toBeEditable();
  });

  test('chiede un codice quando il campo è vuoto', async ({ page }) => {
    await expect(page.getByRole('status')).toHaveText('Scrivi un codice di tre cifre.');
  });

  test('spiega il codice mentre lo scrivi', async ({ page }) => {
    const field = page.getByLabel('Codice');
    const sentence = page.getByRole('status');

    await field.pressSequentially('20');
    await expect(sentence).toHaveText('20 non è un codice di stato.');
    await field.pressSequentially('1');
    await expect(sentence).toHaveText('201 Created: successo.');
  });

  test('dà la sola classe a un codice senza nome', async ({ page }) => {
    await page.getByLabel('Codice').fill('418');
    await expect(page.getByRole('status')).toHaveText('418: errore del client.');
  });

  test('rifiuta un numero fuori dagli intervalli', async ({ page }) => {
    await page.getByLabel('Codice').fill('600');
    await expect(page.getByRole('status')).toHaveText('600 non è un codice di stato.');
  });
});
