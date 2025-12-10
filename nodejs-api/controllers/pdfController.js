const puppeteer = require('puppeteer');
const fs = require('fs');
const path = require('path');

const generarBoletaPDF = async (req, res) => {
    const data = req.body;

    // 1. CARGAR Y CONVERTIR IMAGEN A BASE64
    const imagePath = path.join(__dirname, '../assets/logo-huellitas.png');
    const logoBase64 = fs.readFileSync(imagePath).toString('base64');
    const logoSrc = `data:image/png;base64,${logoBase64}`;

    // Formatear items para el HTML
    const itemsHtml = data.items.map(item => `
        <div class="servicio-fila">
            <div class="servicio-desc">
                ${item.nombre} <span style="font-size:9px; color:#666;">(x${item.cantidad})</span>
            </div>
            <div class="servicio-precio">S/ ${item.subtotal.toFixed(2)}</div>
        </div>
    `).join('');

    try {
        const browser = await puppeteer.launch({
            headless: "new",
            args: ['--no-sandbox', '--disable-setuid-sandbox']
        });
        const page = await browser.newPage();

        const htmlContent = `
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                body { 
                    font-family: 'Courier New', monospace; 
                    background: white; 
                    padding: 0; 
                }
                .ticket {
                    width: 100%;
                    max-width: 300px;
                    margin: 0 auto;
                    padding: 10px;
                    background: white;
                }
                .cabecera {
                    text-align: center;
                    margin-bottom: 10px;
                    border-bottom: 2px dashed #333;
                    padding-bottom: 10px;
                }
                
                /* ESTILOS NUEVOS PARA EL LOGO IMAGEN */
                .logo-img {
                    width: 120px; /* Ajusta el tamaño aquí */
                    height: auto;
                    margin: 0 auto 5px auto;
                    display: block;
                }

                .info-negocio { font-size: 9px; color: #666; line-height: 1.3; }
                
                .info-ticket { font-size: 10px; margin: 3px 0; color: #333; }
                .info-ticket strong { color: #000; }
                
                .separador { border: none; border-top: 1px dashed #999; margin: 8px 0; }
                .separador-doble { border: none; border-top: 2px dashed #333; margin: 10px 0; }
                
                .seccion-titulo {
                    background: linear-gradient(90deg,rgba(19, 120, 83, 1) 0%, rgba(26, 171, 125, 1) 50%, rgba(23, 209, 150, 1) 100%);
                    color: white;
                    padding: 4px;
                    font-size: 10px;
                    font-weight: bold;
                    text-align: center;
                    margin: 8px 0;
                    border-radius: 4px;
                }

                .tabla-header {
                    display: flex;
                    justify-content: space-between;
                    background: #333;
                    color: white;
                    padding: 5px;
                    font-size: 9px;
                    font-weight: bold;
                    border-radius: 3px 3px 0 0;
                }
                
                .servicio-fila {
                    display: flex;
                    justify-content: space-between;
                    padding: 5px;
                    font-size: 9px;
                    border-bottom: 1px solid #eee;
                }
                
                .totales {
                    margin: 10px 0;
                    background: #f8f9fa;
                    padding: 8px;
                    border-radius: 4px;
                }
                .fila-total {
                    display: flex;
                    justify-content: space-between;
                    font-size: 10px;
                    margin: 4px 0;
                }
                .fila-total.final {
                    font-size: 12px;
                    font-weight: bold;
                    border-top: 1px solid #333;
                    padding-top: 5px;
                }
                
                .pie { text-align: center; margin-top: 10px; }
                .mensaje { font-size: 11px; font-weight: bold; color: #137853; margin-bottom: 5px;}
                .contacto { font-size: 8px; color: #666; }
            </style>
        </head>
        <body>
            <div class="ticket">
                <div class="cabecera">
                    <img src="${logoSrc}" class="logo-img" alt="Huellitas Vet">
                    
                    <div class="info-negocio">Av. Principal 123 - Chiclayo</div>
                    <div class="info-negocio">RUC: 20123456789</div>
                    <div class="info-negocio">Telf: (074) 223-344</div>
                </div>

                <div class="info-ticket"><strong>N° TICKET:</strong> ${data.id || '---'}</div>
                <div class="info-ticket"><strong>FECHA:</strong> ${data.fecha}</div>
                
                <div class="separador"></div>
                <div class="info-ticket"><strong>CLIENTE:</strong> ${data.cliente}</div>
                <div class="info-ticket"><strong>DNI:</strong> ${data.dni || '---'}</div>
                <div class="info-ticket"><strong>MÉTODO PAGO:</strong> ${data.metodoPago || 'Efectivo'}</div>

                <div class="seccion-titulo">DETALLE DE COMPRA</div>
                
                <div class="tabla-servicios">
                    <div class="tabla-header">
                        <div>DESCRIPCIÓN</div>
                        <div>TOTAL</div>
                    </div>
                    ${itemsHtml}
                </div>

                <div class="totales">
                    <div class="fila-total final">
                        <div>TOTAL A PAGAR:</div>
                        <div>S/ ${data.total.toFixed(2)}</div>
                    </div>
                </div>

                <hr class="separador-doble">
                <div class="pie">
                    <div class="mensaje">¡GRACIAS POR SU PREFERENCIA!</div>
                    <div class="contacto">www.huellitas.com</div>
                </div>
            </div>
        </body>
        </html>
        `;

        await page.setContent(htmlContent);

        const pdfBuffer = await page.pdf({
            width: '80mm',
            height: '120mm',
            printBackground: true,
            margin: { top: '0px', bottom: '0px', left: '0px', right: '0px' }
        });

        await browser.close();

        res.set({
            'Content-Type': 'application/pdf',
            'Content-Length': pdfBuffer.length
        });
        res.send(pdfBuffer);

    } catch (error) {
        console.error("Error PDF:", error);
        res.status(500).json({ error: "Error al generar boleta: " + error.message });
    }
};

module.exports = { generarBoletaPDF };