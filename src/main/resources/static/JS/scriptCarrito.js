const stripe = Stripe('pk_test_51SbZpBBao9PItqILSgXaIxO5QvvPDzx7HVeyCMQ0jhmlol7SHGQFywedxsrsTH3LwfQujOUsMvSIoDFJM7L5FSSt00BSIemTt0');
let elements;

let carrito = JSON.parse(localStorage.getItem('cart_huellitas')) || [];

function agregarAlCarrito(id, nombre, precio) {
    if (!nombre || nombre === "null") nombre = "Producto";
    const existente = carrito.find(item => item.productoId === id);
    if (existente) existente.cantidad++;
    else carrito.push({ productoId: id, nombre: nombre, precio: parseFloat(precio), cantidad: 1 });

    actualizarStorage();
    if (typeof Toast !== 'undefined') Toast.success(`Añadido: ${nombre}`, "Carrito");
    else alert("Agregado");
}

function actualizarStorage() {
    localStorage.setItem('cart_huellitas', JSON.stringify(carrito));
    renderizarCarrito();
}

function renderizarCarrito() {
    const container = document.getElementById('carritoItems');
    const contador = document.getElementById('cartCount');
    const totalElem = document.getElementById('carritoTotal');
    if (!container) return;

    contador.innerText = carrito.reduce((acc, item) => acc + item.cantidad, 0);

    if (carrito.length === 0) {
        container.innerHTML = '<div class="text-center py-10"><span class="material-symbols-outlined text-4xl text-gray-300">shopping_cart</span><p class="text-gray-500 mt-2">Vacío</p></div>';
        totalElem.innerText = 'S/ 0.00';
        return;
    }

    let html = '';
    let total = 0;
    carrito.forEach((item, index) => {
        let subtotal = item.precio * item.cantidad;
        total += subtotal;
        html += `
            <div class="flex items-center justify-between bg-white p-3 rounded-lg border border-gray-100 shadow-sm">
                <div><h5 class="font-bold text-gray-800 text-sm">${item.nombre}</h5><p class="text-xs text-gray-500">S/ ${item.precio} x ${item.cantidad}</p></div>
                <div class="flex items-center gap-3"><span class="font-bold text-primary text-sm">S/ ${subtotal.toFixed(2)}</span><button onclick="eliminarDelCarrito(${index})" class="text-red-400 hover:text-red-600"><span class="material-symbols-outlined text-sm">delete</span></button></div>
            </div>`;
    });
    container.innerHTML = html;
    totalElem.innerText = 'S/ ' + total.toFixed(2);
}

function eliminarDelCarrito(index) {
    carrito.splice(index, 1);
    actualizarStorage();
}

function toggleCarrito() {
    const modal = document.getElementById('carritoModal');
    const panel = document.getElementById('carritoPanel');
    if (modal.classList.contains('hidden')) {
        modal.classList.remove('hidden');
        setTimeout(() => panel.classList.remove('translate-x-full'), 10);
    } else {
        panel.classList.add('translate-x-full');
        setTimeout(() => modal.classList.add('hidden'), 300);
    }
}

// --- APERTURA Y GESTIÓN DE PAGO ---

function finalizarCompra() {
    if (carrito.length === 0) {
        if (typeof Toast !== 'undefined') Toast.warning("El carrito está vacío");
        return;
    }
    document.getElementById('pagoTotalDisplay').innerText = document.getElementById('carritoTotal').innerText;
    toggleCarrito();
    document.getElementById('pagoModal').classList.remove('hidden');
    document.getElementById('pagoModal').classList.add('flex');

    // Resetear formulario
    document.getElementById('metodoPago').value = "YAPE";
    cambiarMetodoPago();
}

function cerrarPago() {
    document.getElementById('pagoModal').classList.add('hidden');
    document.getElementById('pagoModal').classList.remove('flex');
}

