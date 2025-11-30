// Navbar hamburger menu functionality
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
        
        // Close menu when clicking a menu link
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
