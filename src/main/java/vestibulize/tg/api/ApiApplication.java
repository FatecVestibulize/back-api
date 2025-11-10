package vestibulize.tg.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.filename(".env")
				.ignoreIfMissing()
				.load();
			
			dotenv.entries().forEach(entry -> {
				System.setProperty(entry.getKey(), entry.getValue());
			});
		} catch (Exception e) {
			System.out.println("Arquivo .env não encontrado. Usando variáveis de ambiente do sistema.");
		}
		
		SpringApplication.run(ApiApplication.class, args);
	}

}
