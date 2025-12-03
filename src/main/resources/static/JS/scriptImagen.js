async function subirImagenCloudinary() {
    const fileInput = document.getElementById('fileInput');
    const statusText = document.getElementById('uploadStatus');
    const previewContainer = document.getElementById('previewContainer');
    const imgPreview = document.getElementById('imgPreview');
    const inputOculto = document.getElementById('prodImg');

    if (fileInput.files.length === 0) return;

    const file = fileInput.files[0];
    statusText.innerText = "Subiendo...";
    statusText.className = "text-xs text-blue-500 font-bold animate-pulse";

    const formData = new FormData();
    formData.append('imagen', file);

    try {
        const res = await fetch('http://localhost:3000/api/upload', {
            method: 'POST',
            body: formData
        });
        const data = await res.json();

        if (data.success) {
            statusText.innerText = "¡Imagen cargada!";
            statusText.className = "text-xs text-green-600 font-bold";

            inputOculto.value = data.url;
            imgPreview.src = data.url;
            previewContainer.classList.remove('hidden');
        } else {
            alert("Error: " + data.error);
            statusText.innerText = "Error al subir";
            statusText.className = "text-xs text-red-500";
        }

    } catch (error) {
        console.error(error);
        statusText.innerText = "Error de conexión";
    }
}