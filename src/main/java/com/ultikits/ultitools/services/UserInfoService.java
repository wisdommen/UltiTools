package com.ultikits.ultitools.services;

import com.ultikits.annotations.ioc.Service;
import com.ultikits.ultitools.dao.UserInfo;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@Service
public class UserInfoService {

    public void addUserInfo(UserInfo userInfo){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        currentSession.persist(userInfo);
    }

    public UserInfo getUserInfoById(String uuid){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        return currentSession.get(UserInfo.class, uuid);
    }

    public List<UserInfo> getUserInfoByName(String username){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        Query<UserInfo> query = currentSession.createQuery("select ui from UserInfo ui where ui.username = :username", UserInfo.class);
        query.setParameter("username", username);
        Transaction transaction = currentSession.getTransaction();
        List<UserInfo> resultList = query.getResultList();
        transaction.commit();
        return resultList;
    }

    public void updateUserInfo(UserInfo userInfo){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        currentSession.persist(userInfo);
    }
}
