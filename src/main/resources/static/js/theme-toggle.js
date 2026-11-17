/**
 * LIVETECH AUCTION - SCRIPT ĐIỀU KHIỂN GIAO DIỆN SÁNG / TỐI (LIGHT/DARK MODE)
 * Tự động ghi nhớ lựa chọn của người dùng vào localStorage và đồng bộ data-bs-theme
 */

// 1. ÁP DỤNG THEME NGAY KHI FILE ĐƯỢC LOAD (CHỐNG NHÁY MÀN HÌNH FOUC)
(function initTheme() {
    const savedTheme = localStorage.getItem('livetech-theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.setAttribute('data-bs-theme', 'dark');
    } else {
        document.documentElement.setAttribute('data-bs-theme', 'light');
    }
})();

// 2. HÀM CHUYỂN ĐỔI THEME KHI CLICK VÀO NÚT TOGGLE
function toggleTheme() {
    const htmlEl = document.documentElement;
    const currentTheme = htmlEl.getAttribute('data-bs-theme') || 'light';
    const targetTheme = currentTheme === 'light' ? 'dark' : 'light';

    htmlEl.setAttribute('data-bs-theme', targetTheme);
    localStorage.setItem('livetech-theme', targetTheme);
    updateThemeToggleUI(targetTheme);
}

// 3. ĐỒNG BỘ ICON VÀ LABEL TRÊN NÚT TOGGLE
function updateThemeToggleUI(theme) {
    const icon = document.getElementById('themeToggleIcon');
    const label = document.getElementById('themeToggleLabel');
    if (!icon || !label) return;

    if (theme === 'dark') {
        icon.className = 'fa-solid fa-sun text-warning';
        label.textContent = 'Giao diện Sáng';
    } else {
        icon.className = 'fa-solid fa-moon';
        label.textContent = 'Giao diện Tối';
    }
}

// Lắng nghe sự kiện khi DOM đã sẵn sàng
document.addEventListener('DOMContentLoaded', () => {
    const currentTheme = document.documentElement.getAttribute('data-bs-theme') || 'light';
    updateThemeToggleUI(currentTheme);
});

// 4. TIỆN ÍCH SAO CHÉP VĂN BẢN VÀO BỘ NHỚ TẠM (CLIPBOARD)
function copyToClipboard(elementId) {
    const el = document.getElementById(elementId);
    if (!el) return;
    const text = el.innerText.trim();
    navigator.clipboard.writeText(text).then(() => {
        const toastEl = document.getElementById('copyToast');
        if (toastEl && window.bootstrap && window.bootstrap.Toast) {
            const toast = new bootstrap.Toast(toastEl, { delay: 2000 });
            toast.show();
        } else {
            alert('Đã sao chép: ' + text);
        }
    }).catch(err => {
        console.error('Không thể sao chép: ', err);
    });
}
