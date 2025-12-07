
function showSection(sectionId, linkElement) {
    localStorage.setItem('activeSectionId', sectionId);

    const sections = document.querySelectorAll('.content-section');
    sections.forEach(section => {
        section.classList.add('hidden-section');
    });

    const targetSection = document.getElementById('section-' + sectionId);
    if (targetSection) {
        targetSection.classList.remove('hidden-section');
    } else {
        const genericSection = document.getElementById('section-generic');
        if (genericSection) {
            genericSection.classList.remove('hidden-section');
        }
    }

    const titles = {
        'dashboard': 'Dashboard',
        'citas': 'Gestión de Citas',
        'servicios': 'Catálogo de Servicios',
        'productos': 'Inventario de Productos',
        'ventas': 'Historial de Ventas',
        'pagos': 'Historial de Pagos',
        'clientes': 'Base de Datos de Clientes',
        'trabajadores': 'Gestión de Trabajadores',
        'vacunas': 'Gestión de Vacunas',
        'mascotas': 'Gestión de Mascotas',
        'enfermedades': 'Gestión de Enfermedades',
        'slider': 'Gestión de Slider'
    };
    document.getElementById('page-title').innerText = titles[sectionId] || 'Huellitas Panel';

    const links = document.querySelectorAll('.nav-link');
    links.forEach(link => {
        if (!link.classList.contains('text-red-400')) {
            link.className = 'nav-link flex items-center gap-3 px-3 py-2.5 rounded-lg text-gray-300 hover:bg-gray-700 hover:text-white transition-colors';
        }
    });

    if (linkElement && !linkElement.classList.contains('text-red-400')) {
        linkElement.className = 'nav-link flex items-center gap-3 px-3 py-2.5 rounded-lg bg-primary text-white font-medium transition-colors shadow-sm';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const storedSectionId = localStorage.getItem('activeSectionId') || 'dashboard';
    const linkToActivate = document.querySelector(`.nav-link[onclick*="'${storedSectionId}'"]`);
    showSection(storedSectionId, linkToActivate);
});
