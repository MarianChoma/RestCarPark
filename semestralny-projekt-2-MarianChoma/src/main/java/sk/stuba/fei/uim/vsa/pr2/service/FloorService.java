package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.Floor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class FloorService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public List<Floor> getCarParkFloors(Long carParkId) {
        try {
            EntityManager manager = emf.createEntityManager();
            TypedQuery<Floor> q = manager.createQuery("SELECT f from CAR_PARK_FLOOR f WHERE f.carPark.id= ?1", Floor.class);
            q.setParameter(1, carParkId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Floor getCarParkFloor(Long carParkFloorId) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Floor> q = manager.createQuery("SELECT f from CAR_PARK_FLOOR f WHERE f.id= ?1", Floor.class);
            q.setParameter(1, carParkFloorId);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Floor createCarParkFloor(Floor f) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(f);
            transaction.commit();
            return f;
        } catch (RollbackException e) {
            return null;
        }
    }

    public void deleteCarParkFloor(Long carParkId, String floorIdentifier) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            TypedQuery<Floor> q = manager.createQuery("DELETE FROM CAR_PARK_FLOOR f WHERE f.carPark.id=?1 and f.identifier LIKE ?2", Floor.class);
            q.setParameter(1, carParkId);
            q.setParameter(2, floorIdentifier);
            q.executeUpdate();
            transaction.commit();
        } catch (NoResultException ignored) {
        }
    }

    public Floor updateCarParkFloor(Floor carParkFloor, String floorIdentifier) {
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        TypedQuery<Floor> q = manager.createQuery("UPDATE CAR_PARK_FLOOR f SET f.identifier=?1 WHERE f.carPark.id=?2 and f.identifier LIKE ?3", Floor.class);
        q.setParameter(1, carParkFloor.getIdentifier());
        q.setParameter(2, carParkFloor.getCarPark().getId());
        q.setParameter(3, floorIdentifier);

        q.executeUpdate();
        try {
            q = manager.createQuery("SELECT f from CAR_PARK_FLOOR f WHERE f.identifier LIKE ?1 and f.carPark.id=?2", Floor.class);
            q.setParameter(1, carParkFloor.getIdentifier());
            q.setParameter(2, carParkFloor.getCarPark().getId());
            transaction.commit();
            return q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
    public Floor getByIdentifier(Long id, String identifier){
        EntityManager manager = this.emf.createEntityManager();
        TypedQuery<Floor> q = manager.createQuery("SELECT f from CAR_PARK_FLOOR f WHERE f.identifier LIKE ?1 and f.carPark.id=?2", Floor.class);
        q.setParameter(1, identifier);
        q.setParameter(2, id);
        return (Floor) q.getSingleResult();
    }
}
