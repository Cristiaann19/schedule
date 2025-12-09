function abrirReserva(idServicio, nombreServicio) {
    // 1. LLENAR LOS INPUTS (Corrección Importante: Faltaba asignar el valor)
    const inputId = document.getElementById('reservaServicioId');
    const inputNombre = document.getElementById('reservaServicioNombre');

    if (inputId) inputId.value = idServicio;
    if (inputNombre) inputNombre.value = nombreServicio;

    // 2. FILTRAR TRABAJADORES
    const selectVet = document.getElementById('reservaVeterinario');

    // Limpiamos el select anterior
    selectVet.innerHTML = '<option value="">Cualquiera (Asignación Automática)</option>';

    // Determinar especialidad requerida
    let especialidadRequerida = "Veterinario";

    // CORRECCIÓN: Usamos la variable correcta 'nombreServicio'
    if (nombreServicio) {
        const nombreLower = nombreServicio.toLowerCase();

        if (nombreLower.includes('baño') || nombreLower.includes('corte') || nombreLower.includes('grooming')) {
            especialidadRequerida = "Estilista";
        }
    }

    // Filtrar la lista global
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

    // 3. Mostrar Modal
    openModal('reservaModal');
}

// Funciones auxiliares (Estas estaban bien)
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

// (Opcional) Si usas esto para productos
function abrirCompra(id, nombre, precio) {
    // Asegúrate que estos IDs existan en tu HTML si vas a usar esta función
    const inputId = document.getElementById('compraProductoId');
    if (inputId) inputId.value = id;

    const labelNombre = document.getElementById('compraNombreProducto');
    if (labelNombre) labelNombre.innerText = nombre;

    const labelPrecio = document.getElementById('compraPrecio');
    if (labelPrecio) labelPrecio.innerText = 'S/ ' + precio;

    openModal('compraModal');
}