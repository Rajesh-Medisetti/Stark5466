// required common variables
variable "ENVIRONMENT" {}








// required java-application variables
variable "JAVA_APPLICATION_DOCKER_IMAGE_NAME" {
  default = ""
}
variable "JAVA_APPLICATION_DOCKER_IMAGE_TAG" {
  default = ""
}
variable "JAVA_APPLICATION_REQUIRED_APPROVALS" {
  default = ""
}
variable "JAVA_APPLICATION_CPU_REQUEST" {
  default = ""
}
variable "JAVA_APPLICATION_CPU_LIMIT" {
  default = ""
}
variable "JAVA_APPLICATION_MEMORY_REQUEST" {
  default = ""
}
variable "JAVA_APPLICATION_MEMORY_LIMIT" {
  default = ""
}
variable "JAVA_APPLICATION_DESIRED_REPLICAS" {
  default = ""
}
variable "JAVA_APPLICATION_MIN_REPLICAS" {
  default = ""
}
variable "JAVA_APPLICATION_MAX_REPLICAS" {
  default = ""
}
variable "JAVA_APPLICATION_HTTP_GET_PATH" {
  default = ""
}
variable "JAVA_APPLICATION_HTTP_GET_PORT" {
  default = ""
}
variable "JAVA_APPLICATION_INGRESS_HOSTNAME" {
  default = ""
}


// extra java-application variables
variable "MONGODB_USERNAME" {}
variable "MONGODB_PASSWORD" {}

// required react-application variables
variable "REACT_APPLICATION_DOCKER_IMAGE_NAME" {
  default = ""
}
variable "REACT_APPLICATION_DOCKER_IMAGE_TAG" {
  default = ""
}
variable "REACT_APPLICATION_REQUIRED_APPROVALS" {
  default = ""
}
variable "REACT_APPLICATION_CPU_REQUEST" {
  default = ""
}
variable "REACT_APPLICATION_CPU_LIMIT" {
  default = ""
}
variable "REACT_APPLICATION_MEMORY_REQUEST" {
  default = ""
}
variable "REACT_APPLICATION_MEMORY_LIMIT" {
  default = ""
}
variable "REACT_APPLICATION_DESIRED_REPLICAS" {
  default = ""
}
variable "REACT_APPLICATION_MIN_REPLICAS" {
  default = ""
}
variable "REACT_APPLICATION_MAX_REPLICAS" {
  default = ""
}
variable "REACT_APPLICATION_HTTP_GET_PATH" {
  default = ""
}
variable "REACT_APPLICATION_HTTP_GET_PORT" {
  default = ""
}
variable "REACT_APPLICATION_INGRESS_HOSTNAME" {
  default = ""
}


// extra react-application variables
