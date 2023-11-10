package Models;

/**
 * Class for describing InHouse parts
 */
public class InHouse extends Part {

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * MachineID Getter
     * @return machineId
     */
    public int getMachineId() {

        return machineId;
    }

    /**
     * MachineID Setter
     * @param machineId
     */
    public void setMachineId(int machineId) {

        this.machineId = machineId;
    }
}
