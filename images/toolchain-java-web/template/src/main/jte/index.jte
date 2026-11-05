@param String name
<!doctype html>
<html lang="it">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Esercizio</title>
    <script src="/webjars/htmx.org/2.0.11/dist/htmx.min.js" defer></script>
</head>
<body>
<main>
    <h1>Ciao, ${name}!</h1>
    <button type="button" hx-get="/greeting?name=Ada" hx-target="#greeting">Saluta Ada</button>
    <p id="greeting" role="status"></p>
</main>
</body>
</html>
