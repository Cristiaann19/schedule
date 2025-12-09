let currentSlide = 0;
const slides = document.querySelectorAll('[data-slider-img]');
const dots = document.querySelectorAll('[id^="dot-"]');

function mostrarSlide(index) {
    slides.forEach(s => s.classList.remove('opacity-100'));
    slides.forEach(s => s.classList.add('opacity-0'));

    slides[index].classList.remove('opacity-0');
    slides[index].classList.add('opacity-100');

    // Actualizar puntos
    dots.forEach(d => d.classList.remove('bg-white'));
    dots.forEach(d => d.classList.add('bg-white/50'));
    if (dots[index]) {
        dots[index].classList.remove('bg-white/50');
        dots[index].classList.add('bg-white');
    }
}

function siguienteSlide() {
    currentSlide = (currentSlide + 1) % slides.length;
    mostrarSlide(currentSlide);
}

// Cambio automático cada 4 segundos
setInterval(siguienteSlide, 4000);