function abrirConsulta(citaId, mascotaId, nombreMascota) {
    document.getElementById('consultaCitaId').value = citaId;
    document.getElementById('consultaMascotaId').value = mascotaId;
    document.getElementById('consultaPaciente').innerText = nombreMascota;
    openModal('consultaModal');
}