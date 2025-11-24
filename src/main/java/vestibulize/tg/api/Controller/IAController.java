package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import vestibulize.tg.api.Entity.Goal;
import vestibulize.tg.api.Utils.OpenAIService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ia")
public class IAController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/resumo")
    public ResponseEntity<String> gerarResumo(@RequestBody Map<String, String> req) {
        String texto = req.get("texto");

        String resumo = openAIService.gerarResumo(texto);

        return ResponseEntity.status(HttpStatus.OK).body(resumo);
    }
}
