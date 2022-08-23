package com.ultikits.ultitools.dao;

import com.ultikits.annotations.ioc.Service;
import com.ultikits.ultitools.entity.UserInfoEntity;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@Service
public class UserInfoDAO {

    public void addUserInfo(UserInfoEntity userInfoEntity){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        currentSession.persist(userInfoEntity);
    }

    public UserInfoEntity getUserInfoById(String uuid){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        return currentSession.get(UserInfoEntity.class, uuid);
    }

    public List<UserInfoEntity> getUserInfoByName(String username){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        Query<UserInfoEntity> query = currentSession.createQuery("select ui from UserInfo ui where ui.username = :username", UserInfoEntity.class);
        query.setParameter("username", username);
        Transaction transaction = currentSession.getTransaction();
        List<UserInfoEntity> resultList = query.getResultList();
        transaction.commit();
        return resultList;
    }

    public void updateUserInfo(UserInfoEntity userInfoEntity){
        Session currentSession = UltiTools.getSessionFactory().getCurrentSession();
        currentSession.persist(userInfoEntity);
    }
}
