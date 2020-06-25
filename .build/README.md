helm install -n otus otus .build/service-auth-chart
helm install -n otus otus .build/service-api-chart

kubectl apply -n otus -f manifests/job-stresstest.yaml
