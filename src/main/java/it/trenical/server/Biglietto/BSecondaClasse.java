package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;
import java.util.ArrayList;
import java.util.List;


public class BSecondaClasse extends Biglietto {

    private BSecondaClasse(Builder builder) {
        super(builder);
    }

    public static class Builder extends Biglietto.Builder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BSecondaClasse build() {
            return new BSecondaClasse(this);
        }
    }
}

