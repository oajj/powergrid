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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    device(String name1, String state1, String condition1, String subject1, Boolean active1) {
        name = name1;
        state = state1;
        usage = condition1;
        token = subject1;
        active = active1;
    }

    public String getToken() {
        return token;
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