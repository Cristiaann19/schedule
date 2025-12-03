const multer = require('multer');
const cloudinary = require('cloudinary').v2;
const fs = require('fs');

cloudinary.config({
    cloud_name: 'ddxdadxtr',
    api_key: '797222788816749',
    api_secret: 'y1Nr-GP65dQjm-Nt4IO83d5phJs'
});

const upload = multer({ dest: 'uploads/' });

const subirImagen = async (req, res) => {
    try {
        if (!req.file) {
            return res.status(400).json({ error: "No seleccionaste ninguna imagen" });
        }

        console.log("--> Subiendo imagen a Cloudinary...");


        const resultado = await cloudinary.uploader.upload(req.file.path, {
            folder: "huellitas_productos",
            use_filename: true
        });

        fs.unlinkSync(req.file.path);

        console.log("--> Imagen subida: " + resultado.secure_url);
        res.json({
            success: true,
            url: resultado.secure_url
        });

    } catch (error) {
        console.error("Error subiendo:", error);
        res.status(500).json({ error: "Error al subir la imagen" });
    }
};

module.exports = { upload, subirImagen };