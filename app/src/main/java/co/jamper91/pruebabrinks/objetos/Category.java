package co.jamper91.pruebabrinks.objetos;

import java.util.LinkedList;

/**
 * Created by Desarrollador on 21/05/2016.
 */
public class Category {

    private String id;
    private String name;

    public Category() {
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public LinkedList<String[]> get_fields()
    {
        LinkedList<String[]> campos = new LinkedList<>();
        String[] campo = new String[]{"name","'"+getName()+"'"};
        campos.add(campo);
        campo = new String[]{"id",getId()};
        campos.add(campo);

        return campos;
    }
}
