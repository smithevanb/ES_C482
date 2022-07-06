package model;

/**
 * RUNTIME ERROR: This class caused some Invocation Exception Errors, being a subclass of Part class, until the concept
 * of casting was better understood and implemented
 *
 *
 */

public class InHouse extends Part{

    /** FUTURE ENHANCEMENT: This incredibly small class could be improved with some methods specific to In-House parts, upon
     * an expansion of the whole application. Perhaps some machines have a certain throughput or cost to operate? This
     * could impact the cost/price of these items.
     */

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    public int getMachineId() {

        return machineId;
    }

    public void setMachineId(int machineId) {

        this.machineId = machineId;
    }
}
