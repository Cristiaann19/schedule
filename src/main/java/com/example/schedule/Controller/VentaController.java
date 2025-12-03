package com.example.schedule.Controller;

import com.example.schedule.Model.DetalleVenta;
import com.example.schedule.Model.Venta;
import com.example.schedule.Service.VentaService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/admin/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    private final String NODE_PDF_URL = "http://localhost:3000/api/pdf/boleta";
    private final String NODE_EXCEL_URL = "http://localhost:3000/api/excel/ventas";

    @GetMapping("/aprobar/{id}")
    public String aprobarVenta(@PathVariable Long id) {
        ventaService.aprobarVenta(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Venta obtenerVenta(@PathVariable Long id) {
        return ventaService.obtenerPorId(id);
    }

    @GetMapping("/reporte/excel")
    public ResponseEntity<byte[]> descargarReporteExcel() {
        List<Venta> ventas = ventaService.listarTodas();

        List<Map<String, Object>> listaParaEnviar = new ArrayList<>();

        for (Venta v : ventas) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("id", v.getId());
            mapa.put("fecha", v.getFecha().toString());
            mapa.put("cliente", v.getCliente().getNombres() + " " + v.getCliente().getApellidos());
            mapa.put("dni", v.getCliente().getDni());
            mapa.put("metodoPago", v.getMetodoPago());
            mapa.put("total", v.getTotal());
            mapa.put("estado", v.getEstado());
            listaParaEnviar.add(mapa);
        }

        RestTemplate restTemplate = new RestTemplate();
        byte[] excelBytes = restTemplate.postForObject(NODE_EXCEL_URL, listaParaEnviar, byte[].class);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ventas_huellitas.xlsx")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }

    @GetMapping("/boleta/{id}")
    public ResponseEntity<byte[]> descargarBoleta(@PathVariable Long id) {

        Venta venta = ventaService.obtenerPorId(id);
        if (venta == null)
            return ResponseEntity.notFound().build();
        Map<String, Object> body = new HashMap<>();
        body.put("id", String.format("V-%05d", venta.getId()));
        body.put("fecha", venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        body.put("cliente", venta.getCliente().getNombres() + " " + venta.getCliente().getApellidos());
        body.put("dni", venta.getCliente().getDni());
        body.put("metodoPago", venta.getMetodoPago());
        body.put("total", venta.getTotal());

        List<Map<String, Object>> items = new ArrayList<>();
        for (DetalleVenta d : venta.getDetalles()) {
            Map<String, Object> item = new HashMap<>();
            item.put("nombre", d.getNombreProducto());
            item.put("cantidad", d.getCantidad());
            item.put("subtotal", d.getSubtotal());
            items.add(item);
        }
        body.put("items", items);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        byte[] pdfBytes = restTemplate.postForObject(NODE_PDF_URL, request, byte[].class);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=boleta_" + venta.getId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}