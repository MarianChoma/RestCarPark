package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class UserService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public Customer getUseById(Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Customer> q = manager.createQuery("SELECT u from USER u WHERE u.id= ?1", Customer.class);
            q.setParameter(1, userId);
            return q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
    public Customer getUseById(String email) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Customer> q = manager.createQuery("SELECT u from USER u WHERE u.email LIKE ?1", Customer.class);
            q.setParameter(1, email);
            return q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Customer> getUsers() {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Customer> q = manager.createQuery("SELECT u from USER u", Customer.class);
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public Customer createUser(Customer user) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();

            transaction.begin();
            manager.persist(user);
            transaction.commit();
            return user;
        }catch(RollbackException e){
            return null;
        }
    }

    public void deleteUser(Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();

            transaction.begin();
            TypedQuery<Car> q = manager.createQuery("DELETE FROM CAR WHERE owner.id=?1", Car.class);
            q.setParameter(1, userId);
            q.executeUpdate();

            TypedQuery<Customer> q1 = manager.createQuery("DELETE FROM USER WHERE id=?1", Customer.class);
            q1.setParameter(1, userId);
            q1.executeUpdate();

            transaction.commit();
        }catch(NoResultException ignored){
        }
    }

    public Customer updateUser(Customer user) {
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        if (user.getId() != null) {
            TypedQuery<Customer> q = manager.createQuery("UPDATE USER SET firstName= ?1, lastName= ?2, email= ?3 WHERE id=?4", Customer.class);
            q.setParameter(1, user.getFirstName());
            q.setParameter(2, user.getLastName());
            q.setParameter(3, user.getEmail());
            q.setParameter(4, user.getId());
            q.executeUpdate();
            q = manager.createQuery("SELECT u from USER u WHERE u.id=?1", Customer.class);
            q.setParameter(1, user.getId());
            transaction.commit();

            return q.getSingleResult();
        }
        return null;
    }

}
