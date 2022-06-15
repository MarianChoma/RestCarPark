package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Coupon;
import sk.stuba.fei.uim.vsa.pr2.web.response.CouponDto;

public class CouponResponseFactory implements ResponseFactory<Coupon, CouponDto>{
    @Override
    public CouponDto transformToDto(Coupon entity) {
        CouponDto couponDto=new CouponDto();
        couponDto.setDiscount(entity.getDiscount());
        couponDto.setName(entity.getName());
        return couponDto;
    }

    @Override
    public Coupon transformToEntity(CouponDto dto) {
        return null;
    }
}
