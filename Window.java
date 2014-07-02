import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
  *
 * @frameworkauthor gametutorial.net
 */

public class Window extends JFrame {
    
    public static final int UNIT_LEN = 32;
    public static final int GAME_WIDTH_IN_UNITS = 32;
    public static final int GAME_HEIGHT_IN_UNITS = 24;

    private Window() {
        // Sets the title for this frame.
        this.setTitle("Attrition");
        
        // Sets size of the frame.
        if (false) { 
            // Full screen mode
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        } else { // Window mode
            // Size of the frame.
            this.setSize(GAME_WIDTH_IN_UNITS * UNIT_LEN, GAME_HEIGHT_IN_UNITS * UNIT_LEN);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }
        
        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Creates the instance of the Framework.java that extends the
        // Canvas.java and puts it on the frame.
        this.setContentPane(new Framework());
        this.setVisible(true);
    }

    public static void main(String[] args) {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
