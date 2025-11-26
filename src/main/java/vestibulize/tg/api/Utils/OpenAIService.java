package vestibulize.tg.api.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public OpenAIService() {
        String apiKey = null;
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .ignoreIfMissing()
                .load();
            apiKey = dotenv.get("OPENAI_API_KEY");
        } catch (Exception e) {
        }
        
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getProperty("OPENAI_API_KEY", System.getenv("OPENAI_API_KEY"));
        }
        
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public String gerarResumo(String texto) {

        // ----------- PROMPT (idêntico ao seu, seguro) -----------
        String prompt = """
Você é um assistente especializado em analisar anotações de um caderno digital e gerar resumos claros, coerentes e objetivos.
Seu papel é:

1. Ler todas as anotações fornecidas.
2. Unificar o conteúdo, removendo redundâncias e contradições.
3. Identificar inconsistências, informações confusas ou trechos sem sentido.
4. Produzir um resumo claro, objetivo e fiel ao que foi escrito.
5. Não inventar informações externas.
6. Retornar a resposta sempre no formato de texto corrido, em tópicos descrito abaixo.

------------------------------------------
IMPORTANTE – INSTRUÇÕES DE COMPORTAMENTO
------------------------------------------

• Nunca adicione informações que não estejam nas anotações.
• Nunca preencha lacunas inventando detalhes.
• Se algo estiver incompleto, confuso ou contraditório, informe isso explicitamente.
• Mantenha tom neutro, profissional e didático.
• Evite repetições e jargões desnecessários.
• Priorize frases curtas, diretas e fáceis de entender.
• Não tente interpretar além daquilo que os textos permitem.
• Se houver interpretações possíveis, liste-as.
• Não traga opiniões pessoais.

------------------------------------------
ESTILO DO TEXTO
------------------------------------------

• O resumo deve ser:
  - extremamente objetivo
  - claro
  - coerente
  - organizado
  - em linguagem simples
  - sem enrolação

• Caso as anotações sejam muito longas, divida o resumo em tópicos.
• Utilize texto corrido apenas quando fizer sentido para o contexto.

------------------------------------------
AGORA ANALISE AS ANOTAÇÕES DO USUÁRIO
------------------------------------------

A seguir estão as anotações que devem ser analisadas:

%s

Gere o resultado final seguindo todas as instruções acima, em texto corrido e em tópicos.
""".formatted(texto);

        // ----------- Body agora seguro e 100% válido -----------
        Map<String, Object> requestBody = Map.of(
            "model", "gpt-4o-mini",
            "temperature", 0.6,
            "messages", List.of(
                Map.of(
                    "role", "user",
                    "content", prompt
                )
            )
        );

        try {
            String response = webClient.post()
                .body(Mono.just(requestBody), Map.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            JsonNode raiz = objectMapper.readTree(response);
            return raiz
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

        } catch (Exception e) {
            return "Erro ao chamar OpenAI: " + e.getMessage();
        }
    }
}
