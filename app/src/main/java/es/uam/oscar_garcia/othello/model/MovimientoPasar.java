package es.uam.oscar_garcia.othello.model;

import es.uam.eps.multij.Movimiento;

/**
 * Created by oscar on 7/04/17.
 */

public class MovimientoPasar extends Movimiento{
    @Override
    public String toString() {

        return "Pasa movimiento";
    }

    @Override
    public boolean equals(Object o) {
        if(o==null){return false;}
        if(o==this){return true;}

        return o instanceof MovimientoPasar;

    }

}
