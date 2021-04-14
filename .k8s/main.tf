resource "kubernetes_namespace" "kubernetes_namespace" {
  metadata {
    name = local.NAMESPACE
  }
}

module "java-application" {
  source = "./java-application"


  // required common variables
  AWS_REGION     = data.aws_region.current.name
  AWS_ACCOUNT_ID = data.aws_caller_identity.current.account_id
  ENVIRONMENT    = var.ENVIRONMENT


  // required java-application variables
  DOCKER_IMAGE_NAME  = local.JAVA_APPLICATION_DOCKER_IMAGE_NAME
  DOCKER_IMAGE_TAG   = local.JAVA_APPLICATION_DOCKER_IMAGE_TAG
  REQUIRED_APPROVALS = local.JAVA_APPLICATION_REQUIRED_APPROVALS
  CPU_REQUEST        = local.JAVA_APPLICATION_CPU_REQUEST
  CPU_LIMIT          = local.JAVA_APPLICATION_CPU_LIMIT
  MEMORY_REQUEST     = local.JAVA_APPLICATION_MEMORY_REQUEST
  MEMORY_LIMIT       = local.JAVA_APPLICATION_MEMORY_LIMIT
  DESIRED_REPLICAS   = local.JAVA_APPLICATION_DESIRED_REPLICAS
  MIN_REPLICAS       = local.JAVA_APPLICATION_MIN_REPLICAS
  MAX_REPLICAS       = local.JAVA_APPLICATION_MAX_REPLICAS
  HTTP_GET_PATH      = local.JAVA_APPLICATION_HTTP_GET_PATH
  HTTP_GET_PORT      = local.JAVA_APPLICATION_HTTP_GET_PORT
  INGRESS_HOSTNAME   = local.JAVA_APPLICATION_INGRESS_HOSTNAME


  // extra java-application variables
  MONGODB_USERNAME = var.MONGODB_USERNAME
  MONGODB_PASSWORD = var.MONGODB_PASSWORD
}

module "react-application" {
  source = "./react-application"


  // required common variables
  AWS_REGION     = data.aws_region.current.name
  AWS_ACCOUNT_ID = data.aws_caller_identity.current.account_id
  ENVIRONMENT    = var.ENVIRONMENT


  // required react-application variables
  DOCKER_IMAGE_NAME  = local.REACT_APPLICATION_DOCKER_IMAGE_NAME
  DOCKER_IMAGE_TAG   = local.REACT_APPLICATION_DOCKER_IMAGE_TAG
  REQUIRED_APPROVALS = local.REACT_APPLICATION_REQUIRED_APPROVALS
  CPU_REQUEST        = local.REACT_APPLICATION_CPU_REQUEST
  CPU_LIMIT          = local.REACT_APPLICATION_CPU_LIMIT
  MEMORY_REQUEST     = local.REACT_APPLICATION_MEMORY_REQUEST
  MEMORY_LIMIT       = local.REACT_APPLICATION_MEMORY_LIMIT
  DESIRED_REPLICAS   = local.REACT_APPLICATION_DESIRED_REPLICAS
  MIN_REPLICAS       = local.REACT_APPLICATION_MIN_REPLICAS
  MAX_REPLICAS       = local.REACT_APPLICATION_MAX_REPLICAS
  HTTP_GET_PATH      = local.REACT_APPLICATION_HTTP_GET_PATH
  HTTP_GET_PORT      = local.REACT_APPLICATION_HTTP_GET_PORT
  INGRESS_HOSTNAME   = local.REACT_APPLICATION_INGRESS_HOSTNAME


  // extra react-application variables
}
