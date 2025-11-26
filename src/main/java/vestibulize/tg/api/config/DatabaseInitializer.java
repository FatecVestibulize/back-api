package vestibulize.tg.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Verifica se já existem categorias no banco
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM category", 
                Integer.class
            );

            if (count != null && count > 0) {
                return;
            }

            ClassPathResource resource = new ClassPathResource("data.sql");
            String sql;
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }

            String[] sqlStatements = sql.split(";");
            
            for (String statement : sqlStatements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    jdbcTemplate.execute(trimmed);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

