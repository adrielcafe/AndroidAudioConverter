package cafe.adriel.androidaudioconverter.model;

public enum BitRate {
    def, //Stands for default case
    u8,
    s16,
    s32,
    flt,
    dbl,
    u8p,
    s16p,
    s32p,
    fltp,
    dblp;

    public String getBitRate() {
        return name().toLowerCase();
    }
}