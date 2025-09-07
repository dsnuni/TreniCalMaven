package it.trenical.server.promozione;


import java.util.List;

public interface PromozioneImpl {

    void setPromozione(Promozione promo);

    boolean removePromozione(String trenoID);
    boolean removePromozionePID(String promozioneID);
    List<Promozione> getAllPromozioni();
}