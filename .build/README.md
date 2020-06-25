helm install -n otus otus .build/otus-chart

kubectl apply -n otus -f manifests/job-stresstest.yaml
