package ru.practicum.explorewithme.users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u " +
            "from User u " +
            "group by u.id " +
            "order by count(distinct u.id) desc")
    Page<User> getAllUsersByPage(PageRequest pageRequest);

}
