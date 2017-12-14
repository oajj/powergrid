package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

public class device {

    private String name;
    private String state;
    private String usage;
    private String token;
    private Boolean active;
    private int id;
    private int tier;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    device(String name1, String state1, String usage, int id, int tier) {
        this.id = id;
        name = name1;
        this.tier = tier;
        state = state1;
        this.usage = usage;
    }

    public int getToken() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}