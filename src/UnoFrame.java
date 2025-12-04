import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

/**
 * The main game window. It draws the cards, buttons, and menus.
 * Updated for M5 to include Background Images and Button Icons.
 *
 * @author Danilo Bukvic Ajan Balaganesh Aydan Eng Aws Ali
 * @version 5.1
 */
public class UnoFrame extends JFrame implements UnoView {
    private final JLabel labelTopCard = new JLabel("Top: -", SwingConstants.CENTER);
    private final JLabel labelPlayer = new JLabel("Player: -", SwingConstants.CENTER);
    private final JLabel labelInfo = new JLabel(" ", SwingConstants.CENTER);
    private final JPanel handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
    private final JButton buttonDraw = new JButton("Draw");
    private final JButton buttonNext = new JButton("Next Player");
    private final BackgroundPanel mainPanel = new BackgroundPanel();
    // Menu items for the top bar
    private final JMenuItem menuUndo = new JMenuItem("Undo");
    private final JMenuItem menuRedo = new JMenuItem("Redo");

    private UnoController controller;
    private boolean isDark = false;

    // Images
    private BufferedImage bgLight;
    private BufferedImage bgDark;
    private Icon iconDraw;
    private Icon iconNext;

    /**
     * Builds the GUI, sets up the menu bar, and asks for player names.
     *
     * @param title The text in the window title bar.
     */
    public UnoFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // Made window slightly larger to accommodate bigger buttons

        // -- LOAD IMAGES (Feature 1) --
        loadImages();

        // -- Setup Menu Bar --
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem itemSave = new JMenuItem("Save Game");
        JMenuItem itemLoad = new JMenuItem("Load Game");
        itemSave.setActionCommand("SAVE");
        itemLoad.setActionCommand("LOAD");
        fileMenu.add(itemSave);
        fileMenu.add(itemLoad);

        JMenu gameMenu = new JMenu("Game");
        menuUndo.setActionCommand("UNDO");
        menuRedo.setActionCommand("REDO");
        gameMenu.add(menuUndo);
        gameMenu.add(menuRedo);

        menuBar.add(fileMenu);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        // -- Setup Players --
        int num = 2;
        List<String> names = new ArrayList<>();
        List<Boolean> isAI = new ArrayList<>();
        for(int i=1; i<=num; i++) { names.add("Player "+i); isAI.add(false); }

        UnoModel model = new UnoModel(num, names, isAI);
        controller = new UnoController(model, this);

        // -- Attach Listeners --
        itemSave.addActionListener(controller);
        itemLoad.addActionListener(controller);
        menuUndo.addActionListener(controller);
        menuRedo.addActionListener(controller);
        buttonDraw.setActionCommand("DRAW");
        buttonDraw.addActionListener(controller);
        buttonNext.setActionCommand("NEXT");
        buttonNext.addActionListener(controller);

        // -- Setup Bigger Buttons & Icons --
        buttonDraw.setFont(new Font("Arial", Font.BOLD, 16));
        buttonNext.setFont(new Font("Arial", Font.BOLD, 16));

        // Add padding to make buttons physically bigger
        buttonDraw.setMargin(new Insets(10, 20, 10, 20));
        buttonNext.setMargin(new Insets(10, 20, 10, 20));

        if (iconDraw != null) buttonDraw.setIcon(iconDraw);
        if (iconNext != null) buttonNext.setIcon(iconNext);

