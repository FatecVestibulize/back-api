package vestibulize.tg.api.Service.SupportMaterial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Repository.SupportMaterial.SupportMaterialRepository;
import vestibulize.tg.api.Entity.SupportMaterial;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import vestibulize.tg.api.Service.Storage.S3StorageService;

@Service
public class SupportMaterialService {

    @Autowired
    private SupportMaterialRepository supportMaterialRepository;

    @Autowired
    private S3StorageService s3StorageService;

    public SupportMaterial createSupportMaterial(SupportMaterial supportMaterial, MultipartFile supportMaterialFile, Long userId) {
        
        try {

            String url = s3StorageService.uploadFile(supportMaterialFile, "users/" + userId + "/support_materials");

            supportMaterial.setUrl(url);
            supportMaterial.setTitle(supportMaterialFile.getOriginalFilename());

            return supportMaterialRepository.save(supportMaterial);

        } catch (Exception e) {
            throw new RuntimeException("Error creating support material: " + e.getMessage());
        }   
    }

    public List<SupportMaterial> listSupportMaterial(Long note_id, Long userId) {
        return supportMaterialRepository.listByNoteId(note_id);
    }

    public SupportMaterial updateSupportMaterial(SupportMaterial supportMaterial, Long id) {
        SupportMaterial searchedSupportMaterial = supportMaterialRepository.findById(id).orElse(new SupportMaterial());
        if (searchedSupportMaterial.getId() == null) {
            throw new RuntimeException("Support material not found");
        }

        searchedSupportMaterial.setTitle(supportMaterial.getTitle());

        return supportMaterialRepository.save(searchedSupportMaterial);
    }


    public Boolean deleteSupportMaterial(Long id) {
        supportMaterialRepository.deleteById(id);
        return true;
    }

}
