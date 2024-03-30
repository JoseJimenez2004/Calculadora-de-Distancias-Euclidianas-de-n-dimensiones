import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CalculadoraDistanciasEuclidianas extends JFrame implements ActionListener {

    private JTextField dimensionesField;
    private JTextField puntosField;
    private JTextArea resultadoArea;
    private JTextArea distanciaMinimaArea;
    private JTextArea coordenadasArea;

    private double distanciaMinima = Double.MAX_VALUE;
    private int puntoMinimoA = -1;
    private int puntoMinimoB = -1;

    private JPanel panelGrafico;
    private double escala = 1.0;

    public CalculadoraDistanciasEuclidianas() {
        setTitle("Calculadora de Distancias Euclidianas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel panelEntrada = new JPanel(new GridLayout(3, 2, 10, 20));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEntrada.setBackground(new Color(200, 200, 200));

        JLabel dimensionesLabel = new JLabel("Tamaño de las dimensiones:");
        dimensionesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dimensionesField = new JTextField();
        dimensionesField.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel puntosLabel = new JLabel("Número de puntos:");
        puntosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntosField = new JTextField();
        puntosField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton calcularButton = new JButton("Calcular");
        calcularButton.addActionListener(this);
        estilizarBoton(calcularButton);

        JButton graficarButton = new JButton("Graficar");
        graficarButton.addActionListener(this);
        estilizarBoton(graficarButton);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        distanciaMinimaArea = new JTextArea();
        distanciaMinimaArea.setEditable(false);
        distanciaMinimaArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane distanciaScrollPane = new JScrollPane(distanciaMinimaArea);
        distanciaScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Distancia Mínima"));
        distanciaScrollPane.setPreferredSize(new Dimension(250, 150));

        coordenadasArea = new JTextArea();
        coordenadasArea.setEditable(false);
        coordenadasArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane coordenadasScrollPane = new JScrollPane(coordenadasArea);
        coordenadasScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Coordenadas"));
        coordenadasScrollPane.setPreferredSize(new Dimension(250, 150));

        panelEntrada.add(dimensionesLabel);
        panelEntrada.add(dimensionesField);
        panelEntrada.add(puntosLabel);
        panelEntrada.add(puntosField);
        panelEntrada.add(calcularButton);
        panelEntrada.add(graficarButton);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        rightPanel.add(distanciaScrollPane);
        rightPanel.add(coordenadasScrollPane);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelEntrada, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(59, 89, 182));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Calcular")) {
            calcularDistancias();
        } else if (e.getActionCommand().equals("Graficar")) {
            graficarPuntos();
        }
    }

    public void calcularDistancias() {
        try {
            int dimensiones = Integer.parseInt(dimensionesField.getText());
            int puntos = Integer.parseInt(puntosField.getText());
            double[][] coordenadas = generarPuntos(dimensiones, puntos);
            mostrarDistancias(coordenadas);
            mostrarCoordenadas(coordenadas);
            mostrarDistanciaMinima();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos en los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarDistancias(double[][] coordenadas) {
        StringBuilder resultado = new StringBuilder("Distancias Euclidianas:\n");
        for (int i = 0; i < coordenadas.length; i++) {
            for (int j = i + 1; j < coordenadas.length; j++) {
                double distancia = distanciaEuclidiana(coordenadas[i], coordenadas[j]);
                resultado.append("Distancia entre Punto ").append(i + 1).append(" y Punto ").append(j + 1).append(": ").append(distancia).append("\n");
            }
        }
        resultadoArea.setText(resultado.toString());
        resultadoArea.setCaretPosition(0); // Mover el scroll al inicio
    }

    public void mostrarCoordenadas(double[][] coordenadas) {
        StringBuilder coordenadasStr = new StringBuilder("Coordenadas de los puntos:\n");
        for (int i = 0; i < coordenadas.length; i++) {
            coordenadasStr.append("Punto ").append(i + 1).append(": ");
            for (int j = 0; j < coordenadas[i].length; j++) {
                coordenadasStr.append(coordenadas[i][j]);
                if (j < coordenadas[i].length - 1) {
                    coordenadasStr.append(", ");
                }
            }
            coordenadasStr.append("\n");
        }
        coordenadasArea.setText(coordenadasStr.toString());
        coordenadasArea.setCaretPosition(0); // Mover el scroll al inicio
    }

    public void mostrarDistanciaMinima() {
        distanciaMinimaArea.setText("Distancia mínima entre Punto " + puntoMinimoA + " y Punto " + puntoMinimoB + ": " + distanciaMinima);

    }

    public double[][] generarPuntos(int dimensiones, int puntos) {
        double[][] coordenadas = new double[puntos][dimensiones];
        Random rand = new Random();

        for (int i = 0; i < puntos; i++) {
            for (int j = 0; j < dimensiones; j++) {
                coordenadas[i][j] = rand.nextInt(100); // Genera coordenadas enteras entre 0 y 99
            }
        }

        return coordenadas;
    }

    public double distanciaEuclidiana(double[] punto1, double[] punto2) {
        double sumaCuadrados = 0;
        for (int i = 0; i < punto1.length; i++) {
            sumaCuadrados += Math.pow(punto1[i] - punto2[i], 2);
        }
        double distancia = Math.sqrt(sumaCuadrados);

        // Verificar si esta distancia es menor que la mínima actual
        if (distancia < distanciaMinima) {
            distanciaMinima = distancia;
            puntoMinimoA = (int) punto1[0] + 1; // El índice del punto se toma desde 0
            puntoMinimoB = (int) punto2[0] + 1; // Se suma 1 para mostrar el número real de punto
        }

        return distancia;
    }

        public void graficarPuntos() {
        try {
            int dimensiones = Integer.parseInt(dimensionesField.getText());
            int puntos = Integer.parseInt(puntosField.getText());
            double[][] coordenadas = generarPuntos(dimensiones, puntos);

            JFrame frame = new JFrame("Gráfico de Puntos");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            panelGrafico = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;

                    // Dibujar la cuadrícula
                    g2d.setColor(Color.LIGHT_GRAY);
                    int gridSpacing = 50;
                    for (int i = 0; i <= getWidth(); i += gridSpacing) {
                        g2d.drawLine(i, 0, i, getHeight());
                    }
                    for (int j = 0; j <= getHeight(); j += gridSpacing) {
                        g2d.drawLine(0, j, getWidth(), j);
                    }

                    // Dibujar los ejes X e Y
                    g2d.setColor(Color.BLACK);
                    g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2); // Eje X
                    g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight()); // Eje Y

                    // Etiquetas de los puntos
                    g2d.setColor(Color.BLUE);
                    for (int i = 0; i < coordenadas.length; i++) {
                        int x = (int) coordenadas[i][0];
                        int y = (int) coordenadas[i][1];
                        int xReal = getWidth() / 2 + (int) (x * escala);
                        int yReal = getHeight() / 2 - (int) (y * escala);
                        g2d.fillOval(xReal - 3, yReal - 3, 6, 6); // Punto
                        g2d.drawString("P" + (i + 1), xReal + 5, yReal - 5); // Etiqueta del punto
                    }
                }
            };

            // Habilitar scroll para zoom
            JScrollPane scrollPane = new JScrollPane(panelGrafico);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Ajuste de la velocidad de scroll

            frame.add(scrollPane);
            frame.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos en los campos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


  public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraDistanciasEuclidianas());
    }
}

