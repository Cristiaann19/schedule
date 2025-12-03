const formEnf = document.querySelector('#enfermedadModal form');

function nuevaEnfermedad() {
    if (formEnf) formEnf.reset();

    const idInput = document.getElementById('enfId');
    if (idInput) idInput.value = '';

    const title = document.querySelector('#enfermedadModal h3');
    if (title) title.innerText = "Registrar Enfermedad";

    openModal('enfermedadModal');
}

function editarEnfermedad(id) {
    const title = document.querySelector('#enfermedadModal h3');
    if (title) title.innerText = "Editar Enfermedad";

    if (typeof Toast !== 'undefined') Toast.info("Cargando datos...", "Espere");

    fetch(`/admin/enfermedades/api/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error en la respuesta");
            return res.json();
        })
        .then(data => {
            console.log("Enfermedad recibida:", data);

            const modal = document.getElementById('enfermedadModal');
            document.getElementById('enfId').value = data.id;
            modal.querySelector('input[name="nombre"]').value = data.nombre;
            modal.querySelector('select[name="especie"]').value = data.especie;
            modal.querySelector('select[name="gravedad"]').value = data.gravedad;

            const descInput = modal.querySelector('textarea[name="descripcion"]');
            if (descInput) descInput.value = data.descripcion || '';

            openModal('enfermedadModal');
        })
        .catch(err => {
            console.error("Error loading disease:", err);
            if (typeof Toast !== 'undefined') Toast.error("No se pudo cargar la información");
        });
}