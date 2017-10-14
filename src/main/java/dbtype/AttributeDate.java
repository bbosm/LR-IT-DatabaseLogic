package dbtype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttributeDate extends Attribute {

    private Date val;

    static public final SimpleDateFormat dateToStr = new SimpleDateFormat("yyyy.MM.dd");

    public AttributeDate(String s) throws ParseException {
        super(s);
        val = dateToStr.parse(s);
    }

    @Override
    public String toString() {
        return dateToStr.format(val);
    }
}
