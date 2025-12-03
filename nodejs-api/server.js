const express = require('express');
const cors = require('cors');

const reniecController = require('./controllers/reniecController');
const emailController = require('./controllers/emailController');
const pdfController = require('./controllers/pdfController');
const excelController = require('./controllers/excelController');
const { upload, subirImagen } = require('./controllers/uploadController');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());


app.get('/api/reniec/:dni', reniecController.consultarReniec);

app.post('/api/email/send', emailController.enviarCorreoContacto);

app.post('/api/pdf/boleta', pdfController.generarBoletaPDF);

app.post('/api/excel/ventas', excelController.generarReporteVentas);
app.post('/api/upload', upload.single('imagen'), subirImagen);

// Iniciar Servidor
app.listen(PORT, () => {
    console.log(`Servidor Microservicios listo en http://localhost:${PORT}`);
});