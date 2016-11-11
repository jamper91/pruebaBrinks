package co.jamper91.pruebabrinks.objetos;

import java.util.LinkedList;

/**
 * Created by Desarrollador on 21/05/2016.
 */
public class Aplicacion {
    private String artist;
    private String price;
    private String summary;
    private String category_id;
    private String title;
    private String imagenen_url;
    private String link;
    private String rights;
    private String contentType;
    private String name;
    private String id;
    private String fecha;
    private String fecha2;


    public Aplicacion() {
    }

    public Aplicacion(String artist, String price, String summary, String category_id, String title, String imagen_url, String link, String rights, String contentType, String name, String id, String fecha, String fecha2) {
        this.artist = artist;
        this.price = price;
        this.summary = summary;
        this.category_id = category_id;
        this.title = title;
        this.imagenen_url = imagen_url;
        this.link = link;
        this.rights = rights;
        this.contentType = contentType;
        this.name = name;
        this.id = id;
        this.fecha = fecha;
        this.fecha2 = fecha2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagenen_url() {
        return imagenen_url;
    }

    public void setImagenen_url(String imagenen_url) {
        this.imagenen_url = imagenen_url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }

    public LinkedList<String[]>get_fields()
    {
        LinkedList<String[]> campos = new LinkedList<>();
        String[] campo = new String[]{"artist",  "'"+getArtist()+ "'"};
        campos.add(campo);
        campo = new String[]{"price", "'"+getPrice()+"'"};
        campos.add(campo);
        campo = new String[]{"summary", "'"+getSummary()+"'"};
        campos.add(campo);
        campo = new String[]{"category_id", getCategory_id()};
        campos.add(campo);
        campo = new String[]{"title", "'"+getTitle()+"'"};
        campos.add(campo);
        campo = new String[]{"imagenen_url", "'"+getImagenen_url()+"'"};
        campos.add(campo);
        campo = new String[]{"link","'"+getLink()+"'"};
        campos.add(campo);
        campo = new String[]{"rights","'"+getRights()+"'"};
        campos.add(campo);
        campo = new String[]{"contentType","'"+getContentType()+"'"};
        campos.add(campo);
        campo = new String[]{"name","'"+getName()+"'"};
        campos.add(campo);
        campo = new String[]{"id",getId()};
        campos.add(campo);
        campo = new String[]{"fecha","'"+getFecha()+"'"};
        campos.add(campo);
        campo = new String[]{"fecha2","'"+getFecha2()+"'"};
        campos.add(campo);

        return campos;
    }
}
