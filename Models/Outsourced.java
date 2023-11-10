package Models;

/**
 * Class for describing Outsourced Parts
 */
public class Outsourced extends Part {

    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Company Name Getter
     * @return
     */
    public String getCompanyName() {

        return companyName;
    }

    /**
     * Company Name Setter
     * @param companyName
     */
    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }
}
