
function abrirReserva(idServicio, nombreServicio) {
    const inputId = document.getElementById('reservaServicioId');
    const inputNombre = document.getElementById('reservaServicioNombre');

    if (inputId) inputId.value = idServicio;
    if (inputNombre) inputNombre.value = nombreServicio;

    openModal('reservaModal');
}

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('hidden');
        modal.classList.add('flex');
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }
}

function abrirCompra(id, nombre, precio) {
    document.getElementById('compraProductoId').value = id;
    document.getElementById('compraNombreProducto').innerText = nombre;
    document.getElementById('compraPrecio').innerText = 'S/ ' + precio;
    openModal('compraModal');
}
