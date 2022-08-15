package com.smartContactManager.repository;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("from Contact as c where c.user.id =:userId")
    public Page<Contact> findContactsByUser(@Param("userId") Long userId, Pageable pageable);
    //public List<Contact> findContactsByUser(@Param("userId") Long userId);

    //alternative way: without @Query
    //public List<Contact> findContactByUser(User user);
}
