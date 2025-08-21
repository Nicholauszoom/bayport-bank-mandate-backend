package com.example.Project.Management.bayport.setup.service;

import com.example.Project.Management.bayport.setup.entity.FSPDflexResponse;
import com.example.Project.Management.bayport.setup.repository.FSPDflexResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class FSPDflexResponseServiceImpl implements FSPDflexResponseService{

    FSPDflexResponseRepository fspDflexResponseRepository;

    @Autowired
    public FSPDflexResponseServiceImpl(FSPDflexResponseRepository fspDflexResponseRepository) {
        this.fspDflexResponseRepository = fspDflexResponseRepository;
    }

    @Override
    public FSPDflexResponse saveFSPDflexResponse(FSPDflexResponse fspDflexResponse) {
        return null;
    }
}
