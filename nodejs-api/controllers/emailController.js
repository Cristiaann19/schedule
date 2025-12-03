const nodemailer = require('nodemailer');

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'huamancruzcristianjesus@gmail.com',
        pass: 'yitz djsc rpsb gqgj'
    }
});

const enviarCorreoContacto = async (req, res) => {
    const { nombre, correo, asunto, mensaje } = req.body;

    if (!nombre || !correo || !mensaje) {
        return res.status(400).json({ error: "Faltan datos requeridos" });
    }

    const mailOptions = {
        from: `"Web Huellitas" <huamancruzcristianjesus@gmail.com>`,
        to: 'huamancruzcristianjesus@gmail.com',
        replyTo: correo,
        subject: `📩 Nuevo Mensaje: ${asunto}`,
        html: `
            <div style="font-family: Arial, sans-serif; padding: 20px; border: 1px solid #eee; border-radius: 10px;">
                <h2 style="color: #10b981;">Nuevo mensaje de contacto</h2>
                <hr style="border: 0; border-top: 1px solid #eee;">
                <p><strong>👤 Nombre:</strong> ${nombre}</p>
                <p><strong>📧 Correo:</strong> ${correo}</p>
                <p><strong>📝 Asunto:</strong> ${asunto}</p>
                <div style="background: #f9f9f9; padding: 15px; margin-top: 10px;">
                    <p><strong>Mensaje:</strong></p>
                    <p style="color: #555;">${mensaje}</p>
                </div>
            </div>
        `
    };

    try {
        await transporter.sendMail(mailOptions);
        console.log(`--> Correo enviado de: ${nombre}`);
        res.json({ success: true });
    } catch (error) {
        console.error("Error enviando correo:", error);
        res.status(500).json({ success: false, error: "Error al enviar correo" });
    }
};

module.exports = { enviarCorreoContacto };