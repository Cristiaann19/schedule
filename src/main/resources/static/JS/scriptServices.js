const formServ = document.getElementById('servicioForm');

function nuevoServicio() {
    formServ.reset();
    document.getElementById('servId').value = '';
    document.getElementById('servColorIni').value = 'from-emerald-500';
    document.getElementById('servColorFin').value = 'to-teal-500';
    document.getElementById('modalTitleServ').innerText = "Nuevo Servicio";
    openModal('servicioModal');
}

function editarServicio(id) {
    document.getElementById('modalTitleServ').innerText = "Editar Servicio";
    fetch(`/admin/servicios/api/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById('servId').value = data.id;
            document.getElementById('servNombre').value = data.nombre;
            document.getElementById('servDesc').value = data.descripcion;
            document.getElementById('servPrecio').value = data.precio;
            document.getElementById('servIcono').value = data.icono;
            document.getElementById('servColorIni').value = data.colorInicio;
            document.getElementById('servColorFin').value = data.colorFin;
            openModal('servicioModal');
        });
}