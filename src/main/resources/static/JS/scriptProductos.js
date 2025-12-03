const formProd = document.getElementById('productoForm');

function nuevoProducto() {
    formProd.reset();
    document.getElementById('prodId').value = '';
    document.getElementById('modalTitleProd').innerText = "Nuevo Producto";
    openModal('productoModal');
}

function editarProducto(id) {
    document.getElementById('modalTitleProd').innerText = "Editar Producto";
    fetch(`/admin/productos/api/${id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById('prodId').value = data.id;
            document.getElementById('prodNombre').value = data.nombre;
            document.getElementById('prodDesc').value = data.descripcion;
            document.getElementById('prodPrecio').value = data.precio;
            document.getElementById('prodStock').value = data.stock;
            document.getElementById('prodImg').value = data.imagenUrl || '';
            openModal('productoModal');
        });
}


