/**
 * MASI Labo - Main JavaScript
 * Shared functionality across all pages
 */

// ================================
// Mobile Sidebar Management
// ================================
function toggleMobileSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar) {
        sidebar.classList.toggle('mobile-open');
    }
    if (overlay) {
        overlay.classList.toggle('active');
    }
    // Prevent body scroll when sidebar is open
    document.body.style.overflow = sidebar.classList.contains('mobile-open') ? 'hidden' : '';
}

// Close sidebar when clicking overlay
function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar) {
        sidebar.classList.remove('mobile-open');
    }
    if (overlay) {
        overlay.classList.remove('active');
    }
    document.body.style.overflow = '';
}

// ================================
// Initialization
// ================================
document.addEventListener('DOMContentLoaded', function() {
    // Setup overlay click handler
    const overlay = document.getElementById('sidebarOverlay');
    if (overlay) {
        overlay.addEventListener('click', closeSidebar);
    }

    // Close sidebar on escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeSidebar();
        }
    });
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
        document.body.style.overflow = '';
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
function toggleProfileMenu(e) {
    e.stopPropagation();
    const menu = document.getElementById("profileMenu");
    const btn = e.currentTarget;
    const isOpen = !menu.classList.contains("hidden");

    // Close any open menu first
    closeProfileMenu();

    if (!isOpen) {
        menu.classList.remove("hidden");
        btn.setAttribute("aria-expanded", "true");
    }
}

function closeProfileMenu() {
    const menu = document.getElementById("profileMenu");
    const dropdown = document.getElementById("profileDropdown");
    if (!menu || !dropdown) return;

    menu.classList.add("hidden");
    const btn = dropdown.querySelector("button[aria-expanded]");
    if (btn) btn.setAttribute("aria-expanded", "false");
}

// Close on click outside
document.addEventListener("click", closeProfileMenu);

// Close on ESC
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeProfileMenu();
});
