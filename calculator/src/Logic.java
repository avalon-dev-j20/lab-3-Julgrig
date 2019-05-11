/**
 * Created by Yulia Grigal on 07.05.2019.
 */
public class Logic {

    private double total;

    public Logic() {
        total = 0;
    }

    public String getTotalString() {
        return converting(total);
    }

    public void setTotal(String n) {
        total = convertToNumber(n);
    }

    public void add(String n) {
        total += convertToNumber(n);
    }

    public void subtract(String n) {
        total -= convertToNumber(n);
    }

    public void multiply(String n) {
        total *= convertToNumber(n);
    }

    public void divide(String n) {
        total /= convertToNumber(n);
    }

    private double convertToNumber(String n) {
        return Double.parseDouble(n);
    }

    private String converting(Double aDouble) {
        if ((aDouble % 1) == 0) {
            return String.valueOf(aDouble.intValue());
        } else {
            return String.valueOf(aDouble);
        }
    }
}
