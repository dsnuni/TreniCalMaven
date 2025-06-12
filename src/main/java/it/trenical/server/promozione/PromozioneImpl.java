package it.trenical.server.promozione;


import java.util.List;

public interface PromozioneImpl {

    void setPromozione(Promozione promo);

    boolean removePromozione(String trenoID);

    List<Promozione> getAllPromozioni();
}