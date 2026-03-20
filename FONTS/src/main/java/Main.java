import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CtrlPresentacio ctrl = new CtrlPresentacio(true);
            ctrl.inicialitzar();
        });
    }
}