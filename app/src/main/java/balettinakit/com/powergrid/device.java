package balettinakit.com.powergrid;

/**
 * Created by ollip on 11/25/2017.
 */

public class device {
    private String name;
    private String state;
    private String usage;
    private String hash;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

   

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    device(String name1, String state1, String condition1, String subject1, String price1){
        id = price1;
        name = name1;
        state = state1;
        usage = condition1;
        hash = subject1;
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