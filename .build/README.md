helm install -n otus otus .build/otus-chart

kubectl apply -n otus -f manifests/job-stresstest.yaml
kubectl port-forward -n otus otus-auth-b9b569b5c-ddn4c 8080:80
