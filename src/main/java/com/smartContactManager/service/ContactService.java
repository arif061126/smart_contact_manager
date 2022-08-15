package com.smartContactManager.service;

import com.smartContactManager.entity.Contact;
import com.smartContactManager.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public void deleteContactById(Long cId){
        this.contactRepository.deleteById(cId);
    }
}
