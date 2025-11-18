package vestibulize.tg.api.Service.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Repository.Category.CategoryRepository;
import java.util.ArrayList;
import vestibulize.tg.api.Entity.Category;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public ArrayList<Category> listCategories() {
        return categoryRepository.listAll();
    }

}
