# Minikube

## Prerequisite
- [gettext](https://www.gnu.org/software/gettext/)
- [Docker](https://www.docker.com/)
- [Minikube](https://minikube.sigs.k8s.io/docs/)

## Note
- If service endpoint is `<none>` check that `spec.selector.[key_value]` matches the 
  deployment under `spec.selector.matchLabels`
- Secrets values must be encoded into `base64`