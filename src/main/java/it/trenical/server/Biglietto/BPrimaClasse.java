package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;
import java.util.ArrayList;
import java.util.List;


public class BPrimaClasse extends Biglietto {

    private BPrimaClasse(Builder builder) {
        super(builder);
    }

    public static class Builder extends Biglietto.Builder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BPrimaClasse build() {
            return new BPrimaClasse(this);
        }
    }
}
