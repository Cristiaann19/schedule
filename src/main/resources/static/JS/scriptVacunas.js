const formVacCat = document.querySelector('#vacunaCatalogoModal form');

function nuevaVacunaCatalogo() {
    if (formVacCat) formVacCat.reset();
    document.getElementById('vacCatId').value = '';
    document.querySelector('#vacunaCatalogoModal h3').innerText = "Registrar Vacuna";
    openModal('vacunaCatalogoModal');
}

function editarVacunaCatalogo(id) {
    document.querySelector('#vacunaCatalogoModal h3').innerText = "Editar Vacuna";

    fetch(`/admin/vacunas-catalogo/api/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById('vacCatId').value = data.id;
            document.getElementById('vacNombre').value = data.nombre;
            document.getElementById('vacFabricante').value = data.fabricante;
            document.getElementById('vacDosis').value = data.dosis;
            document.getElementById('vacEnfermedad').value = data.enfermedadAsociada;
            document.getElementById('vacEdad').value = data.edadRecomendada;

            openModal('vacunaCatalogoModal');
        })
        .catch(err => console.error("Error cargando vacuna:", err));
}