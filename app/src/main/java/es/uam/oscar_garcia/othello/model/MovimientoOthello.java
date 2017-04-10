package es.uam.oscar_garcia.othello.model;

import es.uam.eps.multij.Movimiento;


/**
 * Created by oscar on 1/03/17.
 */

public class MovimientoOthello extends Movimiento {
    protected int fila=0;
    protected int columna=0;


    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Movimiento final fila: "+this.fila+" y columna:"+this.columna+"\n";
    }

    @Override
    public boolean equals(Object o) {
        if(o==null){return false;}
        if(o==this){return true;}

        if (o instanceof MovimientoOthello){
            MovimientoOthello m=(MovimientoOthello)o;
            return this.fila==m.fila && this.columna == m.columna;
        }
        return false;
    }
    /**
     * Constructor del Movimiento
     * @param fila
     * @param columna
     */
    public MovimientoOthello(int fila, int columna) {
        super();
        this.fila=fila;
        this.columna=columna;
    }
}