        // -- Layout Components --
        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());

        JPanel north = new JPanel(new GridLayout(2, 1));
        north.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 3));
        stats.setOpaque(false);

        // Make stats text larger and bold
        Font statsFont = new Font("Arial", Font.BOLD, 18);
        labelTopCard.setFont(statsFont);
        labelPlayer.setFont(statsFont);
        labelInfo.setFont(statsFont);

        stats.add(labelTopCard); stats.add(labelPlayer); stats.add(labelInfo);

        north.add(stats);

        JPanel south = new JPanel();
        south.setOpaque(false);
        south.add(buttonDraw); south.add(buttonNext);

        handPanel.setOpaque(false);

        mainPanel.add(north, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(handPanel) {
            {
                setOpaque(false);
                getViewport().setOpaque(false);
            }
        }, BorderLayout.CENTER);
        mainPanel.add(south, BorderLayout.SOUTH);

        model.addView(this);
        setVisible(true);
    }

    /**
     * Helper to load images from disk.
     */
    private void loadImages() {
        try {
            bgLight = ImageIO.read(new File("images/bg_light.jpg"));
            bgDark  = ImageIO.read(new File("images/bg_dark.jpg"));

            BufferedImage imgDraw = ImageIO.read(new File("images/draw_btn.png"));
            BufferedImage imgNext = ImageIO.read(new File("images/next_btn.png"));

            // --- FIX: Increased Scale to 50x50 ---
            iconDraw = new ImageIcon(imgDraw.getScaledInstance(70, 70, Image.SCALE_SMOOTH));
            iconNext = new ImageIcon(imgNext.getScaledInstance(70, 70, Image.SCALE_SMOOTH));

        } catch (IOException e) {
            System.out.println("Could not load images. Ensure 'images' folder exists with files.");
            e.printStackTrace();
        }
    }

    /**
     * Called when the model changes. We repaint the whole hand and update text.
     */
    @Override
    public void handleUpdate(UnoEvent e) {
        this.isDark = e.isDark();

        mainPanel.setIsDark(isDark);
        mainPanel.repaint();

        // --- FIX: Dynamic Text Color ---
        Color textColor = isDark ? Color.WHITE : Color.BLACK;
        labelTopCard.setForeground(textColor);
        labelPlayer.setForeground(textColor);
        labelInfo.setForeground(textColor);

        labelTopCard.setText("Top: " + e.getTopCardText());
        labelPlayer.setText("Turn: " + e.getCurrentPlayerName());
        labelInfo.setText(e.getInfo());

        // Redraw hand buttons
        handPanel.removeAll();
        for (int i = 0; i < e.getHand().size(); i++) {
            UnoCard c = e.getHand().get(i);
            JButton b = new JButton(c.toText(isDark));
            b.setPreferredSize(new Dimension(100, 50)); // Make card buttons a bit bigger too
            b.setBackground(mapCardColor(c.getColor(isDark)));
            b.setForeground(isDark ? Color.WHITE : Color.BLACK);
            b.setActionCommand("PLAY:" + i);
            b.addActionListener(controller);
            b.setEnabled(!e.isMustPressNext() && !e.isAIPlayer());
            handPanel.add(b);
        }

        if (e.isAIPlayer()) {
            buttonDraw.setEnabled(false);
            buttonNext.setText(e.isMustPressNext() ? "Next Player" : "Run AI Turn");
            buttonNext.setEnabled(true);
        } else {
            buttonDraw.setEnabled(!e.isMustPressNext());
            buttonNext.setText("Next Player");
            buttonNext.setEnabled(e.isMustPressNext());
        }

        handPanel.revalidate();
        handPanel.repaint();
    }

    @Override
    public void handleRoundEnd(String message) {
        JOptionPane.showMessageDialog(this, message, "Round Over", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void handleEnd(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public UnoColor promptForWildColor() {
        UnoColor[] opts = isDark ?
                new UnoColor[]{UnoColor.TEAL, UnoColor.PINK, UnoColor.PURPLE, UnoColor.ORANGE} :
                new UnoColor[]{UnoColor.RED, UnoColor.GREEN, UnoColor.BLUE, UnoColor.YELLOW};
        int n = JOptionPane.showOptionDialog(this, "Choose Color:", "Wild", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
        return (n >= 0) ? opts[n] : opts[0];
    }

    @Override
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public Color mapCardColor(UnoColor c) {
        return switch(c) {
            case RED -> new Color(255, 80, 80);
            case GREEN -> new Color(80, 200, 80);
            case BLUE -> new Color(80, 80, 255);
            case YELLOW -> new Color(255, 220, 0);
            case TEAL -> new Color(0, 128, 128);
            case PINK -> new Color(255, 105, 180);
            case PURPLE -> new Color(128, 0, 128);
            case ORANGE -> new Color(255, 165, 0);
            default -> Color.GRAY;
        };
    }
    /**
     * Inner class representing a panel with a custom background image. -> BONUS FEATURE
     * It overrides paintComponent to draw the appropriate image be it light or dark
     * filling the entire frame amd images are not loaded it falls back to a solid color.
     */
    private class BackgroundPanel extends JPanel {
        private boolean isDarkPanel = false;

        public void setIsDark(boolean b) {
            this.isDarkPanel = b;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage img = isDarkPanel ? bgDark : bgLight;
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(isDarkPanel ? new Color(50, 0, 50) : new Color(240, 240, 240));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}