package com.example.schedule.Controller;

import com.example.schedule.Model.ConfiguracionWeb;
import com.example.schedule.Service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Controller
@RequestMapping("/admin/web")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionService configService;

    // Directorio donde se guardarán las fotos (Dentro de tu proyecto)
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/gestionar")
    public String gestionarWeb(Model model) {
        // Obtenemos la configuración actual para mostrarla en el HTML
        model.addAttribute("config", configService.obtenerConfiguracion());
        return "redirect:/admin/dashboard?success=Configuracion+actualizada";
    }

    @PostMapping("/logo/subir")
    public String subirLogo(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = guardarArchivo(file);

                // CAMBIO 2: La URL pública ahora empieza con /media/ (según el WebConfig)
                String urlPublica = "/media/" + fileName;

                configService.guardarLogo(urlPublica);
            }
            return "redirect:/admin/web/gestionar?success=Logo+actualizado";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/admin/web/gestionar?error=Error";
        }
    }

    @PostMapping("/slider/agregar")
    public String agregarSlider(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = guardarArchivo(file);
                // URL pública con /media/
                String urlPublica = "/media/" + fileName;
                configService.agregarImagenSlider(urlPublica);
            }
            return "redirect:/admin/web/gestionar?success=Imagen+agregada";
        } catch (IOException e) {
            return "redirect:/admin/web/gestionar?error=Error";
        }
    }

    @GetMapping("/slider/eliminar")
    public String eliminarSlider(@RequestParam("url") String url) {
        configService.eliminarImagenSlider(url);
        return "redirect:/admin/web/gestionar?success=Imagen+eliminada";
    }

    // --- MÉTODO GUARDAR ARCHIVO (MODIFICADO) ---
    private String guardarArchivo(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Agregamos un timestamp para evitar nombres duplicados
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return uniqueFileName;
    }
}