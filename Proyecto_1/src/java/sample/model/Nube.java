package sample.model;

import java.util.Observable;

public class Nube extends Observable implements Runnable {
    private EjeNube ejeNube;
    private boolean status;

    public boolean estatus;

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Nube() {
    estatus=true;
    }

    public EjeNube getEjeNube() {
        return ejeNube;
    }

    public void setEjeNube(EjeNube ejeNube) {
        this.ejeNube = ejeNube;
    }

    @Override
    public void run() {
        while (estatus){
            ejeNube.setX(ejeNube.getX()-10);

            setChanged();
            notifyObservers(ejeNube);
            try {
                Thread.sleep(50l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
