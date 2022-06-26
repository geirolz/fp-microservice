# ---------------------------------------------------------------------------------------------------------------------
# MODULE PARAMETERS
# These variables are expected to be passed in by the operator
# ---------------------------------------------------------------------------------------------------------------------

# App info
variable "app_name" {
  description = "App name value used to label resources"
  type        = string
  default     = "app"
}

variable "app_version" {
  description = "App version with format x.y.z"
  type        = string
  default     = "latest"
}


# Kubectl options
variable "kubectl_config_context_name" {
  description = "The config context to use when authenticating to the Kubernetes cluster. If empty, defaults to the current context specified in the kubeconfig file."
  type        = string
  default     = "minikube"
}

variable "kubectl_config_path" {
  description = "The path to the config file to use for kubectl. If empty, defaults to $HOME/.kube/config"
  type        = string
  default     = "~/.kube/config"
}