helm install -n otus-monitoring hcn stable/nginx-ingress -f .build/monitoring/nginx-ingress.yaml
helm install -n otus-monitoring hcp stable/prometheus-operator -f .build/monitoring/prometheus-operator.yaml
helm install -n otus hcppe stable/prometheus-postgres-exporter -f .build/monitoring/prometheus-postgres-exporter.yaml 
