// required common variables
variable "AWS_REGION" {}
variable "AWS_ACCOUNT_ID" {}
variable "ENVIRONMENT" {}


// required react-application variables
variable "DOCKER_IMAGE_NAME" {}
variable "DOCKER_IMAGE_TAG" {}
variable "REQUIRED_APPROVALS" {}
variable "CPU_REQUEST" {}
variable "CPU_LIMIT" {}
variable "MEMORY_REQUEST" {}
variable "MEMORY_LIMIT" {}
variable "DESIRED_REPLICAS" {}
variable "MIN_REPLICAS" {}
variable "MAX_REPLICAS" {}
variable "HTTP_GET_PATH" {}
variable "HTTP_GET_PORT" {}
variable "INGRESS_HOSTNAME" {}


// extra react-application variables
