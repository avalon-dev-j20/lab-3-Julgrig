import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by  Yulia Grigal on 28.04.2019.
 */
public class Calculator implements ActionListener, KeyListener {
    private JTextField io;
    private ArrayList<JButton> controls;
    private JPanel ui;

    private boolean startNumber = true;
    private String previousOp = "=";

    private final Logic logic;

    private Calculator() {
        logic = new Logic();
        initUI();
    }

    private void initUI() {
        ui = new JPanel(new BorderLayout(2, 2));
        controls = new ArrayList<>();
        //  создаем поле ввода
        JPanel text = new JPanel(new GridLayout(0, 1, 3, 3));
        ui.add(text, BorderLayout.PAGE_START);
        io = new JTextField(15);
        io.setText("0");
        Font font = io.getFont();
        font = font.deriveFont(font.getSize() * 1.6f);
        io.setFont(font);
        io.setHorizontalAlignment(SwingConstants.RIGHT);
        io.setFocusable(false);
        text.add(io);

        // создаем кнопки
        JPanel buttons = new JPanel(new GridLayout(4, 4, 10, 10));
        buttons.setBorder(new EmptyBorder(10, 0, 10, 0));
        ui.add(buttons, BorderLayout.CENTER);

        String[] keyValues = {
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "*",
                "CE", "0", ".", "/"
        };

        for (String keyValue : keyValues) {
            addButton(buttons, keyValue);
        }
        // создаем кнопку "="
        JButton equals = new JButton("=");
        configureButton(equals);
        ui.add(equals, BorderLayout.PAGE_END);

        ui.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JComponent getUI() {
        return ui;
    }

    private void addButton(Container container, String text) {
        JButton button = new JButton(text);
        configureButton(button);
        container.add(button);
    }

    private void configureButton(JButton button) {
        Font font = button.getFont();
        button.setFont(font.deriveFont(font.getSize() * 1.5f));
        button.addActionListener(this);
        button.addKeyListener(this);
        controls.add(button);
    }

    private void actionClear() {
        startNumber = true;
        io.setText("0");
        previousOp = "=";
        logic.setTotal("0");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command.equals("CE")) {
            actionClear();
        } else if (command.equals("=")
                || command.equals("+")
                || command.equals("-")
                || command.equals("*")
                || command.equals("/")) {

            if (startNumber) { // Ошибка: необходимо ввести число, а не оператор
                actionClear();
                io.setText("Ошибка ввода");
            } else {   // иначе все корректно, ошибки нет
                startNumber = true;  // Следующим должно быть введено число
                try {
                    // Пролучить значение с дисплея, конвертировать, выполнить предыдущую операцию
                    // Если это первая операция, то значенеие previousOp имеет значение "="

                    if (previousOp.equals("=")) {
                        logic.setTotal(io.getText());
                    } else if (previousOp.equals("+")) {
                        logic.add(io.getText());
                    } else if (previousOp.equals("-")) {
                        logic.subtract(io.getText());
                    } else if (previousOp.equals("*")) {
                        logic.multiply(io.getText());
                    } else if (previousOp.equals("/")) {
                        logic.divide(io.getText());
                    }

                    io.setText(logic.getTotalString());

                } catch (NumberFormatException ex) {
                    actionClear();
                    io.setText("Error");
                }
                //записываем текущую операцию
                previousOp = ae.getActionCommand();
            }


        } else if (command.equals(".")) {
            if (!io.getText().contains(".")) {
                io.setText(io.getText() + ".");
            }
        } else {
            if (startNumber) {
                //если это первое число, то записываем введенное число (автоматически затирая предыдущие записи)
                io.setText(command);
                startNumber = false;
            } else {
                //если это последующее введенное  число, то дописываем его к первому числу
                io.setText(io.getText() + command);
            }
        }
    }

    private JButton getButton(String text) {
        for (JButton button : controls) {
            String s = button.getText();
            if (text.endsWith(s)
                    || (s.equals("=")
                    && (text.equals("Equals") || text.equals("Enter")))) {

                return button;
            }
        }
        return null;
    }


    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        String keyText = KeyEvent.getKeyText(ke.getKeyCode());
        JButton button = getButton(keyText);
        if (button != null) {
            button.requestFocusInWindow();
            button.doClick();
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Calculator calc = new Calculator();
                JFrame jFrame = new JFrame("Calculator") {
                    @Override
                    public void paint(Graphics graphics) {
                        Dimension d = getSize();
                        Dimension m = getMaximumSize();
                        boolean resize = d.width > m.width || d.height > m.height;
                        d.width = Math.min(m.width, d.width);
                        d.height = Math.min(m.height, d.height);
                        if (resize) {
                            Point point = getLocation();
                            setVisible(false);
                            setSize(d);
                            setLocation(point);
                            setVisible(true);
                        }
                        super.paint(graphics);
                    }
                };
                jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jFrame.setContentPane(calc.getUI());

                jFrame.pack();
                jFrame.setMinimumSize(jFrame.getSize());
                jFrame.setMaximumSize(new Dimension((int) (jFrame.getSize().width * 1.3), (int) (jFrame.getSize().height * 1.3)));

                jFrame.setLocationByPlatform(true);
                jFrame.setVisible(true);
            }
        });
    }

}


