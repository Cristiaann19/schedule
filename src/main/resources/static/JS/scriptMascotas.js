const formMascota = document.getElementById('mascotaForm');

function nuevaMascota() {
    formMascota.reset();
    document.getElementById('mascotaId').value = '';
    document.getElementById('modalTitleMascota').innerText = "Nueva Mascota";
    openModal('mascotaModal');
}

function editarMascota(id) {
    document.getElementById('modalTitleMascota').innerText = "Editar Mascota";

    console.log("Cargando mascota ID:", id);

    fetch(`/admin/mascotas/api/${id}`)
        .then(res => {
            if (!res.ok) throw new Error("Error en la petición");
            return res.json();
        })
        .then(data => {
            console.log("Datos recibidos:", data);

            document.getElementById('mascotaId').value = data.id;

            document.getElementById('mascotaNombre').value = data.nombre;
            document.getElementById('mascotaEspecie').value = data.especie;
            document.getElementById('mascotaRaza').value = data.raza;
            document.getElementById('mascotaEdad').value = data.edad;
            document.getElementById('mascotaObs').value = data.observaciones || "";

            if (data.cliente) {
                const selectCliente = document.getElementById('mascotaCliente');
                if (selectCliente) selectCliente.value = data.cliente.id;
            }

            const radios = document.getElementsByName('sexo');
            if (data.sexo) {
                for (let r of radios) {
                    if (r.value.toLowerCase() === data.sexo.toLowerCase()) {
                        r.checked = true;
                        break;
                    }
                }
            }

            openModal('mascotaModal');
        })
        .catch(error => {
            console.error("Error editando mascota:", error);
            alert("No se pudieron cargar los datos.");
        });
}

function verHistorialMascota(id, nombre) {
    document.getElementById('historialNombreMascota').innerText = nombre;
    const container = document.getElementById('contenedorHistorial');
    container.innerHTML = '<div class="flex justify-center py-10"><span class="material-symbols-outlined animate-spin text-4xl text-primary">refresh</span></div>';

    openModal('historialMascotaModal');

    fetch(`/admin/historial/api/mascota/${id}`)
        .then(res => res.json())
        .then(data => {
            container.innerHTML = ''; // Limpiar loader

            if (data.length === 0) {
                container.innerHTML = `
                    <div class="text-center py-10 opacity-60">
                        <span class="material-symbols-outlined text-6xl text-gray-300 mb-4">folder_off</span>
                        <p class="text-lg text-gray-500">No hay registros médicos para ${nombre}</p>
                    </div>`;
                return;
            }

            // Ordenar por fecha (más reciente primero)
            data.sort((a, b) => new Date(b.fechaRegistro) - new Date(a.fechaRegistro));

            let html = '<div class="space-y-8 relative before:absolute before:inset-0 before:ml-5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-gray-300 before:to-transparent">';

            data.forEach(ficha => {
                const fecha = new Date(ficha.fechaRegistro).toLocaleString();

                // Determinar tipo de evento para el icono y color
                let icon = 'stethoscope';
                let colorClass = 'bg-red-100 text-red-600 border-red-200';
                let titulo = 'Consulta General';

                if (ficha.vacunaAplicada && ficha.vacunaAplicada !== '-- Ninguna --') {
                    icon = 'vaccines';
                    colorClass = 'bg-blue-100 text-blue-600 border-blue-200';
                    titulo = 'Vacunación: ' + ficha.vacunaAplicada;
                } else if (ficha.enfermedadDetectada && ficha.enfermedadDetectada !== '-- Ninguna / Sana --') {
                    titulo = 'Diagnóstico: ' + ficha.enfermedadDetectada;
                }

                html += `
                <div class="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">
                    <div class="flex items-center justify-center w-10 h-10 rounded-full border-2 bg-white shadow shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 ${colorClass.replace('bg-', 'border-')}">
                        <span class="material-symbols-outlined text-sm">${icon}</span>
                    </div>
                    
                    <div class="w-[calc(100%-4rem)] md:w-[calc(50%-2.5rem)] p-5 bg-white rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-shadow">
                        <div class="flex justify-between items-start mb-2">
                            <h4 class="font-bold text-gray-800">${titulo}</h4>
                            <span class="text-xs text-gray-400 bg-gray-50 px-2 py-1 rounded">${fecha}</span>
                        </div>
                        
                        <div class="space-y-2 text-sm text-gray-600">
                            ${ficha.diagnostico ? `<p><strong class="text-gray-800">Obs:</strong> ${ficha.diagnostico}</p>` : ''}
                            ${ficha.tratamiento ? `<p class="bg-yellow-50 p-2 rounded border border-yellow-100 text-yellow-800"><strong class="block text-xs uppercase text-yellow-600 mb-1">Receta/Tratamiento:</strong> ${ficha.tratamiento}</p>` : ''}
                            ${ficha.peso ? `<div class="flex items-center gap-1 text-xs font-semibold text-blue-600 mt-2"><span class="material-symbols-outlined text-[16px]">monitor_weight</span> Peso: ${ficha.peso} kg</div>` : ''}
                        </div>
                    </div>
                </div>
                `;
            });

            html += '</div>';
            container.innerHTML = html;
        })
        .catch(err => {
            console.error(err);
            container.innerHTML = '<p class="text-center text-red-500">Error cargando el historial.</p>';
        });
}