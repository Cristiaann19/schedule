package com.example.schedule.Controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ContactoController {

    private final String NODE_EMAIL_URL = "http://localhost:3000/api/email/send";

    @PostMapping("/contacto/enviar")
    public String enviarMensaje(@RequestParam String nombre, @RequestParam String correo,
            @RequestParam String asunto, @RequestParam String mensaje) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("nombre", nombre);
            body.put("correo", correo);
            body.put("asunto", asunto);
            body.put("mensaje", mensaje);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(NODE_EMAIL_URL, request, String.class);

            return "redirect:/index?contactoExito=true#contacto";
        } catch (Exception e) {
            return "redirect:/index?error=No+se+pudo+enviar+el+mensaje#contacto";
        }
    }
}