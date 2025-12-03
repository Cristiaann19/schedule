let carrito = JSON.parse(localStorage.getItem('cart_huellitas')) || [];

function agregarAlCarrito(id, nombre, precio) {
    if (!nombre || nombre === "null") nombre = "Producto";

    const existente = carrito.find(item => item.productoId === id);

    if (existente) {
        existente.cantidad++;
    } else {
        carrito.push({ productoId: id, nombre: nombre, precio: parseFloat(precio), cantidad: 1 });
    }

    actualizarStorage();

    if (typeof Toast !== 'undefined') {
        Toast.success(`Se añadió al carrito: ${nombre}`, "¡Agregado!");
    } else {
        alert("Agregado al carrito");
    }
}

function actualizarStorage() {
    localStorage.setItem('cart_huellitas', JSON.stringify(carrito));
    renderizarCarrito();
}

function renderizarCarrito() {
    const container = document.getElementById('carritoItems');
    const contador = document.getElementById('cartCount');
    const totalElem = document.getElementById('carritoTotal');

    if (!container || !contador || !totalElem) return;

    contador.innerText = carrito.reduce((acc, item) => acc + item.cantidad, 0);

    if (carrito.length === 0) {
        container.innerHTML = '<div class="text-center py-10"><span class="material-symbols-outlined text-4xl text-gray-300">production_quantity_limits</span><p class="text-gray-500 mt-2">Carrito vacío</p></div>';
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
                <div>
                    <h5 class="font-bold text-gray-800 text-sm">${item.nombre}</h5>
                    <p class="text-xs text-gray-500">S/ ${item.precio} x ${item.cantidad}</p>
                </div>
                <div class="flex items-center gap-3">
                    <span class="font-bold text-primary text-sm">S/ ${subtotal.toFixed(2)}</span>
                    <button onclick="eliminarDelCarrito(${index})" class="text-red-400 hover:text-red-600">
                        <span class="material-symbols-outlined text-sm">delete</span>
                    </button>
                </div>
            </div>
        `;
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
        setTimeout(() => { panel.classList.remove('translate-x-full'); }, 10);
    } else {
        panel.classList.add('translate-x-full');
        setTimeout(() => { modal.classList.add('hidden'); }, 300);
    }
}

function finalizarCompra() {
    if (carrito.length === 0) {
        if (typeof Toast !== 'undefined') Toast.warning("El carrito está vacío");
        else alert("El carrito está vacío");
        return;
    }

    document.getElementById('pagoTotalDisplay').innerText = document.getElementById('carritoTotal').innerText;
    toggleCarrito();
    document.getElementById('pagoModal').classList.remove('hidden');
    document.getElementById('pagoModal').classList.add('flex');
}

function cerrarPago() {
    document.getElementById('pagoModal').classList.add('hidden');
    document.getElementById('pagoModal').classList.remove('flex');
}

async function enviarPedido() {
    const metodo = document.getElementById('metodoPago').value;
    const codigo = document.getElementById('codigoOperacion').value;

    if (!codigo) {
        if (typeof Toast !== 'undefined') Toast.warning("Ingresa el número de operación");
        else alert("Ingresa el número de operación");
        return;
    }

    const btnConfirmar = document.querySelector('#formPago button[type="submit"]');
    const textoOriginal = btnConfirmar.innerText;
    btnConfirmar.disabled = true;
    btnConfirmar.innerText = "Procesando...";

    const compraData = {
        items: carrito,
        metodoPago: metodo,
        codigoOperacion: codigo
    };

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
            if (typeof Toast !== 'undefined') {
                Toast.success("¡Pago registrado! Validaremos tu operación.", "Compra Exitosa");
            } else {
                alert("¡Pago registrado correctamente!");
            }

            carrito = [];
            actualizarStorage();
            cerrarPago();

            setTimeout(() => {
                window.location.reload();
            }, 2000);

        } else {
            const errorData = await response.json();
            if (typeof Toast !== 'undefined') Toast.error(errorData.error || "Error al procesar");
            else alert("Error: " + errorData.error);
        }
    } catch (error) {
        console.error(error);
        if (typeof Toast !== 'undefined') Toast.error("Error de conexión con el servidor");
        else alert("Error de conexión");
    } finally {
        btnConfirmar.disabled = false;
        btnConfirmar.innerText = textoOriginal;
    }
}

renderizarCarrito();