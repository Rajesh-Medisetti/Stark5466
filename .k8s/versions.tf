data "aws_region" "current" {}

data "aws_availability_zones" "available" {}

data "aws_caller_identity" "current" {}

data "aws_eks_cluster" "cluster" {
  name = local.CLUSTER_ID
}

data "aws_eks_cluster_auth" "cluster" {
  name = local.CLUSTER_ID
}

terraform {
  required_version = "~> 0.14"

  required_providers {
    random = {
      source  = "hashicorp/random"
      version = "~> 3"
    }

    local = {
      source  = "hashicorp/local"
      version = "~> 2"
    }

    null = {
      source  = "hashicorp/null"
      version = "~> 3"
    }

    template = {
      source  = "hashicorp/template"
      version = "~> 2"
    }

    github = {
      source  = "hashicorp/github"
      version = "~> 4"
    }

    aws = {
      source  = "hashicorp/aws"
      version = "~> 3"
    }

    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2"
    }
  }
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}
