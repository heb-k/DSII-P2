// Function to switch between tabs
function showTab(tabName, buttonElement) {
    // Hide all tab contents
    const contents = document.querySelectorAll('.tab-content');
    contents.forEach(content => content.classList.remove('active'));
    
    // Remove active class from all buttons
    const buttons = document.querySelectorAll('.tab-btn');
    buttons.forEach(btn => btn.classList.remove('active'));
    
    // Show selected tab content
    document.getElementById(tabName).classList.add('active');
    
    // Add active class to clicked button (or find by tab name if no button provided)
    if (buttonElement) {
        buttonElement.classList.add('active');
    } else {
        // Find button by data-tab attribute
        const targetButton = document.querySelector(`[data-tab="${tabName}"]`);
        if (targetButton) {
            targetButton.classList.add('active');
        }
    }
}

// Check URL parameters on page load to pre-select tab
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const tabParam = urlParams.get('tab');
    
    if (tabParam === 'following') {
        showTab('following');
    } else if (tabParam === 'followers') {
        showTab('followers');
    }
    // If no param or invalid, default 'followers' is already active from HTML
});
