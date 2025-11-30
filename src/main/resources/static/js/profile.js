// profile.js - username change & account delete modals

document.addEventListener('DOMContentLoaded', () => {
  const openUsernameBtn = document.getElementById('openUsernameModal');
  const usernameModal = document.getElementById('usernameChangeModal');
  const closeUsernameBtn = document.getElementById('closeUsernameModal');
  const newUsernameInput = document.getElementById('newUsername');
  const feedback = document.getElementById('changeUsernameFeedback');
  const confirmUsernameBtn = document.getElementById('confirmUsernameBtn');

  const openDeleteBtn = document.getElementById('openDeleteModal');
  const deleteModal = document.getElementById('deleteAccountModal');
  const closeDeleteBtn = document.getElementById('closeDeleteModal');

  let checkTimeout = null;

  function toggle(modal, show) {
    if (!modal) return;
    modal.classList[show ? 'add' : 'remove']('show');
    document.body.style.overflow = show ? 'hidden' : '';
  }

  if (openUsernameBtn && usernameModal) {
    openUsernameBtn.addEventListener('click', () => toggle(usernameModal, true));
  }
  if (closeUsernameBtn && usernameModal) {
    closeUsernameBtn.addEventListener('click', () => toggle(usernameModal, false));
  }
  if (openDeleteBtn && deleteModal) {
    openDeleteBtn.addEventListener('click', () => toggle(deleteModal, true));
  }
  if (closeDeleteBtn && deleteModal) {
    closeDeleteBtn.addEventListener('click', () => toggle(deleteModal, false));
  }

  // Outside click to close
  [usernameModal, deleteModal].forEach(modal => {
    if (!modal) return;
    modal.addEventListener('click', (e) => {
      if (e.target === modal) toggle(modal, false);
    });
  });

  // Real-time username availability
  if (newUsernameInput) {
    newUsernameInput.addEventListener('input', function() {
      const value = this.value.trim();
      clearTimeout(checkTimeout);

      if (value.length < 3) {
        feedback.textContent = 'Mínimo 3 caracteres';
        feedback.className = 'feedback unavailable';
        confirmUsernameBtn.disabled = true;
        return;
      }
      if (!/^[a-zA-Z0-9_]+$/.test(value)) {
        feedback.textContent = 'Caracteres inválidos';
        feedback.className = 'feedback unavailable';
        confirmUsernameBtn.disabled = true;
        return;
      }

      checkTimeout = setTimeout(() => {
        fetch('/users/check-username?username=' + encodeURIComponent(value))
          .then(r => r.json())
          .then(data => {
            if (data.available) {
              feedback.textContent = '✓ Disponível';
              feedback.className = 'feedback available';
              confirmUsernameBtn.disabled = false;
            } else {
              feedback.textContent = '✗ Já em uso';
              feedback.className = 'feedback unavailable';
              confirmUsernameBtn.disabled = true;
            }
          })
          .catch(() => {
            feedback.textContent = 'Erro ao verificar';
            feedback.className = 'feedback unavailable';
            confirmUsernameBtn.disabled = true;
          });
      }, 500);
    });
  }
});
