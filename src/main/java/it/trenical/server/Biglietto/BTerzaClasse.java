package it.trenical.server.Biglietto;

import it.trenical.server.Cliente.*;
import it.trenical.server.Treno.*;
import java.util.ArrayList;
import java.util.List;



public class BTerzaClasse extends Biglietto {

    private BTerzaClasse(Builder builder) {
        super(builder);
    }

    public static class Builder extends Biglietto.Builder{


        @Override
        public BTerzaClasse build() {
            return new BTerzaClasse(this);
        }
    }
}
