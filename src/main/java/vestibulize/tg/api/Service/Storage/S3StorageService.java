package vestibulize.tg.api.Service.Storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3StorageService {

    private final AmazonS3 amazonS3;
    private String bucketName;

    public S3StorageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        
        try {
            Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .filename(".env")
                .ignoreIfMissing()
                .load();
            bucketName = dotenv.get("AWS_S3_BUCKET");
        } catch (Exception e) {
            System.out.println("⚠️  Arquivo .env não encontrado para S3. Usando variáveis de ambiente do sistema.");
        }
        
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = System.getenv("AWS_S3_BUCKET");
        }
        
    }

    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, "uploads");
    }

    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        
        String fileName = folderPath + "/" + UUID.randomUUID().toString() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, 
                fileName, 
                inputStream, 
                metadata
            );

            amazonS3.putObject(putObjectRequest);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (Exception e) {
            throw new IOException("Error uploading file to S3: " + e.getMessage(), e);
        }
    }

    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

}