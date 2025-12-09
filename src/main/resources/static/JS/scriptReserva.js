function abrirReserva(idServicio, nombreServicio) {
    const inputId = document.getElementById('reservaServicioId');
    const inputNombre = document.getElementById('reservaServicioNombre');

    if (inputId) inputId.value = idServicio;
    if (inputNombre) inputNombre.value = nombreServicio;

    const selectVet = document.getElementById('reservaVeterinario');

    selectVet.innerHTML = '<option value="">Cualquiera (Asignación Automática)</option>';

    let especialidadRequerida = "Veterinario";

    if (nombreServicio) {
        const nombreLower = nombreServicio.toLowerCase();

        if (nombreLower.includes('baño') || nombreLower.includes('corte') || nombreLower.includes('grooming')) {
            especialidadRequerida = "Estilista";
        }
    }

    if (typeof listaTrabajadoresPublico !== 'undefined' && listaTrabajadoresPublico !== null && Array.isArray(listaTrabajadoresPublico)) {

        const candidatos = listaTrabajadoresPublico.filter(t =>
            t.especialidad === especialidadRequerida && t.estadoLaboral === 'ACTIVO'
        );

        candidatos.forEach(trab => {
            const option = document.createElement('option');
            option.value = trab.id;
            option.text = trab.nombres + " " + trab.apellidos;
            selectVet.appendChild(option);
        });
    } else {
        console.warn("La lista de trabajadores no se cargó correctamente desde el servidor.");
    }

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
    const inputId = document.getElementById('compraProductoId');
    if (inputId) inputId.value = id;

    const labelNombre = document.getElementById('compraNombreProducto');
    if (labelNombre) labelNombre.innerText = nombre;

    const labelPrecio = document.getElementById('compraPrecio');
    if (labelPrecio) labelPrecio.innerText = 'S/ ' + precio;

    openModal('compraModal');
}