package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.ClientSystem;

import java.util.List;
import java.util.Optional;


/**
 * 
 * @author Augustino Mwageni
 *
 */

public interface ClientSystemService {

	public Optional<ClientSystem> findById(Long id);
	
	public List<ClientSystem> findAll();
	
	public ClientSystem createClientSystem(ClientSystem clientSystem);
	
	public ClientSystem updateClientSystem(ClientSystem clientSystem);

	public List<ClientSystem> findAllByClientType(Long clientType);

}
