- If service endpoint is `<none>` check that `spec.selector.[key_value]` matches the 
  deployment under `spec.selector.matchLabels`
- Secrets values must be encoded into `base64`