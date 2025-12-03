const modal = document.getElementById('clienteModal');
const form = document.getElementById('clienteForm');
const modalTitle = document.getElementById('modalTitle');

function openModal(modalId) {
    document.getElementById(modalId).classList.remove('hidden');
    document.getElementById(modalId).classList.add('flex');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.add('hidden');
    document.getElementById(modalId).classList.remove('flex');
}

function nuevoCliente() {
    form.reset();
    document.getElementById('clienteId').value = '';
    modalTitle.innerText = "Nuevo Cliente";
    openModal('clienteModal');
}

function editarCliente(id) {
    modalTitle.innerText = "Editar Cliente";

    fetch(`/admin/clientes/api/${id}`)
        .then(response => {
            if (!response.ok) throw new Error("Error de red");
            return response.json();
        })
        .then(data => {
            document.getElementById('clienteId').value = data.id;
            document.getElementById('clienteDni').value = data.dni;
            document.getElementById('clienteNombres').value = data.nombres;
            document.getElementById('clienteApellidos').value = data.apellidos;
            document.getElementById('clienteTelefono').value = data.telefono;
            document.getElementById('clienteCorreo').value = data.correo;
            document.getElementById('clienteDireccion').value = data.direccion;

            openModal('clienteModal');
        })
        .catch(error => {
            console.error('Error:', error);
            if (typeof Toast !== 'undefined') {
                Toast.error("No se pudo cargar la información del cliente.");
            }
        });
}

function buscarReniec() {
    const dniInput = document.getElementById('clienteDni');
    const dni = dniInput.value;

    if (dni.length !== 8) {
        if (typeof Toast !== 'undefined') Toast.warning("El DNI debe tener 8 dígitos");
        else alert("El DNI debe tener 8 dígitos");
        return;
    }

    const btn = event.currentTarget;
    const originalContent = btn.innerHTML;
    btn.innerHTML = '<span class="material-symbols-outlined animate-spin text-sm">refresh</span>';
    btn.disabled = true;

    fetch(`http://localhost:3000/api/reniec/${dni}`)
        .then(response => response.json())
        .then(data => {
            console.log("JSON Recibido:", data);

            if (data.success && data.datos) {
                const persona = data.datos;

                document.getElementById('clienteNombres').value = persona.nombres;
                document.getElementById('clienteApellidos').value = `${persona.ape_paterno} ${persona.ape_materno}`;

                if (persona.domiciliado) {
                    const dir = persona.domiciliado;
                    const direccionCompleta = `${dir.direccion}, ${dir.distrito} - ${dir.provincia}`;
                    document.getElementById('clienteDireccion').value = direccionCompleta;
                }

                if (typeof Toast !== 'undefined') Toast.success("Datos encontrados en RENIEC");

            } else {
                if (typeof Toast !== 'undefined') Toast.error("No se encontraron datos. Verifique el DNI.");
                else alert("No se encontraron datos.");

                document.getElementById('clienteNombres').value = "";
                document.getElementById('clienteApellidos').value = "";
                document.getElementById('clienteDireccion').value = "";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            if (typeof Toast !== 'undefined') Toast.error("Error al conectar con el servicio de RENIEC (Node.js)");
            else alert("Error al conectar con Node.js");
        })
        .finally(() => {
            btn.innerHTML = originalContent;
            btn.disabled = false;
        });
}

window.onclick = function (event) {
    if (event.target == modal) {
        closeModal('clienteModal');
    }
}