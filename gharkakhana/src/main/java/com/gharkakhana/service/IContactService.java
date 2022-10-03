package com.gharkakhana.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.gharkakhana.entity.Contact;
import com.gharkakhana.repository.IContactRepository;

@Service
public class IContactService {
	@Autowired
	private IContactRepository IContactRepository;

	public boolean saveMessage(Contact contact) {
		IContactRepository.save(contact);
		return true;
	}
}
