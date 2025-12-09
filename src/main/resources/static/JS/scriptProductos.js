const formProd = document.getElementById('productoForm');

function nuevoProducto() {
    formProd.reset();
    document.getElementById('prodId').value = '';
    document.getElementById('modalTitleProd').innerText = "Nuevo Producto";
    document.getElementById('prodImg').value = '';
    document.getElementById('previewContainer').classList.add('hidden');
    document.getElementById('imgPreview').src = '';
    document.getElementById('uploadStatus').innerText = "Sin archivo";

    openModal('productoModal');
}

function editarProducto(id) {
    document.getElementById('modalTitleProd').innerText = "Editar Producto";

    if (typeof Toast !== 'undefined') Toast.info("Cargando datos...");

    fetch(`/admin/productos/api/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error al obtener producto");
            return res.json();
        })
        .then(data => {
            document.getElementById('prodId').value = data.id;
            document.getElementById('prodNombre').value = data.nombre;
            document.getElementById('prodDesc').value = data.descripcion;
            document.getElementById('prodPrecio').value = data.precio;
            document.getElementById('prodStock').value = data.stock;

            const hiddenInput = document.getElementById('prodImg');
            const previewContainer = document.getElementById('previewContainer');
            const imgPreview = document.getElementById('imgPreview');
            const statusText = document.getElementById('uploadStatus');

            hiddenInput.value = data.imagenUrl || '';

            if (data.imagenUrl) {
                imgPreview.src = data.imagenUrl;
                previewContainer.classList.remove('hidden');
                statusText.innerText = "Imagen actual cargada";
            } else {
                previewContainer.classList.add('hidden');
                imgPreview.src = '';
                statusText.innerText = "Sin imagen";
            }

            openModal('productoModal');
        })
        .catch(err => {
            console.error(err);
            if (typeof Toast !== 'undefined') Toast.error("No se pudo cargar el producto");
        });
}

function verHistorialProducto(id, nombre) {
    document.getElementById('historialProdNombre').innerText = nombre;
    const tbody = document.getElementById('tablaHistorialBody');
    const totalCant = document.getElementById('totalCantHistorial');
    const totalMonto = document.getElementById('totalMontoHistorial');

    tbody.innerHTML = '<tr><td colspan="5" class="text-center py-8"><span class="material-symbols-outlined animate-spin text-3xl text-primary">refresh</span></td></tr>';

    openModal('historialProductoModal');

    fetch(`/admin/productos/historial/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error en historial");
            return res.json();
        })
        .then(data => {
            tbody.innerHTML = '';

            if (data.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" class="text-center py-8 text-gray-400">No hay ventas registradas para este producto.</td></tr>';
                totalCant.innerText = "0";
                totalMonto.innerText = "S/ 0.00";
                return;
            }

            let sumCant = 0;
            let sumTotal = 0;

            data.forEach(item => {
                sumCant += item.cantidad;
                sumTotal += item.subtotal;

                const row = `
                    <tr class="hover:bg-gray-50 transition-colors border-b last:border-b-0">
                        <td class="px-4 py-3 text-gray-600">${item.fecha}</td>
                        <td class="px-4 py-3 font-mono text-xs text-gray-500 bg-gray-50 rounded w-fit">${item.documento}</td>
                        <td class="px-4 py-3 text-right text-gray-600">S/ ${item.precio.toFixed(2)}</td>
                        <td class="px-4 py-3 text-center font-bold text-gray-800">${item.cantidad}</td>
                        <td class="px-4 py-3 text-right font-bold text-emerald-600">S/ ${item.subtotal.toFixed(2)}</td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });

            totalCant.innerText = sumCant;
            totalMonto.innerText = `S/ ${sumTotal.toFixed(2)}`;
        })
        .catch(err => {
            console.error(err);
            if (typeof Toast !== 'undefined') Toast.error("Error al cargar el historial");
            tbody.innerHTML = '<tr><td colspan="5" class="text-center py-4 text-red-500">Error de conexión</td></tr>';
        });
}