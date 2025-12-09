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
            document.getElementById('citaId').value = data.id;

            if (data.fechaHora) document.querySelector('input[name="fechaHora"]').value = data.fechaHora.substring(0, 16);

            if (data.mascota) document.querySelector('select[name="mascota"]').value = data.mascota.id;

            if (data.estado) document.querySelector('select[name="estado"]').value = data.estado;

            document.querySelector('textarea[name="motivo"]').value = data.motivo;

            const selectServicio = document.getElementById('citaServicio');

            if (data.servicioId) selectServicio.value = data.servicioId;

            const trabajadorId = data.veterinario ? data.veterinario.id : null;

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

function filtrarTrabajadoresPorServicio(servicioNombre = "", trabajadorSeleccionadoId = null) {
    const selectServicio = document.getElementById('citaServicio');
    const selectTrabajador = document.getElementById('citaTrabajador');

    if (!servicioNombre && selectServicio.selectedIndex > 0) {
        servicioNombre = selectServicio.options[selectServicio.selectedIndex].getAttribute('data-nombre');
    }

    selectTrabajador.innerHTML = '<option value="">Cualquiera (Automático)</option>';

    if (!servicioNombre) return;

    let especialidadRequerida = "Veterinario";

    const nombreLower = servicioNombre.toLowerCase();
    if (nombreLower.includes('baño') || nombreLower.includes('corte') || nombreLower.includes('grooming')) {
        especialidadRequerida = "Estilista";
    }

    const candidatos = listaTrabajadores.filter(t =>
        t.especialidad === especialidadRequerida && t.estadoLaboral === 'ACTIVO'
    );

    candidatos.forEach(trab => {
        const option = document.createElement('option');
        option.value = trab.id;
        option.text = trab.nombres + " " + trab.apellidos;
        selectTrabajador.appendChild(option);
    });

    if (trabajadorSeleccionadoId) {
        selectTrabajador.value = trabajadorSeleccionadoId;
    }
}
function filtrarPersonalPorServicio() {
    filtrarTrabajadoresPorServicio();
}