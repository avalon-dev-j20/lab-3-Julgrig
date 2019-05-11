import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

/**
 * Created by Yulia Grigal on 27.04.2019.
 */
public class ColorPicker implements MouseListener, ChangeListener {

    private JPanel ui;
    private ColorPreview colorPreview;


    public ColorPicker() {
        initUI();
    }

    private void initUI() {
        ui = new JPanel(new GridLayout(1, 2, 10, 10));
        ui.setBorder(new EmptyBorder(10, 10, 10, 10));

        colorPreview = new ColorPreview();
        colorPreview.addMouseListener(this);

        JPanel sliderPanel = new JPanel(new GridLayout(3, 1));

        String[] labelValues = {
                "Red:  ",
                "Green:",
                "Blue: "
        };

        for (String label : labelValues) {
            addSliderWithLabel(sliderPanel, label);
        }

        ui.add(colorPreview, BorderLayout.WEST);
        ui.add(sliderPanel, BorderLayout.EAST);
    }

    private JComponent getUI() {
        return ui;
    }

    private void addSliderWithLabel(Container container, String text) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel(text);
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 125);
        slider.setName(text);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
        position.put(0, new JLabel("0"));
        position.put(255, new JLabel("255"));
        slider.setLabelTable(position);
        slider.addChangeListener(this);
        panel.add(label);
        panel.add(slider);
        container.add(panel);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isBlank(colorPreview.getHexColor())) copyToClipboard(colorPreview.getHexColor());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent component = (JComponent) e.getSource();
        component.setToolTipText(colorPreview.getHexColor());
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        switch(((JSlider) e.getSource()).getName().trim()) {
            case "Red:":
                colorPreview.setRed(((JSlider) e.getSource()).getValue());
                break;
            case "Green:":
                colorPreview.setGreen(((JSlider) e.getSource()).getValue());
                break;
            case "Blue:":
                colorPreview.setBlue(((JSlider) e.getSource()).getValue());
        }
    }

    private class ColorPreview extends JPanel {
        private int r = 125;
        private int g = 125;
        private int b = 125;

        public ColorPreview() throws HeadlessException {
            initialElements();
        }

        private void initialElements() {
            setBackground(new Color(r, g, b));
            setVisible(true);
        }

        public void setBlue(int blue) {
            this.b = blue;
            setBackground(new Color(r, g, b));
        }

        public void setGreen(int green) {
            this.g = green;
            setBackground(new Color(r, g, b));
        }

        public void setRed(int red) {
            this.r = red;
            setBackground(new Color(r, g, b));
        }

        public String getHexColor() {
            String red = pad(Integer.toHexString(r));
            String green = pad(Integer.toHexString(g));
            String blue = pad(Integer.toHexString(b));
            return "#" + red + green + blue;
        }

        private String pad(String s) {
            return (s.length() == 1) ? "0" + s : s;
        }

    }

    private Clipboard clipboard = Toolkit
            .getDefaultToolkit()
            .getSystemClipboard();

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        clipboard.setContents(selection, selection);
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColorPicker colorPicker = new ColorPicker();
                JFrame jFrame = new JFrame("Color Picker");
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setContentPane(colorPicker.getUI());
                jFrame.pack();
                jFrame.setMinimumSize(jFrame.getSize());
                jFrame.setLocationByPlatform(true);
                jFrame.setVisible(true);
            }
        });

    }

}