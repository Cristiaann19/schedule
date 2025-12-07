const formCita = document.getElementById('citaForm');

function abrirConsulta(citaId, mascotaId, nombreMascota) {
    document.getElementById('consultaCitaId').value = citaId;
    document.getElementById('consultaMascotaId').value = mascotaId;
    document.getElementById('consultaPaciente').innerText = nombreMascota;
    openModal('consultaModal');
}

function nuevaCita() {
    formCita.reset();
    document.getElementById('citaId').value = '';
    document.getElementById('modalTitleCita').innerText = "Agendar Cita";
    const selectEstado = document.querySelector('#citaForm select[name="estado"]');
    if (selectEstado) selectEstado.value = 'PENDIENTE';

    openModal('citaModal');
}

function editarCita(id) {
    const titulo = document.getElementById('modalTitleCita');
    if (titulo) titulo.innerText = "Editar Cita";

    if (typeof Toast !== 'undefined') {
        Toast.info("Cargando datos...", "Espere");
    }

    fetch(`/admin/citas/api/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error en la respuesta del servidor");
            return res.json();
        })
        .then(data => {
            console.log("--> Datos cita:", data);


            const inputId = document.getElementById('citaId');
            if (inputId) inputId.value = data.id;

            if (data.mascota) {
                const selectMascota = document.querySelector('#citaForm select[name="mascota"]');
                if (selectMascota) selectMascota.value = data.mascota.id;
            }

            if (data.veterinario) {
                const selectVet = document.querySelector('#citaForm select[name="veterinario"]');
                if (selectVet) selectVet.value = data.veterinario.id;
            }

            const selectServicio = document.querySelector('#citaForm select[name="servicioId"]');
            if (selectServicio) selectServicio.value = data.servicioId;

            const inputFecha = document.querySelector('#citaForm input[name="fechaHora"]');
            if (data.fechaHora && inputFecha) {

                let fechaFormateada = data.fechaHora.substring(0, 16);
                inputFecha.value = fechaFormateada;
            }

            if (data.estado) {
                const selectEstado = document.querySelector('#citaForm select[name="estado"]');
                if (selectEstado) selectEstado.value = data.estado;
            }

            const txtMotivo = document.querySelector('#citaForm textarea[name="motivo"]');
            if (txtMotivo) txtMotivo.value = data.motivo;

            openModal('citaModal');
        })
        .catch(error => {
            console.error("Error:", error);
            if (typeof Toast !== 'undefined') {
                Toast.error("No se pudo cargar la información de la cita.");
            } else {
                alert("Error al cargar la cita.");
            }
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