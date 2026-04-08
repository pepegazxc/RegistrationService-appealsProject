package main.service;

import main.entity.UsersEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MayorRequestService {

    @Transactional
    public void addMayorRequest(UsersEntity user){

    }
}
