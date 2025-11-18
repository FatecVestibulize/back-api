package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Category;
import vestibulize.tg.api.Service.Category.CategoryService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/category")
    public ResponseEntity<List<Category>> listCategories(@RequestHeader(value = "token", required = true) String token) {
        
        jwtUtil.extractId(token);
        List<Category> categories = categoryService.listCategories();

        return ResponseEntity.status(HttpStatus.CREATED).body(categories);
        
        
    }

}