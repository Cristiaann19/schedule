const Toast = {
    container: null,

    init: function () {
        if (!document.getElementById('toast-container')) {
            this.container = document.createElement('div');
            this.container.id = 'toast-container';
            this.container.className = "fixed bottom-5 right-5 z-[100] flex flex-col gap-3 pointer-events-none"; // Apilables
            document.body.appendChild(this.container);
        } else {
            this.container = document.getElementById('toast-container');
        }

        this.checkUrlParams();
    },

    fire: function (data) {
        if (!this.container) this.init();

        const title = data.title || (data.icon === 'success' ? '¡Éxito!' : 'Atención');
        const message = data.text || '';
        const type = data.icon || 'info';
        const duration = data.timer || 4000;
        const styles = {
            success: { bg: 'bg-green-100', text: 'text-green-800', iconColor: 'text-green-500', icon: 'check_circle' },
            error: { bg: 'bg-red-100', text: 'text-red-800', iconColor: 'text-red-500', icon: 'error' },
            warning: { bg: 'bg-yellow-100', text: 'text-yellow-800', iconColor: 'text-yellow-600', icon: 'warning' },
            info: { bg: 'bg-blue-100', text: 'text-blue-800', iconColor: 'text-blue-500', icon: 'info' }
        };

        const style = styles[type] || styles.info;

        const toastEl = document.createElement('div');
        toastEl.className = `pointer-events-auto flex items-center w-full max-w-xs p-4 rounded-lg shadow-lg border border-gray-200 bg-white transform transition-all duration-300 translate-y-10 opacity-0`;

        toastEl.innerHTML = `
            <div class="inline-flex items-center justify-center flex-shrink-0 w-8 h-8 rounded-lg ${style.bg} ${style.iconColor}">
                <span class="material-symbols-outlined text-sm">${style.icon}</span>
            </div>
            <div class="ml-3">
                <h4 class="text-sm font-bold text-gray-900">${title}</h4>
                <div class="text-xs text-gray-500">${message}</div>
            </div>
            <button type="button" class="ml-auto -mx-1.5 -my-1.5 bg-white text-gray-400 hover:text-gray-900 rounded-lg p-1.5 hover:bg-gray-100 inline-flex items-center justify-center h-8 w-8">
                <span class="material-symbols-outlined text-lg">close</span>
            </button>
        `;

        this.container.appendChild(toastEl);

        requestAnimationFrame(() => {
            toastEl.classList.remove('translate-y-10', 'opacity-0');
        });

        const closeBtn = toastEl.querySelector('button');
        closeBtn.onclick = () => this.close(toastEl);
        setTimeout(() => {
            this.close(toastEl);
        }, duration);
    },

    close: function (element) {
        element.classList.add('opacity-0', 'translate-x-full');
        setTimeout(() => {
            if (element.parentElement) element.remove();
        }, 300);
    },
    success: function (msg, title) { this.fire({ icon: 'success', text: msg, title: title }); },
    error: function (msg, title) { this.fire({ icon: 'error', text: msg, title: title }); },
    warning: function (msg, title) { this.fire({ icon: 'warning', text: msg, title: title }); },
    info: function (msg, title) { this.fire({ icon: 'info', text: msg, title: title }); },

    checkUrlParams: function () {
        const urlParams = new URLSearchParams(window.location.search);
        let cleaned = false;

        if (urlParams.has('success')) {
            let msg = urlParams.get('success');
            if (msg === 'true' || !msg) msg = "Operación realizada con éxito";
            this.success(decodeURIComponent(msg.replace(/\+/g, ' ')));
            cleaned = true;
        }

        if (urlParams.has('error')) {
            let msg = urlParams.get('error');
            this.error(decodeURIComponent(msg.replace(/\+/g, ' ')));
            cleaned = true;
        }

        if (urlParams.has('reservada') || urlParams.has('compraExito') || urlParams.has('contactoExito')) {
            this.success("Operación realizada con éxito");
            cleaned = true;
        }

        if (cleaned) {
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Toast.init();
});
function mostrarToast(msg, type) {
    if (type === 'success') Toast.success(msg);
    else Toast.error(msg);
}