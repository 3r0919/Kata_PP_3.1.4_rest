window.onload = function () {
    const firstTabPill = document.querySelector('.ddpills');
    const firstTab = document.querySelector('.ddtabs');

    if (firstTabPill && firstTab) {
        firstTabPill.classList.add('active');
        firstTab.classList.add('active', 'show');
    } else {
        console.error('First tab pill or tab not found');
    }
};
