let autoScrollInterval;
const container = document.getElementById('carruselTestimonios');

function scrollTestimonios(direction) {
    if (container) {
        const cardWidth = container.querySelector('div.snap-center').offsetWidth;
        const gap = 24;
        const scrollAmount = cardWidth + gap;

        container.scrollBy({
            left: direction * scrollAmount,
            behavior: 'smooth'
        });
    }
}

function startAutoScroll() {
    if (autoScrollInterval) clearInterval(autoScrollInterval);

    autoScrollInterval = setInterval(() => {
        if (container) {
            const maxScrollLeft = container.scrollWidth - container.clientWidth;

            if (container.scrollLeft >= maxScrollLeft - 10) {
                container.scrollTo({ left: 0, behavior: 'smooth' });
            } else {
                scrollTestimonios(1);
            }
        }
    }, 3000);
}

function stopAutoScroll() {
    clearInterval(autoScrollInterval);
}

document.addEventListener('DOMContentLoaded', () => {
    startAutoScroll();
    if (container) {
        container.addEventListener('mouseenter', stopAutoScroll);
        container.addEventListener('mouseleave', startAutoScroll);
        container.addEventListener('touchstart', stopAutoScroll);
        container.addEventListener('touchend', startAutoScroll);
    }
});


function setRating(stars) {
    const ratingInput = document.getElementById('ratingValue');
    if (ratingInput) {
        ratingInput.value = stars;
    }

    const buttons = document.querySelectorAll('.star-btn span');

    buttons.forEach((btn, index) => {
        if (index < stars) {
            btn.style.fontVariationSettings = "'FILL' 1";
            btn.parentElement.classList.remove('text-gray-300');
            btn.parentElement.classList.add('text-yellow-400');
        } else {
            btn.style.fontVariationSettings = "'FILL' 0";
            btn.parentElement.classList.add('text-gray-300');
            btn.parentElement.classList.remove('text-yellow-400');
        }
    });
}