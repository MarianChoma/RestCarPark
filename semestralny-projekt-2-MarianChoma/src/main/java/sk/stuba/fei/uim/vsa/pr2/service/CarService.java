package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Customer;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class CarService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

    public Car createCar(Car car) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();

            transaction.begin();
            manager.persist(car);
            transaction.commit();
            return car;
        } catch (RollbackException e) {
            return null;
        }
    }

    public Car getCarById(Long id) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Car> q = manager.createQuery("SELECT car from CAR car WHERE car.id= ?1", Car.class);
            q.setParameter(1, id);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<Car> getCars(){
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Car> q = manager.createQuery("SELECT car from CAR car", Car.class);
            return q.getResultList();
        }catch (NoResultException e) {
            return null;
        }
    }
    public List<Car> getCars(Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Car> q = manager.createQuery("SELECT c from CAR c WHERE c.owner.id= ?1", Car.class);
            q.setParameter(1, userId);
            return q.getResultList();
        }catch(NoResultException r){
            return null;
        }
    }
    public List<Car>  getCars(String vehicleRegistrationPlate) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Car> q = manager.createQuery("SELECT car from CAR car WHERE car.vrp= ?1", Car.class);
            q.setParameter(1, vehicleRegistrationPlate);
            return q.getResultList();
        }catch(NoResultException e){
            return null;
        }
    }

    public void deleteCar(Long id) {

        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        TypedQuery<Car> q = manager.createQuery("DELETE FROM CAR WHERE id=?1", Car.class);
        q.setParameter(1, id);
        q.executeUpdate();
        transaction.commit();
    }

    public Car updateCar(Car car){
        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        if (car.getId() != null) {
            TypedQuery<Car> q = manager.createQuery("UPDATE CAR SET brand= ?1, model= ?2, colour= ?3, vrp=?4 WHERE CAR.id=?5", Car.class);
            q.setParameter(1, car.getBrand());
            q.setParameter(2, car.getModel());
            q.setParameter(3, car.getColour());
            q.setParameter(4, car.getVrp());
            q.setParameter(5, car.getId());
            q.executeUpdate();
            q = manager.createQuery("SELECT c FROM CAR c WHERE c.id=?1", Car.class);
            q.setParameter(1, car.getId());
            transaction.commit();

            return q.getSingleResult();
        }
        return null;
    }
}
