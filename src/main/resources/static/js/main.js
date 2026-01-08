/**
 * MASI Labo - Main JavaScript
 * Shared functionality across all pages
 */

// ================================
// Sidebar Management
// ================================
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');
    const overlay = document.getElementById('sidebarOverlay');

    if (window.innerWidth <= 768) {
        // Mobile: toggle mobile-open class
        sidebar.classList.toggle('mobile-open');
        if (overlay) {
            overlay.classList.toggle('active');
        }
    } else {
        // Desktop: toggle collapsed class
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');

        // Save preference to localStorage
        const isCollapsed = sidebar.classList.contains('collapsed');
        localStorage.setItem('sidebarCollapsed', isCollapsed);
    }
}

// Close sidebar when clicking overlay
function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    sidebar.classList.remove('mobile-open');
    if (overlay) {
        overlay.classList.remove('active');
    }
}

// ================================
// Initialization
// ================================
document.addEventListener('DOMContentLoaded', function() {
    // Restore sidebar state from localStorage
    const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    if (isCollapsed && window.innerWidth > 768) {
        const sidebar = document.getElementById('sidebar');
        const mainContent = document.getElementById('mainContent');
        if (sidebar) sidebar.classList.add('collapsed');
        if (mainContent) mainContent.classList.add('expanded');
    }

    // Setup overlay click handler
    const overlay = document.getElementById('sidebarOverlay');
    if (overlay) {
        overlay.addEventListener('click', closeSidebar);
    }
});

// ================================
// Responsive Handler
// ================================
window.addEventListener('resize', function() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (window.innerWidth > 768) {
        // Remove mobile classes on resize to desktop
        if (sidebar) sidebar.classList.remove('mobile-open');
        if (overlay) overlay.classList.remove('active');
    }
});

// ================================
// Utility Functions
// ================================

// Confirm delete action
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this item?');
}

// Format date
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'short', day: 'numeric' };
    return new Date(dateString).toLocaleDateString('en-US', options);
}
