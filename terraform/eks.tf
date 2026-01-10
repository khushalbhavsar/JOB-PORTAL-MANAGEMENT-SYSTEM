# ============================================================
# EKS Cluster Configuration (Auto Mode)
# ============================================================

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 19.0"

  cluster_name    = "${var.project_name}-eks"
  cluster_version = var.eks_cluster_version

  vpc_id                         = module.vpc.vpc_id
  subnet_ids                     = module.vpc.private_subnets
  cluster_endpoint_public_access = true

  # EKS Add-ons
  cluster_addons = {
    coredns = {
      most_recent = true
    }
    kube-proxy = {
      most_recent = true
    }
    vpc-cni = {
      most_recent = true
    }
    aws-ebs-csi-driver = {
      most_recent = true
    }
  }

  # EKS Managed Node Groups
  eks_managed_node_groups = {
    general = {
      name           = "${var.project_name}-node-group"
      instance_types = var.eks_node_instance_types

      min_size     = var.eks_min_capacity
      max_size     = var.eks_max_capacity
      desired_size = var.eks_desired_capacity

      labels = {
        role = "general"
      }

      tags = {
        "k8s.io/cluster-autoscaler/enabled"                      = "true"
        "k8s.io/cluster-autoscaler/${var.project_name}-eks"      = "owned"
      }
    }
  }

  # Enable IRSA
  enable_irsa = true

  # Cluster access
  manage_aws_auth_configmap = true

  tags = {
    Name = "${var.project_name}-eks"
  }
}

# EKS Cluster Outputs
output "cluster_endpoint" {
  description = "EKS cluster endpoint"
  value       = module.eks.cluster_endpoint
}

output "cluster_name" {
  description = "EKS cluster name"
  value       = module.eks.cluster_name
}

output "cluster_security_group_id" {
  description = "Security group ID attached to the EKS cluster"
  value       = module.eks.cluster_security_group_id
}
