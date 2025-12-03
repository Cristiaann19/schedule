const ExcelJS = require('exceljs');

const generarReporteVentas = async (req, res) => {
    const ventas = req.body;

    try {
        const workbook = new ExcelJS.Workbook();
        const worksheet = workbook.addWorksheet('Reporte de Ventas');

        worksheet.columns = [
            { header: 'ID Venta', key: 'id', width: 10 },
            { header: 'Fecha', key: 'fecha', width: 20 },
            { header: 'Cliente', key: 'cliente', width: 30 },
            { header: 'DNI', key: 'dni', width: 15 },
            { header: 'Método Pago', key: 'metodo', width: 15 },
            { header: 'Total (S/)', key: 'total', width: 15 },
            { header: 'Estado', key: 'estado', width: 15 }
        ];

        const headerRow = worksheet.getRow(1);
        headerRow.font = { bold: true, color: { argb: 'FFFFFFFF' } };
        headerRow.fill = {
            type: 'pattern',
            pattern: 'solid',
            fgColor: { argb: 'FF10B981' }
        };
        headerRow.commit();

        if (Array.isArray(ventas)) {
            ventas.forEach(v => {
                worksheet.addRow({
                    id: v.id,
                    fecha: v.fecha,
                    cliente: v.clienteNombre || v.cliente,
                    dni: v.clienteDni || v.dni,
                    metodo: v.metodoPago,
                    total: v.total,
                    estado: v.estado
                });
            });
        }

        res.setHeader('Content-Type', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
        res.setHeader('Content-Disposition', 'attachment; filename=reporte_ventas.xlsx');

        await workbook.xlsx.write(res);
        res.end();

    } catch (error) {
        console.error("Error generando Excel:", error);
        res.status(500).json({ error: "Error al generar Excel" });
    }
};

module.exports = { generarReporteVentas };