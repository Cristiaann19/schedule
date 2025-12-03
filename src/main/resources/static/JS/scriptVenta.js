function verDetalleVenta(id) {
    document.getElementById('ventaIdDisplay').innerText = "Cargando...";

    fetch(`/admin/ventas/api/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error del servidor: " + res.status);
            return res.json();
        })
        .then(data => {
            console.log("Venta recibida:", data);
            document.getElementById('ventaIdDisplay').innerText = data.id;

            const nombreCliente = data.cliente?.nombres || "Cliente";
            const apellidoCliente = data.cliente?.apellidos || "Desconocido";
            document.getElementById('ventaCliente').innerText = `${nombreCliente} ${apellidoCliente}`;

            document.getElementById('ventaFecha').innerText = data.fecha ? data.fecha.split('T')[0] : "-";

            document.getElementById('ventaMetodo').innerText = data.metodoPago || "-";
            document.getElementById('ventaCodigo').innerText = data.codigoOperacion || "-";
            document.getElementById('ventaTotal').innerText = 'S/ ' + (data.total ? data.total.toFixed(2) : "0.00");

            const lista = document.getElementById('ventaProductosLista');
            lista.innerHTML = '';

            if (data.detalles) {
                data.detalles.forEach(d => {
                    lista.innerHTML += `
                        <li class="flex justify-between border-b border-gray-100 py-2">
                            <span class="text-gray-600">${d.cantidad}x ${d.nombreProducto}</span>
                            <span class="font-medium">S/ ${d.subtotal.toFixed(2)}</span>
                        </li>
                    `;
                });
            }

            openModal('ventaModal');
        })
        .catch(err => {
            console.error(err);
            alert("No se pudo cargar el detalle de la venta.");
        });
}