// 1. Lógica para cambiar entre Yape y Stripe
function cambiarMetodoPago() {
    const metodo = document.getElementById('metodoPago').value;
    const btn = document.getElementById('btnConfirmarPago');

    if (metodo === 'TARJETA') {
        document.getElementById('seccionYape').classList.add('hidden');
        document.getElementById('seccionStripe').classList.remove('hidden');
        btn.innerText = "Pagar con Tarjeta";
        iniciarStripe(); // Cargar formulario seguro
    } else {
        document.getElementById('seccionYape').classList.remove('hidden');
        document.getElementById('seccionStripe').classList.add('hidden');
        btn.innerText = "Confirmar Pago";
    }
}


// 2. Iniciar Stripe (Pedir Token al Backend)
async function iniciarStripe() {
    const totalTexto = document.getElementById('carritoTotal').innerText.replace('S/ ', '').trim();
    const total = parseFloat(totalTexto);

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const headers = {
        "Content-Type": "application/json"
    };

    if (csrfToken && csrfHeader) {
        headers[csrfHeader] = csrfToken;
    }

    try {
        const response = await fetch("/api/pagos/crear-intento", {
            method: "POST",
            headers: headers,
            body: JSON.stringify({ amount: total }),
        });

        if (!response.ok) {
            throw new Error(`Error del servidor: ${response.status}`);
        }

        const data = await response.json();
        const clientSecret = data.clientSecret;

        // Configurar Stripe Elements
        const appearance = { theme: 'stripe' };
        elements = stripe.elements({ appearance, clientSecret });

        const paymentElement = elements.create("payment", { layout: "tabs" });
        paymentElement.mount("#payment-element");

    } catch (error) {
        console.error("Error iniciando Stripe:", error);
        document.getElementById('stripe-error').innerText = "Error de conexión con el banco. Recarga la página.";
        document.getElementById('stripe-error').classList.remove('hidden');
    }
}

// 3. Controlador Principal del Botón "Confirmar"
async function procesarPagoGeneral() {
    const metodo = document.getElementById('metodoPago').value;
    const btn = document.getElementById('btnConfirmarPago');

    btn.disabled = true;
    btn.innerText = "Procesando...";

    if (metodo === 'TARJETA') {
        // --- FLUJO STRIPE ---
        const { error } = await stripe.confirmPayment({
            elements,
            redirect: "if_required"
        });

        if (error) {
            console.error("Error Stripe:", error);
            const msgError = document.getElementById('stripe-error');
            msgError.innerText = error.message;
            msgError.classList.remove('hidden');

            btn.disabled = false;
            btn.innerText = "Pagar con Tarjeta";
        } else {
            guardarVentaEnBD("TARJETA", "STRIPE_AUTO", "COMPLETADA");
        }

    } else {
        // --- FLUJO YAPE / PLIN ---
        const codigo = document.getElementById('codigoOperacion').value;
        if (!codigo) {
            if (typeof Toast !== 'undefined') Toast.warning("Ingresa el N° de Operación");
            btn.disabled = false;
            btn.innerText = "Confirmar Pago";
            return;
        }
        guardarVentaEnBD(metodo, codigo, "POR_VALIDAR");
    }
}

// 4. Guardar Venta en Backend (Java)
async function guardarVentaEnBD(metodo, codigo, estado) {
    const compraData = {
        items: carrito,
        metodoPago: metodo,
        codigoOperacion: codigo,
        estado: estado // Stripe = COMPLETADA, Yape = POR_VALIDAR
    };

    // Obtener Token CSRF de Spring Security
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    try {
        const response = await fetch('/carrito/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(compraData)
        });

        if (response.ok) {
            if (typeof Toast !== 'undefined') Toast.success("Compra registrada correctamente", "¡Éxito!");

            carrito = [];
            actualizarStorage();
            cerrarPago();

            setTimeout(() => window.location.reload(), 2000);
        } else {
            const data = await response.json();
            if (typeof Toast !== 'undefined') Toast.error(data.error || "Error al guardar");
        }
    } catch (e) {
        console.error(e);
        if (typeof Toast !== 'undefined') Toast.error("Error de conexión");
    } finally {
        const btn = document.getElementById('btnConfirmarPago');
        btn.disabled = false;
        btn.innerText = "Confirmar Pago";
    }
}

renderizarCarrito();