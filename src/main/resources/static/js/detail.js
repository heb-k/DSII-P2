// Star rating JS: supports half-star selection by click position
(function(){
    const container = document.getElementById('starRating');
    if(!container) return;
    const stars = Array.from(container.querySelectorAll('.star-clickable'));
    const input = document.getElementById('ratingInput');

    function updateDisplay(value) {
        // value is number like 3, 3.5, etc.
        stars.forEach(s => {
            const v = Number(s.getAttribute('data-value'));
            if (value >= v) {
                s.classList.add('full'); s.classList.remove('half','empty');
            } else if (value >= v - 0.5) {
                s.classList.add('half'); s.classList.remove('full','empty');
            } else {
                s.classList.add('empty'); s.classList.remove('full','half');
            }
        });
    }

    stars.forEach(s => {
        s.addEventListener('mousemove', (ev) => {
            // visual hover preview (not setting value)
            const rect = s.getBoundingClientRect();
            const relX = ev.clientX - rect.left;
            const isLeft = relX < rect.width/2;
            const preview = Number(s.dataset.value) - (isLeft ? 0.5 : 0);
            updateDisplay(preview);
        });
        s.addEventListener('mouseleave', () => {
            // restore to current input value
            const val = parseFloat(input.value) || 0;
            updateDisplay(val);
        });
        s.addEventListener('click', (ev) => {
            const rect = s.getBoundingClientRect();
            const relX = ev.clientX - rect.left;
            const isLeft = relX < rect.width/2;
            const newVal = Number(s.dataset.value) - (isLeft ? 0.5 : 0);
            input.value = newVal;
            updateDisplay(newVal);
        });
    });

    // initialize to 0
    input.value = '';
    updateDisplay(0);
})();

// Validação do formulário de review
const reviewForm = document.getElementById('reviewForm');
if (reviewForm) {
    reviewForm.addEventListener('submit', function(e) {
        const ratingInput = document.getElementById('ratingInput');
        const ratingError = document.getElementById('ratingError');
    
        if (!ratingInput.value || parseFloat(ratingInput.value) === 0) {
            e.preventDefault();
            ratingError.style.display = 'block';
            document.getElementById('starRating').scrollIntoView({ behavior: 'smooth', block: 'center' });
            return false;
        }
        ratingError.style.display = 'none';
    });
}

// Edit Review Functions
function editReview(reviewId) {
    // Get the edit button to read data attributes
    const editButton = document.querySelector(`button[data-review-id="${reviewId}"]`);
    const currentComment = editButton.getAttribute('data-comment');
    const currentRating = parseFloat(editButton.getAttribute('data-rating'));
    
    // Hide the comment display
    const reviewCard = document.querySelector(`[data-review-id="${reviewId}"]`);
    const commentDisplay = reviewCard.querySelector('.review-comment');
    commentDisplay.style.display = 'none';
    
    // Show the edit form
    const editForm = document.getElementById(`edit-form-${reviewId}`);
    editForm.style.display = 'block';
    
    // Set the current values
    const commentTextarea = document.getElementById(`comment-edit-${reviewId}`);
    commentTextarea.value = currentComment;
    
    // Initialize star rating for edit mode
    initEditStarRating(reviewId, currentRating);
}

function cancelEdit(reviewId) {
    // Show the comment display
    const reviewCard = document.querySelector(`[data-review-id="${reviewId}"]`);
    const commentDisplay = reviewCard.querySelector('.review-comment');
    commentDisplay.style.display = 'block';
    
    // Hide the edit form
    const editForm = document.getElementById(`edit-form-${reviewId}`);
    editForm.style.display = 'none';
}

function deleteReview(reviewId) {
    if (confirm('Tem certeza que deseja excluir esta review?')) {
        const form = document.getElementById(`delete-form-${reviewId}`);
        form.submit();
    }
}

function initEditStarRating(reviewId, initialRating) {
    const container = document.getElementById(`star-rating-edit-${reviewId}`);
    if (!container) return;
    
    const stars = Array.from(container.querySelectorAll('.star-clickable'));
    const input = document.getElementById(`rating-edit-${reviewId}`);
    
    function updateDisplay(value) {
        stars.forEach(s => {
            const v = Number(s.getAttribute('data-value'));
            if (value >= v) {
                s.classList.add('full'); s.classList.remove('half','empty');
            } else if (value >= v - 0.5) {
                s.classList.add('half'); s.classList.remove('full','empty');
            } else {
                s.classList.add('empty'); s.classList.remove('full','half');
            }
        });
    }
    
    stars.forEach(s => {
        s.addEventListener('mousemove', (ev) => {
            const rect = s.getBoundingClientRect();
            const relX = ev.clientX - rect.left;
            const isLeft = relX < rect.width/2;
            const preview = Number(s.dataset.value) - (isLeft ? 0.5 : 0);
            updateDisplay(preview);
        });
        s.addEventListener('mouseleave', () => {
            const val = parseFloat(input.value) || 0;
            updateDisplay(val);
        });
        s.addEventListener('click', (ev) => {
            const rect = s.getBoundingClientRect();
            const relX = ev.clientX - rect.left;
            const isLeft = relX < rect.width/2;
            const newVal = Number(s.dataset.value) - (isLeft ? 0.5 : 0);
            input.value = newVal;
            updateDisplay(newVal);
        });
    });
    
    // Set initial value
    input.value = initialRating;
    updateDisplay(initialRating);
}
