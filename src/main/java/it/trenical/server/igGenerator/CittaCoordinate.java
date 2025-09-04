package it.trenical.server.igGenerator;

public enum CittaCoordinate {
    LECCE(40.3457, 18.1657),
    BRINDISI(40.6342, 17.93905),
    TARANTO(40.4764, 17.2210),
    MATERA(40.6664, 16.6014),
    BARI(41.1181, 16.8700),
    POTENZA(40.6244, 15.8039),
    FOGGIA(41.4656, 15.5557),
    CAMPOBASSO(41.5610, 14.6684),
    PESCARA(42.4678, 14.2042),
    L_AQUILA(42.3500, 13.3833),
    TERAMO(42.6606, 13.7157),
    RIETI(42.4069, 12.8578),
    ROMA(41.9005, 12.5012),
    VITERBO(42.4253, 12.1063),
    TERNI(42.5636, 12.6427),
    PERUGIA(43.1122, 12.3888),
    AREZZO(43.4621, 11.8826),
    SIENA(43.3188, 11.3308),
    FIRENZE(43.7696, 11.2558),
    PRATO(43.8777, 11.1022),
    BOLOGNA(44.4949, 11.3426),
    MODENA(44.6471, 10.9252),
    PARMA(44.8015, 10.3279),
    REGGIO_EMILIA(44.6983, 10.6301),
    MANTOVA(45.1564, 10.7914),
    VERONA(45.4384, 10.9916),
    VICENZA(45.5455, 11.5354),
    TRENTO(46.0707, 11.1211),
    BOLZANO(46.4983, 11.3548),
    BRESSANONE(46.7157, 11.6586);

    private final double latitudine;
    private final double longitudine;

    CittaCoordinate(double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }
}

