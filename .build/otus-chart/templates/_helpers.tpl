{{/* vim: set filetype=mustache: */}}

{{- define "app.name" -}}
    {{- printf "%s-%s" .Release.Name .Values.app.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "app.labels" -}}
app: {{ include "app.name" . }}
version: {{ .Chart.AppVersion }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- end -}}

{{- define "postgresql.uri" -}}
    {{- printf "%s-postgresql:%s/%s" .Release.Name .Values.postgresql.service.port .Values.postgresql.postgresqlDatabase -}}
{{- end -}}
{{- define "postgresql.jdbc" -}}
jdbc:postgresql://{{ include "postgresql.uri" . }}
{{- end -}}
