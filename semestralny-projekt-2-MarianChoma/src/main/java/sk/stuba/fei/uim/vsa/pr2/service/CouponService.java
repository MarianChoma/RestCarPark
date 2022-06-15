package sk.stuba.fei.uim.vsa.pr2.service;

import sk.stuba.fei.uim.vsa.pr2.domain.Coupon;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class CouponService {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private final UserService userService=new UserService();

    public List<Coupon> getCoupons() {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Coupon> q = manager.createQuery("SELECT c from COUPON c", Coupon.class);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Coupon createCoupon(Coupon coupon) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();

            transaction.begin();
            manager.persist(coupon);
            transaction.commit();
            return coupon;
        } catch (RollbackException e) {
            return null;
        }
    }

    public Coupon getCoupon(Long couponId) {
        try {
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Coupon> q = manager.createQuery("SELECT c from COUPON c WHERE c.id= ?1", Coupon.class);
            q.setParameter(1, couponId);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteCoupon(Long couponId) {

        EntityManager manager = this.emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        TypedQuery<Coupon> q = manager.createQuery("DELETE FROM COUPON WHERE id=?1", Coupon.class);
        q.setParameter(1, couponId);
        q.executeUpdate();
        transaction.commit();
    }

    public Coupon giveCouponToUser(Long couponId, Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            TypedQuery<Coupon> q = manager.createQuery("Update COUPON c SET c.user=?1 where c.id=?2 AND c.user IS NULL", Coupon.class);
            q.setParameter(1, userService.getUseById(userId));
            q.setParameter(2, couponId);
            q.executeUpdate();
            transaction.commit();
            TypedQuery<Coupon> q2 = manager.createQuery("SELECT c FROM COUPON c WHERE c.id=?1", Coupon.class);
            q2.setParameter(1,couponId);
            return q2.getSingleResult();
        }catch (NoResultException ignored){
            return null;
        }
    }

    public List<Coupon> getCoupons(Long userId) {
        try{
            EntityManager manager = this.emf.createEntityManager();
            TypedQuery<Coupon> q = manager.createQuery("SELECT c from COUPON c JOIN USER u ON c.user.id=u.id WHERE c.user.id=?1", Coupon.class);
            q.setParameter(1, userId);
            return q.getResultList();
        }catch(NoResultException e) {
            return null;
        }
    }
}
