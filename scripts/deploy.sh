#!/bin/bash

# ============================================================
# Job Portal - Deployment Script
# Deploys all microservices to Amazon EKS
# ============================================================

set -e

echo "🚀 Job Portal Deployment Script"
echo "================================"

# Configuration
AWS_REGION="${AWS_REGION:-us-east-1}"
EKS_CLUSTER="${EKS_CLUSTER:-jobportal-eks}"
NAMESPACE="${NAMESPACE:-jobportal}"

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    log_info "Checking prerequisites..."
    
    command -v aws >/dev/null 2>&1 || { log_error "AWS CLI is required but not installed."; exit 1; }
    command -v kubectl >/dev/null 2>&1 || { log_error "kubectl is required but not installed."; exit 1; }
    command -v helm >/dev/null 2>&1 || { log_error "Helm is required but not installed."; exit 1; }
    command -v docker >/dev/null 2>&1 || { log_error "Docker is required but not installed."; exit 1; }
    
    log_info "All prerequisites met ✓"
}

# Configure kubectl for EKS
configure_kubectl() {
    log_info "Configuring kubectl for EKS cluster: $EKS_CLUSTER"
    aws eks update-kubeconfig --region $AWS_REGION --name $EKS_CLUSTER
    log_info "kubectl configured ✓"
}

# Create namespace
create_namespace() {
    log_info "Creating namespace: $NAMESPACE"
    kubectl create namespace $NAMESPACE --dry-run=client -o yaml | kubectl apply -f -
    log_info "Namespace ready ✓"
}

# Deploy with Helm
deploy_service() {
    local service=$1
    log_info "Deploying $service..."
    
    helm upgrade --install $service ./helm/$service \
        --namespace $NAMESPACE \
        --set image.tag=${IMAGE_TAG:-latest} \
        --wait --timeout 5m
    
    log_info "$service deployed ✓"
}

# Main deployment
main() {
    check_prerequisites
    configure_kubectl
    create_namespace
    
    # Deploy services
    deploy_service "api-gateway"
    deploy_service "auth-service"
    deploy_service "job-service"
    deploy_service "application-service"
    
    log_info ""
    log_info "🎉 Deployment complete!"
    log_info ""
    log_info "Services:"
    kubectl get pods -n $NAMESPACE
    log_info ""
    log_info "Endpoints:"
    kubectl get svc -n $NAMESPACE
}

main "$@"
