// Check if user needs to set username (passed from controller)
const needsUsername = document.body.dataset.needsUsername === 'true';

if (needsUsername) {
    document.getElementById('usernameModal').classList.add('show');
}

const usernameInput = document.getElementById('usernameInput');
const feedback = document.getElementById('usernameFeedback');
const submitBtn = document.getElementById('submitBtn');
let checkTimeout = null;

if (usernameInput) {
    usernameInput.addEventListener('input', function() {
        const value = this.value.trim();
        
        // Clear previous timeout
        clearTimeout(checkTimeout);
        
        if (value.length < 3) {
            feedback.textContent = 'Mínimo 3 caracteres';
            feedback.className = 'feedback unavailable';
            submitBtn.disabled = true;
            return;
        }
        
        // Check with server after 500ms delay
        checkTimeout = setTimeout(() => {
            fetch('/auth/check-username?username=' + encodeURIComponent(value))
                .then(res => res.json())
                .then(data => {
                    if (data.available) {
                        feedback.textContent = '✓ Username disponível!';
                        feedback.className = 'feedback available';
                        submitBtn.disabled = false;
                    } else {
                        feedback.textContent = '✗ Username já está em uso';
                        feedback.className = 'feedback unavailable';
                        submitBtn.disabled = true;
                    }
                })
                .catch(err => {
                    feedback.textContent = 'Erro ao verificar';
                    feedback.className = 'feedback unavailable';
                    submitBtn.disabled = true;
                });
        }, 500);
    });
}

// Tab switching functionality
function switchTab(tabName) {
    // Remove active class from all tabs and tab contents
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));
    
    // Add active class to selected tab
    event.target.classList.add('active');
    document.getElementById(tabName + '-tab').classList.add('active');
}

// Hamburger menu functionality for movies page
document.addEventListener('DOMContentLoaded', function() {
    const hamburger = document.querySelector('.hamburger');
    const navbarMenu = document.querySelector('.navbar-menu');
    const overlay = document.querySelector('.navbar-overlay');
    
    if (hamburger && navbarMenu && overlay) {
        hamburger.addEventListener('click', function() {
            hamburger.classList.toggle('active');
            navbarMenu.classList.toggle('active');
            overlay.classList.toggle('active');
            
            // Prevent body scroll when menu is open
            if (navbarMenu.classList.contains('active')) {
                document.body.style.overflow = 'hidden';
            } else {
                document.body.style.overflow = '';
            }
        });
        
        // Close menu when clicking overlay
        overlay.addEventListener('click', function() {
            hamburger.classList.remove('active');
            navbarMenu.classList.remove('active');
            overlay.classList.remove('active');
            document.body.style.overflow = '';
        });
        
        // Close menu when clicking menu links
        const menuLinks = navbarMenu.querySelectorAll('a');
        menuLinks.forEach(link => {
            link.addEventListener('click', function() {
                hamburger.classList.remove('active');
                navbarMenu.classList.remove('active');
                overlay.classList.remove('active');
                document.body.style.overflow = '';
            });
        });
    }
});
