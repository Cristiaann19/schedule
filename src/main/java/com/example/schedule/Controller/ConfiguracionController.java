package com.example.schedule.Controller;

import com.example.schedule.Model.ConfiguracionWeb;
import com.example.schedule.Service.ConfiguracionService;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/web")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionService configService;

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/gestionar")
    public String gestionarWeb(Model model) {
        model.addAttribute("config", configService.obtenerConfiguracion());
        return "redirect:/admin/dashboard?success=Configuracion+actualizada";
    }

    @PostMapping("/logo/subir")
    public String subirLogo(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = guardarArchivo(file);

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

    private String guardarArchivo(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
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

    @GetMapping("/api/imagenes")
    @ResponseBody
    public List<String> listarImagenesLocales() {
        List<String> urls = new ArrayList<>();
        try {
            Path folderPath = Paths.get(UPLOAD_DIR);
            if (Files.exists(folderPath)) {
                // Aquí usamos java.util.stream.Stream
                try (Stream<Path> paths = Files.walk(folderPath, 1)) {
                    urls = paths
                            .filter(Files::isRegularFile)
                            .map(path -> "/media/" + path.getFileName().toString())
                            .collect(Collectors.toList());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    // --- NUEVO 2: Seleccionar Logo existente ---
    @PostMapping("/logo/seleccionar")
    public String seleccionarLogoExistente(@RequestParam("url") String url) {
        configService.guardarLogo(url);
        return "redirect:/admin/dashboard?success=Logo+cambiado";
    }

    // --- NUEVO 3: Agregar imagen existente al Slider ---
    @PostMapping("/slider/seleccionar")
    public String seleccionarSliderExistente(@RequestParam("url") String url) {
        configService.agregarImagenSlider(url);
        return "redirect:/admin/dashboard?success=Imagen+agregada+al+slider";
    }

    @GetMapping("/api/imagenes/borrar")
    @ResponseBody
    public ResponseEntity<?> borrarImagenFisica(@RequestParam("url") String url) {
        try {
            // URL viene como "/media/foto.png", extraemos solo el nombre
            String nombreArchivo = url.substring(url.lastIndexOf("/") + 1);
            Path rutaArchivo = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);

            boolean borrado = Files.deleteIfExists(rutaArchivo);

            if (borrado) {
                // Opcional: También deberíamos quitarla del slider si se estaba usando
                configService.eliminarImagenSlider(url);
                return ResponseEntity.ok().body("{\"status\": \"ok\"}");
            } else {
                return ResponseEntity.badRequest()
                        .body("{\"status\": \"error\", \"message\": \"Archivo no encontrado\"}");
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("{\"status\": \"error\"}");
        }
    }
}