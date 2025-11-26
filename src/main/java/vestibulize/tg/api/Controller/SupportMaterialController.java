package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import vestibulize.tg.api.Entity.SupportMaterial;
import vestibulize.tg.api.Service.SupportMaterial.SupportMaterialService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
public class SupportMaterialController {
    @Autowired
    private SupportMaterialService supportMaterialService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/note/{note_id}/support-material", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportMaterial> createSupportMaterial(
        @RequestHeader(value = "token", required = true) String token,
        @PathVariable Long note_id,
        @RequestParam("support_material") MultipartFile supportMaterialFile,
        @RequestParam(value = "title", required = false) String title) {

        try {
            SupportMaterial supportMaterial = new SupportMaterial();
            supportMaterial.setNote_id(note_id);
            supportMaterial.setTitle(title);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(
                supportMaterialService.createSupportMaterial(supportMaterial, supportMaterialFile, jwtUtil.extractId(token))
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupportMaterial());
        }

    }

    @GetMapping("/note/{note_id}/support-material")
    public ResponseEntity<List<SupportMaterial>> listSupportMaterial(@PathVariable Long note_id, @RequestHeader(value = "token", required = true) String token) {
        return ResponseEntity.status(HttpStatus.OK).body(supportMaterialService.listSupportMaterial(note_id, jwtUtil.extractId(token)));
    }

    @PutMapping("/support-material/{id}")
    public ResponseEntity<SupportMaterial> updateSupportMaterial(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody SupportMaterial supportMaterial) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(supportMaterialService.updateSupportMaterial(supportMaterial, id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(supportMaterial);
        }
    }

    @DeleteMapping("/support-material/{id}")
    public ResponseEntity<Boolean> deleteSupportMaterial(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(supportMaterialService.deleteSupportMaterial(id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }

}