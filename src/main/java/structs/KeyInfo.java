package structs;

/**
 * Class KeyInfo
 */
public class KeyInfo {
    private String name;
    private Runnable accion;
    private double cooldown;

    /**
     * Constructor for KeyInfo
     * @param name Name of the key
     * @param accion Action of the key
     */
    public KeyInfo(String name, Runnable accion) {
        this.name = name;
        this.accion = accion;
        cooldown = 0;
    }

    /**
     * Get the name of the key
     * @return Name of the key
     */
    public String getName() {
        return name;
    }

    /**
     * Get the action of the key
     * @return Action of the key
     */
    public Runnable getAccion() {
        return accion;
    }

    /**
     * Get the cooldown of the key
     * @return Cooldown of the key
     */
    public double getCooldown() {
        return cooldown;
    }

    /**
     * Set the cooldown of the key
     * @param cooldown Cooldown of the key
     */
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}
