# Le regole del Dockerfile (ADR 033). Ogni messaggio comincia con l'id della regola di static.yaml.
package dockerfile

import rego.v1

users := [command.Value[0] | some command in input; command.Cmd == "user"]

deny contains "D1: manca USER, quindi il servizio gira come root" if {
	count(users) == 0
}

deny contains sprintf("D1: l'ultimo USER è %s, cioè root", [user]) if {
	user := users[count(users) - 1]
	user in {"root", "0", "0:0", "root:root"}
}
