locals {
  app_labels = {
    App = var.app_name
    Tier = "backend"
  }
  sql_db_labels = {
    App = var.app_name
    Tier = "sql_database"
  }
}

resource "kubernetes_deployment" "app-deployment" {
  metadata {
    name = var.app_name
    labels = local.app_labels
  }
  spec {
    replicas = 1
    selector {
      match_labels = local.app_labels
    }
    template {
      metadata {
        labels = local.app_labels
      }
      spec {
        container {
          image = "${var.app_name}:${var.app_version}"
          name  = var.app_name
          port {
            container_port = 80
          }
          env {
            name = "WORDPRESS_DB_HOST"
            value = "mysql-service"
          }
          env {
            name = "WORDPRESS_DB_PASSWORD"
            value_from {
              secret_key_ref {
                name = "mysql-pass"
                key = "password"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "app-service" {
  metadata {
    name = "${var.app_name}-service"
  }
  spec {
    selector = local.app_labels
    port {
      port        = 80
      target_port = 80
      node_port = 32000
    }
    type = "NodePort"
  }
}