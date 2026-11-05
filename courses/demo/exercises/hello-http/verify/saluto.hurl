# GET /saluto risponde con un saluto personalizzato.
GET {{base_url}}/saluto?nome=Ada
HTTP 200
[Asserts]
header "Content-Type" contains "application/json"
jsonpath "$.messaggio" == "Ciao, Ada!"

# Senza il parametro nome saluta il mondo.
GET {{base_url}}/saluto
HTTP 200
[Asserts]
jsonpath "$.messaggio" == "Ciao, mondo!"
