# Le verifiche controllano che il servizio sia rimasto quello corretto: l'esercizio chiede di
# scrivere i test, non di cambiare il codice.

# Saluta per nome.
GET {{base_url}}/saluto?nome=Ada
HTTP 200
[Asserts]
header "Content-Type" contains "application/json"
jsonpath "$.messaggio" == "Ciao, Ada!"

# Toglie gli spazi intorno al nome.
GET {{base_url}}/saluto?nome=%20%20Ada%20
HTTP 200
[Asserts]
jsonpath "$.messaggio" == "Ciao, Ada!"

# Senza nome saluta il mondo.
GET {{base_url}}/saluto
HTTP 200
[Asserts]
jsonpath "$.messaggio" == "Ciao, mondo!"

# Con un nome di soli spazi saluta il mondo.
GET {{base_url}}/saluto?nome=%20%20%20
HTTP 200
[Asserts]
jsonpath "$.messaggio" == "Ciao, mondo!"

# Un nome di 30 caratteri si saluta.
GET {{base_url}}/saluto?nome=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
HTTP 200

# Un nome di 31 caratteri è troppo lungo.
GET {{base_url}}/saluto?nome=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
HTTP 400
