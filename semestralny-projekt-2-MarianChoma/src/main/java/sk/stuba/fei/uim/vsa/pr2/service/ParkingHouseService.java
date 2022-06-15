package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class ParkingHouseService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public List<ParkingHouse> getCarParks() {
        try{
            EntityManager manager = emf.createEntityManager();
            TypedQuery<ParkingHouse> query = manager.createQuery("SELECT cp from CAR_PARK cp", ParkingHouse.class);
            return query.getResultList();
        }
        catch (NoResultException e){
            return null;
        }
    }
    public ParkingHouse getCarParks(String name) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<ParkingHouse> q = manager.createQuery("SELECT cp from CAR_PARK cp WHERE cp.name LIKE ?1", ParkingHouse.class);
            q.setParameter(1, name);
            return q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


    public ParkingHouse getCarPark(Long carParkId) {
        try{
            EntityManager manager = emf.createEntityManager();
            TypedQuery<ParkingHouse> q = manager.createQuery("SELECT cp from CAR_PARK cp WHERE cp.id= ?1", ParkingHouse.class);
            q.setParameter(1, carParkId);
            return q.getSingleResult();
        }catch(NoResultException e){
            return null;
        }

    }

    public ParkingHouse createCarPark(ParkingHouse p) {
        try{
            EntityManager manager = emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(p);
            transaction.commit();
        }
        catch (RollbackException e){
            return null;
        }

        return p;
    }

    public void deleteCarPark(Long carParkId) {
        try{
            EntityManager manager = emf.createEntityManager();
            manager.getTransaction().begin();
            //TypedQuery<ParkingHouse> q = manager.createNamedQuery(ParkingHouse.DELETE_FROM_TABLE, ParkingHouse.class);
            TypedQuery<ParkingHouse> q = manager.createQuery("DELETE FROM CAR_PARK WHERE id=?1", ParkingHouse.class);
            q.setParameter(1, carParkId);
            q.executeUpdate();
            manager.getTransaction().commit();
        }
        catch(NoResultException ignored){
        }
    }
    public ParkingHouse updateCarPark(ParkingHouse carPark) {
        EntityManager manager = emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        if ((carPark).getId() != null) {
            TypedQuery<ParkingHouse> q = manager.createQuery("UPDATE CAR_PARK SET name= ?1, address= ?2, prices= ?3 WHERE id=?4", ParkingHouse.class);
            q.setParameter(1, carPark.getName());
            q.setParameter(2, carPark.getAddress());
            q.setParameter(3, carPark.getPrices());
            q.setParameter(4, carPark.getId());

            q.executeUpdate();
            q = manager.createQuery("SELECT cp from CAR_PARK cp WHERE cp.id=?1", ParkingHouse.class);
            q.setParameter(1, carPark.getId());
            transaction.commit();
            return q.getSingleResult();
        }
        return null;
    }
    public void close() {
        emf.close();
    }
}
