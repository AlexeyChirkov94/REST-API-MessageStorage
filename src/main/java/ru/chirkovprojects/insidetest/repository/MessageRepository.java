package ru.chirkovprojects.insidetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.chirkovprojects.insidetest.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    @Modifying
    @Query(value = "select * from messages where author_id =:authorId order by datetime_of_message desc FETCH NEXT :countOfNotes ROWS ONLY", nativeQuery = true)
    List<Message> findLastMessageByAuthorId(Long authorId, Integer countOfNotes);

}
