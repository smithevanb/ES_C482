package model;

/**
 * RUNTIME ERROR: This class caused some Invocation Exception Errors, being a subclass of Part class, until the concept
 * of casting was better understood and implemented.
 */

public class Outsourced extends Part{

    /** FUTURE ENHANCEMENT: This incredibly small class could be improved with some methods specific to Outsourced parts, upon
     * an expansion of the whole application. What if a part is available from more than one Company? It would not be
     * difficult to image such a scenario. */

    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    public String getCompanyName() {

        return companyName;
    }

    public void setCompanyName(String companyName) {

        this.companyName = companyName;
    }
}
