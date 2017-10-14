package dbtype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttributeDateInvl extends Attribute {

    private Date left;
    private Date right;

    static private final SimpleDateFormat dateToStr = AttributeDate.dateToStr;

    public AttributeDateInvl(String s) throws ParseException {
        super(s);

        String[] stringParameters = s.split("\\s+");
        left = dateToStr.parse(stringParameters[0]);
        right = dateToStr.parse(stringParameters[1]);
    }

    @Override
    public String toString() {
        return dateToStr.format(left) + " " + dateToStr.format(right);
    }

    public boolean isInside(Date check) {
        return !left.after(check) && !right.before(check);
    }
}
