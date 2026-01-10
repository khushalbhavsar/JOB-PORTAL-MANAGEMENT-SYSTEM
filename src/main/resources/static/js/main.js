// Job Portal Management System - Main JavaScript

// API Base URL
const API_BASE = '/api';

// Authentication helper functions
const Auth = {
    getToken: () => localStorage.getItem('token'),
    getUser: () => JSON.parse(localStorage.getItem('user') || 'null'),
    isLoggedIn: () => !!localStorage.getItem('token'),
    
    setAuth: (token, user) => {
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    },
    
    clearAuth: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
    
    logout: () => {
        Auth.clearAuth();
        window.location.href = '/login';
    },
    
    getHeaders: () => ({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${Auth.getToken()}`
    })
};

// API Helper functions
const API = {
    async get(endpoint) {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            headers: Auth.getHeaders()
        });
        return response.json();
    },
    
    async post(endpoint, data) {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'POST',
            headers: Auth.getHeaders(),
            body: JSON.stringify(data)
        });
        return response.json();
    },
    
    async put(endpoint, data) {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'PUT',
            headers: Auth.getHeaders(),
            body: JSON.stringify(data)
        });
        return response.json();
    },
    
    async delete(endpoint) {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'DELETE',
            headers: Auth.getHeaders()
        });
        return response.json();
    }
};

// Utility functions
const Utils = {
    formatDate: (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },
    
    formatDateTime: (dateString) => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleString('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },
    
    formatSalary: (min, max, currency = 'INR') => {
        if (!min && !max) return 'Not Disclosed';
        const formatNum = (num) => {
            if (num >= 100000) return (num / 100000).toFixed(1) + ' LPA';
            if (num >= 1000) return (num / 1000).toFixed(0) + 'K';
            return num.toLocaleString();
        };
        
        if (min && max) {
            return `${currency} ${formatNum(min)} - ${formatNum(max)}`;
        }
        return `${currency} ${formatNum(min || max)}`;
    },
    
    getStatusBadge: (status) => {
        const statusClasses = {
            'APPLIED': 'bg-info',
            'UNDER_REVIEW': 'bg-warning text-dark',
            'SHORTLISTED': 'bg-success',
            'INTERVIEW_SCHEDULED': 'bg-purple',
            'REJECTED': 'bg-danger',
            'HIRED': 'bg-teal'
        };
        return `<span class="badge ${statusClasses[status] || 'bg-secondary'}">${status.replace('_', ' ')}</span>`;
    },
    
    showToast: (message, type = 'success') => {
        const toast = document.createElement('div');
        toast.className = `toast show position-fixed top-0 end-0 m-3`;
        toast.style.zIndex = '9999';
        toast.innerHTML = `
            <div class="toast-header bg-${type === 'success' ? 'success' : 'danger'} text-white">
                <strong class="me-auto">${type === 'success' ? 'Success' : 'Error'}</strong>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast"></button>
            </div>
            <div class="toast-body">${message}</div>
        `;
        document.body.appendChild(toast);
        setTimeout(() => toast.remove(), 3000);
    },
    
    showLoading: (container) => {
        container.innerHTML = `
            <div class="text-center py-5">
                <div class="loading-spinner"></div>
                <p class="mt-3 text-muted">Loading...</p>
            </div>
        `;
    },
    
    truncate: (text, length = 100) => {
        if (!text) return '';
        return text.length > length ? text.substring(0, length) + '...' : text;
    }
};

// Update navigation based on auth state
function updateNavigation() {
    const authNav = document.getElementById('navAuth') || document.getElementById('authButtons');
    if (!authNav) return;
    
    if (Auth.isLoggedIn()) {
        const user = Auth.getUser();
        let dashboardUrl = '/seeker/dashboard';
        if (user.role === 'ADMIN') dashboardUrl = '/admin/dashboard';
        if (user.role === 'RECRUITER') dashboardUrl = '/recruiter/dashboard';
        
        authNav.innerHTML = `
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                    <i class="fas fa-user-circle me-1"></i>${user.name}
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="${dashboardUrl}"><i class="fas fa-tachometer-alt me-2"></i>Dashboard</a></li>
                    <li><a class="dropdown-item" href="/profile"><i class="fas fa-user me-2"></i>Profile</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#" onclick="Auth.logout()"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                </ul>
            </li>
        `;
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    updateNavigation();
});

// Export for global use
window.Auth = Auth;
window.API = API;
window.Utils = Utils;
