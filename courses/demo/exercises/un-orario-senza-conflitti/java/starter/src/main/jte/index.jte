@import dev.learnlab.exercise.TimetableView
@param TimetableView timetable
<!doctype html>
<html lang="it">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Orario</title>
    <script src="/webjars/htmx.org/2.0.11/dist/htmx.min.js" defer></script>
</head>
<body>
<main>
    <h1>Orario</h1>
    <button type="button" hx-post="/timetable" hx-target="#grid" hx-swap="outerHTML">Ricalcola</button>
    @template.grid(timetable = timetable)
</main>
</body>
</html>
