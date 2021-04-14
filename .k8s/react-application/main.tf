resource "kubernetes_deployment" "kubernetes_deployment" {
  lifecycle {
    ignore_changes = [
      spec[0].template[0].metadata[0].annotations["keel.sh/update-time"]
    ]
  }

  metadata {
    name      = "${local.APPLICATION_NAME}-deployment"
    namespace = local.NAMESPACE

    labels = {
      app                 = local.APPLICATION_NAME
      name                = local.APPLICATION_NAME
      "keel.sh/policy"    = "force"
      "keel.sh/trigger"   = "poll"
      "keel.sh/match-tag" = "true"
      "keel.sh/approvals" = var.REQUIRED_APPROVALS
    }

    annotations = {
      "keel.sh/pollSchedule" = "@every 5s"
    }
  }

  spec {
    replicas = var.DESIRED_REPLICAS

    selector {
      match_labels = {
        app = local.APPLICATION_NAME
      }
    }

    template {
      metadata {
        annotations = {
          "keel.sh/update-time" = ""
        }
        labels = {
          app = local.APPLICATION_NAME
        }
      }

      spec {
        container {
          name  = local.APPLICATION_NAME
          image = "${var.AWS_ACCOUNT_ID}.dkr.ecr.${var.AWS_REGION}.amazonaws.com/${var.DOCKER_IMAGE_NAME}:${var.DOCKER_IMAGE_TAG}"

          env {
            name = "DD_AGENT_HOST"
            value_from {
              field_ref {
                field_path = "status.hostIP"
              }
            }
          }

          env {
            name = "DD_ENTITY_ID"
            value_from {
              field_ref {
                field_path = "metadata.uid"
              }
            }
          }

          env {
            name  = "ENVIRONMENT"
            value = var.ENVIRONMENT
          }

          resources {
            limits = {
              cpu    = var.CPU_LIMIT
              memory = var.MEMORY_LIMIT
            }

            requests = {
              cpu    = var.CPU_REQUEST
              memory = var.MEMORY_REQUEST
            }
          }

          liveness_probe {
            http_get {
              path = var.HTTP_GET_PATH
              port = var.HTTP_GET_PORT
            }

            initial_delay_seconds = 60
            timeout_seconds       = 10
            period_seconds        = 60
            success_threshold     = 1
            failure_threshold     = 10
          }

          readiness_probe {
            http_get {
              path = var.HTTP_GET_PATH
              port = var.HTTP_GET_PORT
            }

            initial_delay_seconds = 60
            timeout_seconds       = 10
            period_seconds        = 60
            success_threshold     = 1
            failure_threshold     = 10
          }

          image_pull_policy = "Always"
        }

        affinity {
          pod_anti_affinity {
            preferred_during_scheduling_ignored_during_execution {
              weight = 100

              pod_affinity_term {
                label_selector {
                  match_labels = {
                    app = local.APPLICATION_NAME
                  }
                }

                topology_key = "kubernetes.io/hostname"
              }
            }
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "kubernetes_service" {
  metadata {
    name      = "${local.APPLICATION_NAME}-service"
    namespace = local.NAMESPACE
  }

  spec {
    port {
      name        = "http"
      port        = var.HTTP_GET_PORT
      target_port = var.HTTP_GET_PORT
    }

    selector = {
      app = local.APPLICATION_NAME
    }

    type = "NodePort"
  }
}

resource "kubernetes_ingress" "kubernetes_ingress" {
  count = var.INGRESS_HOSTNAME != "" ? 1 : 0

  metadata {
    name      = "${local.APPLICATION_NAME}-ingress"
    namespace = local.NAMESPACE

    annotations = {
      "kubernetes.io/ingress.class" = "nginx"
    }
  }

  spec {
    rule {
      host = var.INGRESS_HOSTNAME

      http {
        path {
          path = "/"

          backend {
            service_name = "${local.APPLICATION_NAME}-service"
            service_port = var.HTTP_GET_PORT
          }
        }
      }
    }
  }
}

resource "kubernetes_horizontal_pod_autoscaler" "kubernetes_horizontal_pod_autoscaler" {
  count = var.DESIRED_REPLICAS == var.MIN_REPLICAS && var.MIN_REPLICAS == var.MAX_REPLICAS ? 0 : 1

  metadata {
    name      = "${local.APPLICATION_NAME}-hpa"
    namespace = local.NAMESPACE
  }

  spec {
    scale_target_ref {
      kind        = "Deployment"
      name        = "${local.APPLICATION_NAME}-deployment"
      api_version = "apps/v1"
    }

    min_replicas                      = var.MIN_REPLICAS
    max_replicas                      = var.MAX_REPLICAS
    target_cpu_utilization_percentage = 75
  }
}
