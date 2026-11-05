// I test nel browser: Chromium apre la pagina dell'orario, la usa come una persona e la controlla
// anche con axe, per le regole di accessibilità.
import AxeBuilder from '@axe-core/playwright';
import { expect, test } from '@playwright/test';

test.describe("la pagina dell'orario", () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/');
  });

  test('mostra la griglia delle classi senza conflitti', async ({ page }) => {
    await expect(page.getByRole('heading', { level: 1, name: 'Orario' })).toBeVisible();
    const table = page.getByRole('table', { name: 'Orario delle classi' });
    await expect(table.getByRole('columnheader', { name: '3A' })).toBeVisible();
    await expect(table.getByRole('rowheader', { name: 'Lun 2' })).toBeVisible();
    await expect(page.getByRole('status')).toHaveText('Nessun conflitto.');
  });

  test('ricalcola la griglia senza ricaricare la pagina', async ({ page }) => {
    // Un segno nella finestra: se la pagina si ricaricasse, sparirebbe.
    await page.evaluate(() => Object.assign(window, { learnMarker: true }));
    await page.getByRole('button', { name: 'Ricalcola' }).click();
    await expect(page.getByRole('status')).toHaveText('Nessun conflitto.');
    expect(await page.evaluate(() => 'learnMarker' in window)).toBe(true);
  });

  test('rispetta le regole di accessibilità', async ({ page }) => {
    const results = await new AxeBuilder({ page }).analyze();
    expect(results.violations).toEqual([]);
  });
});
