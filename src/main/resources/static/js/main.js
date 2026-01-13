/**
 * MASI Labo - Main JavaScript
 * Handles Sidebar and Profile Dropdown
 */

document.addEventListener('DOMContentLoaded', function() {
    // 1. Setup Global Click Listener (Closes menus when clicking outside)
    document.addEventListener('click', function(event) {
        closeProfileMenuIfClickedOutside(event);
        closeSidebarIfClickedOutside(event);
    });

    // 2. Setup Escape Key Listener
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeProfileMenu();
            closeSidebar();
        }
    });
});

// ================================
// PROFILE DROPDOWN LOGIC
// ================================

function toggleProfileMenu(event) {
    // Stop the click from bubbling up to the document (which would close it immediately)
    if (event) event.stopPropagation();

    const menu = document.getElementById('profileMenu');
    const btn = document.querySelector('.profile-trigger');

    if (!menu) return;

    // Toggle the hidden class
    const isHidden = menu.classList.toggle('hidden');

    // Update ARIA attribute for accessibility
    if (btn) {
        btn.setAttribute('aria-expanded', !isHidden);
    }
}

function closeProfileMenu() {
    const menu = document.getElementById('profileMenu');
    const btn = document.querySelector('.profile-trigger');

    if (menu && !menu.classList.contains('hidden')) {
        menu.classList.add('hidden');
        if (btn) btn.setAttribute('aria-expanded', 'false');
    }
}

function closeProfileMenuIfClickedOutside(event) {
    const menu = document.getElementById('profileMenu');
    const dropdown = document.getElementById('profileDropdown');

    // If menu is open AND click is NOT inside the dropdown
    if (menu && !menu.classList.contains('hidden')) {
        if (dropdown && !dropdown.contains(event.target)) {
            closeProfileMenu();
        }
    }
}

// ================================
// MOBILE SIDEBAR LOGIC
// ================================

function toggleMobileSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar) sidebar.classList.toggle('mobile-open');
    if (overlay) overlay.classList.toggle('active');

    // Prevent scrolling body when sidebar is open
    if (sidebar && sidebar.classList.contains('mobile-open')) {
        document.body.style.overflow = 'hidden';
    } else {
        document.body.style.overflow = '';
    }
}

function closeSidebar() {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');

    if (sidebar) sidebar.classList.remove('mobile-open');
    if (overlay) overlay.classList.remove('active');
    document.body.style.overflow = '';
}

function closeSidebarIfClickedOutside(event) {
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('sidebarOverlay');
    const menuBtn = document.querySelector('.mobile-menu-btn');

    // Only run on mobile (when overlay is active)
    if (sidebar && sidebar.classList.contains('mobile-open')) {
        // If click is on overlay
        if (event.target === overlay) {
            closeSidebar();
        }
    }
}

// ================================
// UTILITIES
// ================================

function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this item?');
}