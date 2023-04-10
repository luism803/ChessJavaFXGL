package structs;

public class KeyInfo {
    private String name;
    private Runnable accion;
    private double cooldown;

    public KeyInfo(String name, Runnable accion) {
        this.name = name;
        this.accion = accion;
        cooldown = 0;
    }

    public String getName() {
        return name;
    }

    public Runnable getAccion() {
        return accion;
    }

    public double getCooldown() {
        return cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}
