const formTrab = document.getElementById('trabajadorForm');

function nuevoTrabajador() {
    formTrab.reset();
    document.getElementById('trabId').value = '';
    document.getElementById('modalTitleTrab').innerText = "Nuevo Trabajador";
    openModal('trabajadorModal');
}

function editarTrabajador(id) {
    document.getElementById('modalTitleTrab').innerText = "Editar Trabajador";
    fetch(`/admin/trabajadores/api/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById('trabId').value = data.id;
            document.getElementById('trabDni').value = data.dni;
            document.getElementById('trabNombres').value = data.nombres;
            document.getElementById('trabApellidos').value = data.apellidos;
            document.getElementById('trabEspecialidad').value = data.especialidad;
            document.getElementById('trabTelefono').value = data.telefono;
            document.getElementById('trabCorreo').value = data.correo;
            document.getElementById('trabHorario').value = data.horarioDisponible;
            document.getElementById('trabEstado').value = data.estadoLaboral;
            openModal('trabajadorModal');
        });
}

function buscarReniecTrabajador() {
    const dni = document.getElementById('trabDni').value;
    if (dni.length !== 8) return alert("DNI inválido");

    fetch(`http://localhost:3000/api/reniec/${dni}`)
        .then(r => r.json())
        .then(data => {
            if (data.success && data.datos) {
                const p = data.datos;
                document.getElementById('trabNombres').value = p.nombres;
                document.getElementById('trabApellidos').value = p.ape_paterno + ' ' + p.ape_materno;
            } else {
                alert("No encontrado");
            }
        });
}