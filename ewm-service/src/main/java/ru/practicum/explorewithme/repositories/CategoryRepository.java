package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.models.category.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c " +
            "from Category c " +
            "where c.id in ?1 " +
            "group by c.id " +
            "order by c.id desc")
    List<Category> getCategoriesFromIds(List<Long> categories);

    @Query("select c " +
            "from Category c " +
            "group by c.id " +
            "order by count(distinct c.id) desc")
    Page<Category> getAllCategoriesByPage(PageRequest pageRequest);

}
