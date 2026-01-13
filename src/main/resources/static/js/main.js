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
/**
 * MASI Labo - Main JavaScript
 * Shared functionality across all pages
 *
 * Includes:
 * - Mobile sidebar toggle + overlay
 * - Profile dropdown menu (safe, no immediate close, supports multiple instances)
 * - Utilities (confirmDelete, formatDate)
 */

(function () {
    "use strict";

    // ================================
    // Helpers
    // ================================
    const qs = (sel, root = document) => root.querySelector(sel);
    const qsa = (sel, root = document) => Array.from(root.querySelectorAll(sel));

    // ================================
    // Mobile Sidebar Management
    // ================================
    function toggleMobileSidebar() {
        const sidebar = document.getElementById("sidebar");
        const overlay = document.getElementById("sidebarOverlay");

        if (sidebar) sidebar.classList.toggle("mobile-open");
        if (overlay) overlay.classList.toggle("active");

        if (sidebar) {
            document.body.style.overflow = sidebar.classList.contains("mobile-open")
                ? "hidden"
                : "";
        }
    }

    function closeSidebar() {
        const sidebar = document.getElementById("sidebar");
        const overlay = document.getElementById("sidebarOverlay");

        if (sidebar) sidebar.classList.remove("mobile-open");
        if (overlay) overlay.classList.remove("active");
        document.body.style.overflow = "";
    }

    // Expose for inline onclick usage
    window.toggleMobileSidebar = toggleMobileSidebar;

    // ================================
    // Profile Dropdown (Robust)
    // ================================
    function closeAllProfileMenus() {
        qsa(".profile-dropdown").forEach((dropdown) => {
            const menu = dropdown.querySelector(".profile-menu");
            const btn = dropdown.querySelector(".profile-trigger");
            if (menu) menu.classList.add("hidden");
            if (btn) btn.setAttribute("aria-expanded", "false");
        });
    }

    function toggleProfileMenu(e) {
        if (e) e.stopPropagation();

        // Works even if you have multiple dropdowns
        const btn = e?.currentTarget || null;
        const dropdown = btn ? btn.closest(".profile-dropdown") : document.getElementById("profileDropdown");

        if (!dropdown) return;

        const menu =
            dropdown.querySelector(".profile-menu") ||
            dropdown.querySelector("#profileMenu");

        if (!menu) return;

        const isOpen = !menu.classList.contains("hidden");

        // Close all first
        closeAllProfileMenus();

        // If it was closed, open it
        if (!isOpen) {
            menu.classList.remove("hidden");
            const trigger = dropdown.querySelector(".profile-trigger");
            if (trigger) trigger.setAttribute("aria-expanded", "true");
        }
    }

    // Expose for inline onclick usage
    window.toggleProfileMenu = toggleProfileMenu;

    // ================================
    // Utilities
    // ================================
    function confirmDelete(message) {
        return confirm(message || "Are you sure you want to delete this item?");
    }
    window.confirmDelete = confirmDelete;

    function formatDate(dateString) {
        const options = { year: "numeric", month: "short", day: "numeric" };
        return new Date(dateString).toLocaleDateString("en-US", options);
    }
    window.formatDate = formatDate;

    // ================================
    // Initialization
    // ================================
    document.addEventListener("DOMContentLoaded", function () {
        // Sidebar overlay click handler
        const overlay = document.getElementById("sidebarOverlay");
        if (overlay) overlay.addEventListener("click", closeSidebar);

        // PROFILE: Stop clicks inside dropdown from closing it
        qsa(".profile-dropdown").forEach((dropdown) => {
            dropdown.addEventListener("click", (ev) => ev.stopPropagation());

            const menu = dropdown.querySelector(".profile-menu") || dropdown.querySelector("#profileMenu");
            if (menu) menu.addEventListener("click", (ev) => ev.stopPropagation());

            // If you prefer NOT using inline onclick, uncomment this:
            // const btn = dropdown.querySelector(".profile-trigger");
            // if (btn) btn.addEventListener("click", toggleProfileMenu);
        });

        // Click outside closes all profile menus
        document.addEventListener("click", closeAllProfileMenus);

        // ESC closes sidebar + profile menus
        document.addEventListener("keydown", function (e) {
            if (e.key === "Escape") {
                closeSidebar();
                closeAllProfileMenus();
            }
        });
    });

    // ================================
    // Responsive Handler
    // ================================
    window.addEventListener("resize", function () {
        const sidebar = document.getElementById("sidebar");
        const overlay = document.getElementById("sidebarOverlay");

        if (window.innerWidth > 768) {
            if (sidebar) sidebar.classList.remove("mobile-open");
            if (overlay) overlay.classList.remove("active");
            document.body.style.overflow = "";
        }
    });
})();
