const formCita = document.getElementById('citaForm');

function abrirConsulta(citaId, mascotaId, nombreMascota) {
    document.getElementById('consultaCitaId').value = citaId;
    document.getElementById('consultaMascotaId').value = mascotaId;
    document.getElementById('consultaPaciente').innerText = nombreMascota;
    openModal('consultaModal');
}

function nuevaCita() {
    document.getElementById('citaForm').reset();
    document.getElementById('citaId').value = '';
    document.getElementById('modalTitleCita').innerText = "Agendar Cita";

    document.getElementById('citaTrabajador').innerHTML = '<option value="">Seleccione servicio primero...</option>';

    openModal('citaModal');
}

function editarCita(id) {
    document.getElementById('modalTitleCita').innerText = "Editar Cita";

    fetch(`/admin/citas/api/${id}`)
        .then(res => res.json())
        .then(data => {
            // ... (tus otros campos: id, fecha, motivo, mascota) ...
            document.getElementById('citaId').value = data.id;

            // Fecha (recuerda el formato)
            if (data.fechaHora) document.querySelector('input[name="fechaHora"]').value = data.fechaHora.substring(0, 16);

            // Mascota
            if (data.mascota) document.querySelector('select[name="mascota"]').value = data.mascota.id;

            // Estado
            if (data.estado) document.querySelector('select[name="estado"]').value = data.estado;

            document.querySelector('textarea[name="motivo"]').value = data.motivo;

            // --- AQUÍ ESTÁ LA MAGIA ---
            // 1. Seleccionar el Servicio
            const selectServicio = document.getElementById('citaServicio');

            // Nota: data.servicioId podría venir como número o string, en Mongo a veces es string
            if (data.servicioId) selectServicio.value = data.servicioId;

            // 2. Obtener el ID del trabajador actual (si existe)
            const trabajadorId = data.veterinario ? data.veterinario.id : null;

            // 3. Ejecutar el filtro MANUALMENTE pasando el nombre del servicio
            // (El nombre viene en data.servicioNombre)
            filtrarTrabajadoresPorServicio(data.servicioNombre, trabajadorId);

            openModal('citaModal');
        });
}

function cobrarCita(id) {
    if (!confirm("¿Deseas cobrar esta cita y generar la venta?")) return;

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/admin/citas/cobrar/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
        .then(res => {
            if (res.ok) {
                Toast.success("Cita cobrada y venta registrada");
                setTimeout(() => window.location.reload(), 1000);
            } else {
                Toast.error("Error al cobrar");
            }
        });
}

// Función para filtrar trabajadores dinámicamente
function filtrarTrabajadoresPorServicio(servicioNombre = "", trabajadorSeleccionadoId = null) {
    const selectServicio = document.getElementById('citaServicio');
    const selectTrabajador = document.getElementById('citaTrabajador');

    // Si no pasamos nombre, lo intentamos sacar del select
    if (!servicioNombre && selectServicio.selectedIndex > 0) {
        servicioNombre = selectServicio.options[selectServicio.selectedIndex].getAttribute('data-nombre');
    }

    // Limpiar opciones actuales
    selectTrabajador.innerHTML = '<option value="">Cualquiera (Automático)</option>';

    if (!servicioNombre) return; // Si no hay servicio, no mostramos nada

    // 1. Determinar especialidad requerida
    let especialidadRequerida = "Veterinario"; // Por defecto

    // Si el nombre contiene "Baño", "Corte" o "Grooming", necesitamos un Estilista
    const nombreLower = servicioNombre.toLowerCase();
    if (nombreLower.includes('baño') || nombreLower.includes('corte') || nombreLower.includes('grooming')) {
        especialidadRequerida = "Estilista";
    }

    // 2. Filtrar la lista global (que viene del HTML)
    // Filtramos por especialidad Y que estén ACTIVOS
    const candidatos = listaTrabajadores.filter(t =>
        t.especialidad === especialidadRequerida && t.estadoLaboral === 'ACTIVO'
    );

    // 3. Llenar el select
    candidatos.forEach(trab => {
        const option = document.createElement('option');
        option.value = trab.id;
        option.text = trab.nombres + " " + trab.apellidos;
        selectTrabajador.appendChild(option);
    });

    // 4. Si estamos editando, seleccionar al trabajador original
    if (trabajadorSeleccionadoId) {
        selectTrabajador.value = trabajadorSeleccionadoId;
    }
}

// Actualizar también la función "filtrarPersonalPorServicio" para el onchange del HTML
function filtrarPersonalPorServicio() {
    filtrarTrabajadoresPorServicio();
}