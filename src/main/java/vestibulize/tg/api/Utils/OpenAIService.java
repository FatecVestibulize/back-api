package vestibulize.tg.api.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public OpenAIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String gerarResumo(String texto) {
        String prompt = "Resuma o seguinte texto de forma clara e objetiva: %s".formatted(texto);
        String bodyJson = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {
              "role": "user",
              "content": "%s"
            }
          ],
          "temperature": 0.6
        }
        """.formatted(prompt);

        try {
            String response = webClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .body(Mono.just(bodyJson), String.class)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // retorna String pura

            JsonNode raiz = objectMapper.readTree(response);
            String resumo = raiz
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

            return resumo;
        
        } catch (Exception e) {
            return "Erro ao chamar OpenAI: " + e.getMessage();
        }
    }
}
