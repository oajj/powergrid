package balettinakit.com.powergrid;

/**
 * Created by Olli Peura on 11/25/2017.
 */

public class device {

    private String name;
    private String state;
    private String usage;
    private int id;

    public int getTier() {
        return tier;
    }

    private int tier;

    public void setId(int id) {
        this.id = id;
    }

    device(String name, String state, String usage, int id, int tier) {
        this.id = id;
        this.name = name;
        this.tier = tier;
        this.state = state;
        this.usage = usage;
    }

    public int getId() {
        return id;
    }

     String getUsage() {
        return usage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getState() {
        return state;
    }

    void setState(String state) {
        this.state = state;
    }
}