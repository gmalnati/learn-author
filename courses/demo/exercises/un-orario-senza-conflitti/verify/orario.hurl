# L'orario calcolato all'avvio non ha conflitti, secondo il validatore indipendente dal risolutore.
GET {{base_url}}/api/timetable
HTTP 200
[Asserts]
jsonpath "$.conflicts" == 0
jsonpath "$.lessons" count == 4
jsonpath "$.score" == "0hard/0soft"

# Un nuovo calcolo restituisce la griglia per htmx, ancora senza conflitti.
POST {{base_url}}/timetable
HTTP 200
[Asserts]
body contains "Orario delle classi"
body contains "Nessun conflitto."
