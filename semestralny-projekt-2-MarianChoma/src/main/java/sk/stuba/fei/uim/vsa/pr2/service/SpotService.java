package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.Floor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class SpotService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public List<ParkingPlace> getParkingSpots(Long carParkId) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<ParkingPlace> q = manager.createQuery("SELECT p from PARKING_SPOT p WHERE p.carPark.id= ?1", ParkingPlace.class);
            q.setParameter(1, carParkId);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<ParkingPlace> getParkingSpots(Long carParkId, String free) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<ParkingPlace> q = manager.createQuery("SELECT p from PARKING_SPOT p WHERE p.carPark.id= ?1 AND p.free=?2", ParkingPlace.class);
            q.setParameter(1, carParkId);
            q.setParameter(2,Boolean.parseBoolean(free));
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Object createParkingSpot(ParkingPlace p) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(p);
            transaction.commit();
            return p;
        } catch (RollbackException | NoResultException e) {
            return null;
        }
    }

    public ParkingPlace getParkingSpot(Long parkingSpotId) {
        try {
            EntityManager manager = emf.createEntityManager();
            TypedQuery<ParkingPlace> q = manager.createQuery("SELECT p from PARKING_SPOT p WHERE p.id= ?1", ParkingPlace.class);
            q.setParameter(1, parkingSpotId);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteParkingSpot(Long parkingSpotId) {
        EntityManager manager = emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        TypedQuery<ParkingPlace> q = manager.createQuery("DELETE FROM RESERVATION r WHERE r.parkingSpot.id=?1", ParkingPlace.class);
        q.setParameter(1, parkingSpotId);
        q.executeUpdate();
        q = manager.createQuery("DELETE FROM PARKING_SPOT WHERE id=?1", ParkingPlace.class);
        q.setParameter(1, parkingSpotId);
        q.executeUpdate();
        transaction.commit();
    }

    public List<ParkingPlace> getParkingSpotsByIdentifier(Long carParkId, String floorIdentifier) {
        try {
            EntityManager manager = emf.createEntityManager();
            TypedQuery<ParkingPlace> q = manager.createQuery("SELECT p from PARKING_SPOT p WHERE p.carPark.id= ?1 AND p.carParkFloor.identifier LIKE ?2", ParkingPlace.class);
            q.setParameter(1, carParkId);
            q.setParameter(2, floorIdentifier);
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }
    public Floor getFloorBySpotId(Long id){
        EntityManager manager = emf.createEntityManager();
        TypedQuery<Floor> q = manager.createQuery("SELECT f from CAR_PARK_FLOOR f Join PARKING_SPOT p ON p.carParkFloor.id=f.id WHERE p.id= ?1", Floor.class);
        q.setParameter(1, id);
        return q.getSingleResult();
    }

    public ParkingPlace updateParkingSpot(ParkingPlace parkingSpot) {
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        TypedQuery<ParkingPlace> q = manager.createQuery("UPDATE PARKING_SPOT ps SET ps.identifier=?1, ps.free=?2 WHERE ps.id=?3", ParkingPlace.class);
        q.setParameter(1,  parkingSpot.getIdentifier());
        q.setParameter(2,  parkingSpot.isFree());
        q.setParameter(3, parkingSpot.getId());
        q.executeUpdate();
        transaction.commit();
        return getParkingSpot(parkingSpot.getId());
    }
}
