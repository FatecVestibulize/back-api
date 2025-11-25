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

    Dotenv dotenv = Dotenv.load();

    private String awsAccessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
    private String awsSecretKey = dotenv.get("AWS_SECRET_KEY");
    private String awsSessionToken = dotenv.get("AWS_SESSION_TOKEN");
    private String awsRegion = dotenv.get("AWS_REGION");

    @Bean
    public AmazonS3 amazonS3() {
       
        if (awsAccessKeyId == null || awsSecretKey == null || awsAccessKeyId.isEmpty() || awsSecretKey.isEmpty()) {
            throw new IllegalStateException(
                "AWS credentials not configured. Please set aws.access.key.id and aws.secret.key " +
                "in application.properties or as environment variables"
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