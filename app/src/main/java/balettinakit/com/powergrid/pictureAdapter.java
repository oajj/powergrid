package balettinakit.com.powergrid;

/**
 * Created by ollip on 12/7/2017.
 */

public class pictureAdapter {
    public static int adaptPicture(String name){
        switch (name){
            case "fridge":
                return R.drawable.jaakaappi;
            case "mixer":
                return R.drawable.tehosekotin;
            case "kiuas":
                return R.drawable.kiuas;
            case "dishwasher":
                return R.drawable.tiskikone;
            case "computer":
                return R.drawable.tietokone;
            case "washing_machine":
                return R.drawable.tiskikone;
            case "tv":
                return R.drawable.tv;
            case "light":
                return R.drawable.valo;
            case "warmer":
                return R.drawable.sahkolammitys;
            default:
                return R.mipmap.ic_launcher_round;
        }

    }
}
