package com.alura.literalura.sevice;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {

   public String obtenerDatos(String url) {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
      HttpResponse<String> response = null;
      try {
         response = client
               .send(request, HttpResponse.BodyHandlers.ofString());
      } catch (Exception e) {
         throw new RuntimeException("no se pudo obtener los datos requeridos");
      }

      String json = response.body();
      return json;
   }

}
