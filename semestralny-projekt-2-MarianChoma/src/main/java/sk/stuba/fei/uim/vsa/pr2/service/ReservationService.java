package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.ParkingHouse;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingPlace;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReservationService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public Reservation createReservation(Reservation reservation) {
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        boolean available=false;
        try{
            ParkingPlace parkingSpot = reservation.getParkingSpot();
            available=parkingSpot.isFree();
        }catch(NullPointerException | NoResultException e){
            return null;
        }

        if (available) {
            TypedQuery<Reservation> q = manager.createQuery("SELECT r FROM RESERVATION r WHERE r.car.id=?1", Reservation.class);
            q.setParameter(1, reservation.getCar().getId());
            boolean canCreate = true;
            try {
                for (int i = 0; i < q.getResultList().size(); i++) {
                    if (( q.getResultList().get(i)).getEnd() == null) {
                        canCreate = false;
                    }
                }
            } catch (NoResultException ignored) {
                return null;
            }
            if (canCreate) {

//                Reservation r = new Reservation();
//                r.setCar((Car) getCar(carId));
//                r.setBeginOfReservation(new Date());
//
//                r.setParkingSpot((ParkingPlace) getParkingSpot(parkingSpotId));
                manager.persist(reservation);

                q = manager.createQuery("UPDATE PARKING_SPOT ps SET ps.free=false WHERE ps.id=?1", Reservation.class);
                q.setParameter(1, reservation.getParkingSpot().getId());
                q.executeUpdate();
                transaction.commit();
                return reservation;
            }
        }
        return null;
    }

    public List<Reservation> getReservations() {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Reservation> q = manager.createQuery("SELECT r FROM RESERVATION r", Reservation.class);

            return q.getResultList();
        }catch (NoResultException e){
            return null;
        }
    }
    public Reservation getReservation(Long id) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Reservation> q = manager.createQuery("SELECT r FROM RESERVATION r WHERE r.id=?1", Reservation.class);
            q.setParameter(1, id);
            return q.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }


    public Object endReservation(Long reservationId) {
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        Reservation r = null;
        Query q;
        try{
            q = manager.createQuery("SELECT r FROM RESERVATION r WHERE r.id= ?1");
            q.setParameter(1, reservationId);
            r = (Reservation) q.getSingleResult();
        }catch(NullPointerException | NoResultException e){
            return null;
        }
        if(r.getEnd()==null){
            Date end = new Date();

            Long hoursToPay = TimeUnit.MILLISECONDS.toHours(end.getTime() - r.getStart().getTime()) + 1;

            q = manager.createQuery("SELECT cp FROM CAR_PARK cp " +
                    "JOIN PARKING_SPOT ps ON ps.carPark.id=cp.id " +
                    "JOIN RESERVATION r ON r.parkingSpot.id=ps.id " +
                    "WHERE r.id=?1");
            q.setParameter(1, reservationId);
            ParkingHouse carPark = (ParkingHouse) q.getSingleResult();

            double price = hoursToPay * carPark.getPrices();

            transaction.begin();
            q = manager.createQuery("UPDATE RESERVATION r SET r.end= ?1, r.prices=?2 WHERE r.id= ?3");
            q.setParameter(1, end);
            q.setParameter(2, price);
            q.setParameter(3, reservationId);
            q.executeUpdate();

            q = manager.createQuery("UPDATE PARKING_SPOT p SET p.free=true WHERE p.id= ?1");
            q.setParameter(1, r.getParkingSpot().getId());
            q.executeUpdate();
            transaction.commit();

            q=manager.createQuery("select r FROM RESERVATION r WHERE r.id= ?1");
            q.setParameter(1,reservationId);
            return q.getSingleResult();
        }
        else{
            return null;
        }
    }

    public List<Reservation> getMyReservations(Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Reservation> q = manager.createQuery("SELECT r from RESERVATION r JOIN CAR c ON c.id=r.car.id WHERE c.owner.id=?1 AND r.end IS NULL", Reservation.class);
            q.setParameter(1, userId);
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Object> getReservations(Long parkingSpotId, Date date) {
        try{
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            LocalDateTime localDay = localDateTime.with(LocalTime.MAX);
            Date endOfDay = Date.from(localDay.atZone(ZoneId.systemDefault()).toInstant());

            EntityManager manager = this.emf.createEntityManager();
            Query q = manager.createQuery("SELECT r FROM RESERVATION r WHERE r.start < ?1 AND r.parkingSpot.id =?2");
            q.setParameter(1, endOfDay);
            q.setParameter(2, parkingSpotId);

            return Arrays.asList(q.getResultList().toArray());
        }catch (NoResultException e){
            return null;
        }
    }
}
