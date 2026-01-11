    let currentStep = 1;
    const EMAIL_DOMAIN = "@masi.ac.ma"; // keep consistent with UI

    function showStep(step) {
    document.querySelectorAll('.step').forEach(s => s.classList.remove('active'));
    document.getElementById('step-' + step).classList.add('active');

    for (let i = 1; i <= 3; i++) {
    const indicator = document.getElementById('indicator-' + i);
    indicator.classList.remove('completed', 'active');
    indicator.classList.add('border-[#E8E8E8]', 'text-[#666]');

    if (i < step) {
    indicator.classList.add('completed');
    indicator.classList.remove('border-[#E8E8E8]', 'text-[#666]');
    indicator.innerHTML = '<i class="fas fa-check text-xs"></i>';
} else if (i === step) {
    indicator.classList.add('active');
    indicator.classList.remove('border-[#E8E8E8]', 'text-[#666]');
    indicator.innerHTML = i;
} else {
    indicator.innerHTML = i;
}
}
}

    function nextStep(current) {
    if (current === 1) {
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    if (!firstName || !lastName) {
    alert('Please fill in both first name and last name');
    return;
}
} else if (current === 2) {
    const emailPrefix = document.getElementById('emailPrefix').value.trim();
    if (!emailPrefix) {
    alert('Please enter your email');
    return;
}
    document.getElementById('email').value = emailPrefix + EMAIL_DOMAIN;

    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    document.getElementById('phone').value = phoneNumber ? '+212' + phoneNumber : '';
}

    currentStep = current + 1;
    showStep(currentStep);
}

    function prevStep(current) {
    currentStep = current - 1;
    showStep(currentStep);
}

    document.getElementById('registerForm').addEventListener('submit', function(e) {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
    e.preventDefault();
    document.getElementById('passwordError').classList.remove('hidden');
    return false;
}

    const emailPrefix = document.getElementById('emailPrefix').value.trim();
    document.getElementById('email').value = emailPrefix + EMAIL_DOMAIN;

    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    document.getElementById('phone').value = phoneNumber ? '+212' + phoneNumber : '';
});

    document.getElementById('confirmPassword').addEventListener('input', function() {
    document.getElementById('passwordError').classList.add('hidden');
});
