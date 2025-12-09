const formVacCat = document.querySelector('#vacunaCatalogoModal form');

function nuevaVacunaCatalogo() {
    document.getElementById('vacCatId').value = '';
    document.getElementById('vacNombre').value = '';
    document.getElementById('vacFabricante').value = '';
    document.getElementById('vacDosis').value = '1';
    document.getElementById('vacEnfermedad').value = '';
    document.getElementById('vacEdad').value = '';

    const inputPrecio = document.getElementById('vacPrecio');
    if (inputPrecio) inputPrecio.value = '';

    openModal('vacunaCatalogoModal');
}

function editarVacunaCatalogo(id) {
    fetch('/admin/vacunas-catalogo/api/' + id)
        .then(response => {
            if (!response.ok) {
                throw new Error("Error en la respuesta del servidor");
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('vacCatId').value = data.id;
            document.getElementById('vacNombre').value = data.nombre;
            document.getElementById('vacFabricante').value = data.fabricante;
            document.getElementById('vacDosis').value = data.dosis;
            document.getElementById('vacEnfermedad').value = data.enfermedadAsociada;
            document.getElementById('vacEdad').value = data.edadRecomendada;

            const inputPrecio = document.getElementById('vacPrecio');
            if (inputPrecio && data.precio) {
                inputPrecio.value = data.precio;
            }

            openModal('vacunaCatalogoModal');
        })
        .catch(error => {
            console.error('Error cargando vacuna:', error);
            alert('No se pudo cargar la información de la vacuna.');
        });
}