# 🚀 Job Portal Management System

## Cloud-Native 3-Tier Spring Boot Application with GitOps on Amazon EKS

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-EKS-blue.svg)](https://aws.amazon.com/eks/)
[![ArgoCD](https://img.shields.io/badge/GitOps-ArgoCD-orange.svg)](https://argoproj.github.io/cd/)
[![Terraform](https://img.shields.io/badge/IaC-Terraform-purple.svg)](https://www.terraform.io/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Application Modules](#-application-modules)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [Deployment Options](#-deployment-options)
- [API Documentation](#-api-documentation)
- [Demo Credentials](#-demo-credentials)
- [Contributing](#-contributing)

---

## 🎯 Overview

**Job Portal Management System** is a production-grade, cloud-native job portal built using **3-Tier Architecture** with Spring Boot. It connects job seekers with recruiters through a modern, scalable, and secure platform deployed on AWS EKS using GitOps practices.

### ✨ Key Features

- 🔐 **JWT-based Authentication** with Role-Based Access Control
- 👤 **Multi-role Support**: Admin, Recruiter, Job Seeker
- 💼 **Job Management**: Post, Search, Filter, Apply
- 📊 **Real-time Dashboard** for all user roles
- 🏗️ **Clean 3-Tier Architecture** (Controller → Service → Repository)
- 🐳 **Containerized** with Docker for consistent deployments
- 🚀 **Auto-scaling** with Kubernetes HPA
- 📈 **Observability** with Prometheus & Grafana
- 🔄 **GitOps** deployment with ArgoCD
- 🏗️ **Infrastructure as Code** with Terraform
- 🔄 **Microservices-Ready** architecture for future evolution

---

## 🏗️ Architecture

### Application Architecture (3-Tier)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT LAYER                                    │
│                    [ Web Browser / Mobile App / API Client ]                 │
└─────────────────────────────────┬───────────────────────────────────────────┘
                                  │ HTTPS
                                  ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      SPRING BOOT APPLICATION                                 │
│                           [ Port 8080 ]                                      │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                     PRESENTATION LAYER (Controllers)                   │  │
│  │    AuthController │ JobController │ ApplicationController │ Admin     │  │
│  └───────────────────────────────────┬───────────────────────────────────┘  │
│                                      │                                       │
│  ┌───────────────────────────────────▼───────────────────────────────────┐  │
│  │                      BUSINESS LAYER (Services)                         │  │
│  │     AuthService │ JobService │ ApplicationService │ AdminService      │  │
│  │                    + JWT Security + Validation                         │  │
│  └───────────────────────────────────┬───────────────────────────────────┘  │
│                                      │                                       │
│  ┌───────────────────────────────────▼───────────────────────────────────┐  │
│  │                     DATA ACCESS LAYER (Repositories)                   │  │
│  │    UserRepository │ JobRepository │ ApplicationRepository │ Spring JPA│  │
│  └───────────────────────────────────┬───────────────────────────────────┘  │
└──────────────────────────────────────┼──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DATA LAYER                                      │
│           ┌─────────────┐                    ┌─────────────┐                 │
│           │   MySQL     │                    │    Redis    │                 │
│           │  (H2 Dev)   │                    │   (Cache)   │                 │
│           └─────────────┘                    └─────────────┘                 │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Cloud Deployment Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              AWS CLOUD                                       │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                    AMAZON EKS CLUSTER                                  │  │
│  │   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                   │  │
│  │   │   Pod 1     │  │   Pod 2     │  │   Pod N     │   ← HPA Scaling   │  │
│  │   │ Job Portal  │  │ Job Portal  │  │ Job Portal  │                   │  │
│  │   └─────────────┘  └─────────────┘  └─────────────┘                   │  │
│  │                          ▲                                             │  │
│  │                          │ ArgoCD GitOps Sync                          │  │
│  └──────────────────────────┼────────────────────────────────────────────┘  │
│                             │                                                │
│  ┌──────────────────────────┼────────────────────────────────────────────┐  │
│  │         RDS MySQL        │        ElastiCache Redis                    │  │
│  └──────────────────────────┴────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
                                       ▲
                                       │ GitHub Actions CI/CD
                                       │
┌─────────────────────────────────────────────────────────────────────────────┐
│                         OBSERVABILITY LAYER                                  │
│         [ Prometheus ]  [ Grafana ]  [ CloudWatch ]                          │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 🧩 Application Modules

| Module | Package | Description |
|--------|---------|-------------|
| **Auth Module** | `com.jobportal.security` | JWT authentication, role management, Spring Security |
| **User Module** | `com.jobportal.user` | Job seeker & recruiter profile management |
| **Job Module** | `com.jobportal.job` | Job posting, search, filtering, company management |
| **Application Module** | `com.jobportal.application` | Job applications, status tracking, notifications |
| **Admin Module** | `com.jobportal.admin` | Platform monitoring, user moderation, statistics |

> 💡 **Note**: These are logical modules within a single Spring Boot application, designed with clear separation of concerns. This architecture can evolve into microservices when scale demands it.

---

## 🛠️ Tech Stack

### Backend

| Technology | Purpose |
|------------|---------|
| Java 17 | Programming Language |
| Spring Boot 3.2 | Application Framework |
| Spring MVC | REST API Controllers |
| Spring Security + JWT | Authentication & Authorization |
| Spring Data JPA | Database ORM |
| Hibernate | ORM Implementation |
| Thymeleaf | Server-side templating (UI) |

### Database

| Technology | Purpose |
|------------|--------|
| MySQL 8.0 / H2 | Primary database (H2 for development) |
| Redis | Caching, Session management (optional) |

### DevOps & Cloud

| Technology | Purpose |
|------------|---------|
| Docker | Containerization |
| Kubernetes (EKS) | Container Orchestration |
| Helm | Kubernetes Package Manager |
| ArgoCD | GitOps Continuous Deployment |
| Terraform | Infrastructure as Code |
| GitHub Actions | CI/CD Pipeline |

### Observability

| Technology | Purpose |
|------------|---------|
| Prometheus | Metrics Collection |
| Grafana | Visualization & Dashboards |
| AWS CloudWatch | AWS Native Monitoring |

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- (Optional) Docker, kubectl, AWS CLI, Terraform

### Option 1: Run Locally (Fastest)

```bash
# Clone the repository
git clone https://github.com/yourusername/job-portal-gitops.git
cd job-portal-gitops

# Run with Maven (uses H2 in-memory database)
mvn spring-boot:run

# Access the application
open http://localhost:8080
```

### Option 2: Docker Compose

```bash
# Start with MySQL & Redis
docker-compose up -d

# Access the application
open http://localhost:8080
```

### Option 3: Kubernetes (Production)

See [SETUP.md](docs/SETUP.md) for detailed EKS deployment with GitOps.

---

## 📦 Deployment Options

### Branch Strategy

| Branch | Purpose | Deployment |
|--------|---------|------------|
| `main` | Development | Docker Compose |
| `gitops` | Production | ArgoCD + EKS |

### GitOps Workflow

```
Developer Push → GitHub Actions → Build & Test → Docker Image → ECR
                                                      ↓
ArgoCD Sync ← Helm Values Update ← Image Tag Update ←┘
     ↓
Amazon EKS Deployment
```

---

## 📚 API Documentation

### Base URLs

| Environment | URL |
|-------------|-----|
| Local | http://localhost:8080/api |
| Production | https://api.jobportal.com |

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/refresh` | Refresh JWT token |
| GET | `/api/auth/me` | Get current user |

### Job Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/jobs` | List all jobs |
| GET | `/api/jobs/{id}` | Get job details |
| POST | `/api/jobs` | Create job (Recruiter) |
| PUT | `/api/jobs/{id}` | Update job (Recruiter) |
| DELETE | `/api/jobs/{id}` | Delete job (Recruiter) |

### Application Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/applications/apply` | Apply for job |
| GET | `/api/applications/my-applications` | Get user applications |
| PUT | `/api/applications/{id}/status` | Update status (Recruiter) |

---

## 🔑 Demo Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@jobportal.com | password123 |
| Recruiter | rahul@techcorp.com | password123 |
| Job Seeker | amit@gmail.com | password123 |

---

## 📁 Project Structure

```
job-portal/
├── src/main/java/com/jobportal/    # Application Source Code
│   ├── controller/                  # REST Controllers (Presentation Layer)
│   ├── service/                     # Business Logic (Service Layer)
│   ├── repository/                  # Data Access (Repository Layer)
│   ├── entity/                      # JPA Entities
│   ├── dto/                         # Data Transfer Objects
│   ├── security/                    # JWT & Spring Security
│   └── config/                      # Configuration classes
├── src/main/resources/
│   ├── templates/                   # Thymeleaf HTML templates
│   ├── static/                      # CSS, JS, Images
│   └── application.yml              # Configuration
├── docker/                          # Docker configurations
│   └── Dockerfile
├── helm/                            # Helm charts for K8s
│   └── job-portal/
├── terraform/                       # AWS Infrastructure (IaC)
│   ├── eks.tf
│   ├── rds.tf
│   └── ecr.tf
├── gitops/                          # ArgoCD configurations
│   └── argocd/
├── monitoring/                      # Observability configs
│   ├── prometheus/
│   └── grafana/
├── .github/workflows/               # CI/CD pipelines
├── docker-compose.yml               # Local development
├── pom.xml                          # Maven configuration
└── README.md
```

---

## 🏆 Resume Description

> **Job Portal Management System – Cloud-Native Spring Boot Application (Java, AWS, GitOps)**
> 
> Designed and developed a production-grade job portal using Spring Boot 3-tier architecture, then productionized it for cloud deployment on Amazon EKS with GitOps (ArgoCD). Implemented JWT-based authentication, role-based access control, CI/CD pipelines using GitHub Actions, containerized the application with Docker, created Helm charts for Kubernetes deployments, and managed AWS infrastructure using Terraform. The modular architecture is designed to evolve into microservices when scale demands it.

### 🎤 Interview Talking Points

- "Built a 3-tier Spring Boot application and productionized it using Docker, Kubernetes on AWS EKS, GitHub Actions for CI, and ArgoCD for GitOps-based deployment."
- "The architecture follows clean separation of concerns (Controller → Service → Repository) making it easy to extract into microservices later."
- "Gained hands-on experience with the full DevOps lifecycle: containerization, orchestration, IaC, CI/CD, and observability."

---

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Khushal Bhavsar**

- GitHub: [@khushalbhavsar](https://github.com/khushalbhavsar)
- LinkedIn: [Khushal Bhavsar](https://linkedin.com/in/khushalbhavsar)

---

<p align="center">
  Made with ❤️ for learning and career growth
</p>
