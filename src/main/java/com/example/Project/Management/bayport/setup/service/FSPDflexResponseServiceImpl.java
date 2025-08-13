package com.dflex.emkopo.app.setup.service;

import com.dflex.emkopo.app.setup.entity.FSPDflexResponse;
import com.dflex.emkopo.app.setup.repository.FSPDflexResponseRepository;
import com.dflex.emkopo.app.setup.repository.UtumishiResponseRepository;
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
