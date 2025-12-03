const puppeteer = require('puppeteer');

const generarBoletaPDF = async (req, res) => {
    const data = req.body;

    try {
        const browser = await puppeteer.launch({ headless: "new" });
        const page = await browser.newPage();

        const htmlContent = `
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: 'Courier New', Courier, monospace; padding: 20px; }
                .ticket { width: 300px; margin: 0 auto; padding: 15px; border: 1px solid #ccc; }
                .cabecera { text-align: center; margin-bottom: 10px; }
                .logo { font-weight: bold; font-size: 20px; }
                .info-negocio { font-size: 10px; color: #555; }
                .separador { border-top: 1px dashed #333; margin: 10px 0; }
                .info-ticket { font-size: 12px; margin-bottom: 4px; }
                .tabla-header { display: flex; font-weight: bold; font-size: 10px; border-bottom: 1px solid #000; }
                .col-desc { flex: 2; } .col-cant { flex: 0.5; text-align: center; } .col-precio { flex: 1; text-align: right; }
                .item { display: flex; font-size: 10px; margin-bottom: 4px; }
                .totales { text-align: right; margin-top: 10px; font-size: 12px; }
                .pie { text-align: center; font-size: 10px; margin-top: 15px; color: #777; }
            </style>
        </head>
        <body>
            <div class="ticket">
                <div class="cabecera">
                    <div class="logo">🐾 HUELLITAS VET</div>
                    <div class="info-negocio">Av. Principal 123 - Chiclayo</div>
                    <div class="info-negocio">RUC: 20123456789</div>
                </div>
                <div class="info-ticket"><strong>N° TICKET:</strong> ${data.id}</div>
                <div class="info-ticket"><strong>FECHA:</strong> ${data.fecha}</div>
                <div class="info-ticket"><strong>CLIENTE:</strong> ${data.cliente}</div>
                <div class="info-ticket"><strong>DNI:</strong> ${data.dni}</div>
                <hr class="separador">
                <div class="tabla-header">
                    <div class="col-desc">DESC</div>
                    <div class="col-cant">CANT</div>
                    <div class="col-precio">TOTAL</div>
                </div>
                ${data.items.map(item => `
                    <div class="item">
                        <div class="col-desc">${item.nombre}</div>
                        <div class="col-cant">${item.cantidad}</div>
                        <div class="col-precio">S/ ${item.subtotal.toFixed(2)}</div>
                    </div>
                `).join('')}
                <hr class="separador">
                <div class="totales">
                    <div><strong>TOTAL: S/ ${data.total.toFixed(2)}</strong></div>
                </div>
                <div class="pie">
                    <p>¡Gracias por su compra!</p>
                </div>
            </div>
        </body>
        </html>
        `;

        await page.setContent(htmlContent);
        const pdfBuffer = await page.pdf({ format: 'A6', printBackground: true });
        await browser.close();

        res.set({
            'Content-Type': 'application/pdf',
            'Content-Length': pdfBuffer.length
        });
        res.send(pdfBuffer);

    } catch (error) {
        console.error("Error PDF:", error);
        res.status(500).json({ error: "Error al generar boleta" });
    }
};

module.exports = { generarBoletaPDF };