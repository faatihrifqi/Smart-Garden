package erwin.com.smartgarden.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kelembaban {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("nilai")
    @Expose
    private String nilai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

}
