# Le regole dei manifest (ADR 033). Con combine l'input è l'elenco dei file, {path, contents}: un file
# con più documenti separati da --- ha l'elenco dei documenti. Ogni messaggio comincia con l'id della
# regola di static.yaml.
package kubernetes

import rego.v1

documents contains document if {
	some file in input
	is_object(file.contents)
	document := file.contents
}

documents contains document if {
	some file in input
	is_array(file.contents)
	some document in file.contents
	is_object(document)
}

deployments contains deployment if {
	some deployment in documents
	deployment.kind == "Deployment"
}

containers contains {"deployment": deployment.metadata.name, "container": container} if {
	some deployment in deployments
	some container in deployment.spec.template.spec.containers
}

deny contains sprintf("K1: il container %s del Deployment %s usa %s, senza una versione", [c.container.name, c.deployment, c.container.image]) if {
	some c in containers
	not regex.match(`:[^/:]+$`, c.container.image)
}

deny contains sprintf("K1: il container %s del Deployment %s usa %s: latest cambia senza che il manifest cambi", [c.container.name, c.deployment, c.container.image]) if {
	some c in containers
	endswith(c.container.image, ":latest")
}

deny contains sprintf("K2: il container %s del Deployment %s non ha %s.%s", [c.container.name, c.deployment, kind, resource]) if {
	some c in containers
	some kind in ["requests", "limits"]
	some resource in ["cpu", "memory"]
	not c.container.resources[kind][resource]
}

deny contains sprintf("K3: il container %s del Deployment %s non ha una readinessProbe con httpGet", [c.container.name, c.deployment]) if {
	some c in containers
	not c.container.readinessProbe.httpGet
}

deny contains sprintf("K4: il Service %s non seleziona i pod di nessun Deployment", [service.metadata.name]) if {
	some service in documents
	service.kind == "Service"
	not selects_pods(service)
}

selects_pods(service) if {
	some deployment in deployments
	every key, value in service.spec.selector {
		deployment.spec.template.metadata.labels[key] == value
	}
}
