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
    {{- printf "jdbc:postgresql://%s-postgresql:%s/%s" .Release.Name .Values.postgresql.service.port .Values.postgresql.postgresqlDatabase -}}
{{- end -}}

{{- define "postgresql.initdb" -}}
psql {{ include "postgresql.uri" . }} <<'EOF'
truncate tbl_users;
insert into tbl_users (id, name, email) values (1, 'User Name', 'user@email.com);
EOF
{{- end }}
