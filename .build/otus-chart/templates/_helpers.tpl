{{/* vim: set filetype=mustache: */}}

{{/* Name of auth service */}}
{{- define "apps.auth.name" -}}
    {{- printf "%s-%s" .Release.Name .Values.apps.auth.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{/* Name of api service */}}
{{- define "apps.api.name" -}}
    {{- printf "%s-%s" .Release.Name .Values.apps.api.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/* Server auth url */}}
{{- define "apps.auth.url" -}}
    {{- printf "http://%s%s" .Values.apps.auth.ingress.host .Values.apps.auth.ingress.basePath -}}
{{- end -}}
{{/* Server api url */}}
{{- define "apps.api.url" -}}
    {{- printf "http://%s%s" .Values.apps.api.ingress.host .Values.apps.api.ingress.basePath -}}
{{- end -}}

{{/* Common labels */}}
{{- define "app.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/* Postgres SQL functions */}}
{{- define "postgresql.name" -}}
{{ .Release.Name }}-postgresql
{{- end -}}
{{- define "postgresql.uri" -}}
    {{- printf "%s:%s/%s" (include "postgresql.name" .) .Values.postgresql.service.port .Values.postgresql.postgresqlDatabase -}}
{{- end -}}
{{- define "postgresql.jdbc" -}}
jdbc:postgresql://{{ include "postgresql.uri" . }}
{{- end -}}
