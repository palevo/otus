{{/* vim: set filetype=mustache: */}}

{{- define "app.name" -}}
    {{- printf "%s-%s" .Release.Name .Values.app.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "app.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/name: {{ include "app.name" . }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "postgresql.uri" -}}
    {{- printf "%s-postgresql:%s/%s" .Release.Name .Values.postgresql.service.port .Values.postgresql.postgresqlDatabase -}}
{{- end -}}
{{- define "postgresql.jdbc" -}}
jdbc:postgresql://{{ include "postgresql.uri" . }}
{{- end -}}
