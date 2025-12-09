function abrirGaleria(tipoDestino) {
    // 1. Guardamos qué queremos cambiar (LOGO o SLIDER)
    document.getElementById('accionGaleriaDestino').value = tipoDestino;

    // 2. Mostramos el modal
    const modal = document.getElementById('galeriaModal');
    modal.classList.remove('hidden');
    modal.classList.add('flex');

    // 3. Pedimos las fotos al servidor (Java)
    const contenedor = document.getElementById('contenedorGaleria');
    contenedor.innerHTML = '<p class="text-center col-span-full text-gray-500">Cargando...</p>';

    fetch('/admin/web/api/imagenes')
        .then(response => response.json())
        .then(urls => {
            contenedor.innerHTML = ''; // Limpiar

            if (urls.length === 0) {
                contenedor.innerHTML = '<p class="text-center col-span-full text-gray-400">No hay imágenes subidas aún.</p>';
                return;
            }

            urls.forEach(url => {
                // Crear tarjeta de imagen
                const div = document.createElement('div');
                div.className = "relative group cursor-pointer border-2 border-gray-200 hover:border-blue-500 rounded-xl overflow-hidden bg-gray-50 shadow-sm transition-all w-full h-40";
                div.onclick = () => seleccionarImagenDeGaleria(url);

                div.innerHTML = `
        <img src="${url}" class="w-full h-full object-contain">
        
        <div class="absolute inset-0 bg-black/50 flex flex-col items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity gap-2">
            <span class="text-white font-bold text-xs bg-blue-600 px-3 py-1 rounded-full shadow-md hover:bg-blue-700">Usar</span>
            
            <button onclick="event.stopPropagation(); borrarDeGaleria('${url}')" 
                    class="text-white bg-red-600 p-1 rounded-full hover:bg-red-700" title="Borrar permanentemente">
                <span class="material-symbols-outlined text-[16px]">delete</span>
            </button>
        </div>
    `;
                contenedor.appendChild(div);
            });
        })
        .catch(err => {
            console.error(err);
            contenedor.innerHTML = '<p class="text-red-500 text-center col-span-full">Error al cargar imágenes.</p>';
        });
}

function seleccionarImagenDeGaleria(url) {
    const destino = document.getElementById('accionGaleriaDestino').value;

    if (confirm('¿Usar esta imagen?')) {
        if (destino === 'LOGO') {
            document.getElementById('inputUrlLogo').value = url;
            document.getElementById('formSelectLogo').submit();
        } else if (destino === 'SLIDER') {
            document.getElementById('inputUrlSlider').value = url;
            document.getElementById('formSelectSlider').submit();
        }
    }
}

function previsualizarImagen(input, idImagenDestino) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            var imagen = document.getElementById(idImagenDestino);
            imagen.src = e.target.result;

            imagen.classList.remove('hidden');
            imagen.style.display = 'block';
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function borrarDeGaleria(url) {
    if (!confirm("¿Estás seguro de borrar este archivo del servidor? Esta acción no se puede deshacer.")) return;

    fetch('/admin/web/api/imagenes/borrar?url=' + encodeURIComponent(url))
        .then(res => {
            if (res.ok) {
                const tipo = document.getElementById('accionGaleriaDestino').value;
                abrirGaleria(tipo);
            } else {
                alert("No se pudo borrar la imagen.");
            }
        });
}

