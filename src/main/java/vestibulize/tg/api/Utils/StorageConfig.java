package vestibulize.tg.api.Utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class StorageConfig {

    @Bean
    public AmazonS3 amazonS3() {
        String awsAccessKeyId = null;
        String awsSecretKey = null;
        String awsSessionToken = null;
        String awsRegion = null;
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .ignoreIfMissing()
                .load();
            awsAccessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
            awsSecretKey = dotenv.get("AWS_SECRET_KEY");
            awsSessionToken = dotenv.get("AWS_SESSION_TOKEN");
            awsRegion = dotenv.get("AWS_REGION");
        } catch (Exception e) {
            System.out.println("⚠️  Arquivo .env não encontrado. Usando variáveis de ambiente do sistema.");
        }
        
        if (awsAccessKeyId == null || awsAccessKeyId.isEmpty()) {
            awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID");
        }
        if (awsSecretKey == null || awsSecretKey.isEmpty()) {
            awsSecretKey = System.getenv("AWS_SECRET_KEY");
        }
        if (awsSessionToken == null || awsSessionToken.isEmpty()) {
            awsSessionToken = System.getenv("AWS_SESSION_TOKEN");
        }
        if (awsRegion == null || awsRegion.isEmpty()) {
            awsRegion = System.getenv("AWS_REGION");
            if (awsRegion == null || awsRegion.isEmpty()) {
                awsRegion = "us-east-1"; // Default
            }
        }
       
        if (awsAccessKeyId == null || awsSecretKey == null || awsAccessKeyId.isEmpty() || awsSecretKey.isEmpty()) {
            throw new IllegalStateException(
                "AWS credentials not configured. Please set AWS_ACCESS_KEY_ID and AWS_SECRET_KEY " +
                "as environment variables"
            );
        }
        
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion);
        
        if (awsSessionToken != null && !awsSessionToken.isEmpty()) {
            System.out.println("Using temporary credentials with session token (AWS Academy)");
            BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
                awsAccessKeyId, 
                awsSecretKey, 
                awsSessionToken
            );
            builder.withCredentials(new AWSStaticCredentialsProvider(sessionCredentials));
        } else {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
            builder.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
        }
        
        return builder.build();

    }

}