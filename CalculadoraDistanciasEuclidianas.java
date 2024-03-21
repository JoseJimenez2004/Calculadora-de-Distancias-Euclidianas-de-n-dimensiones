import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CalculadoraDistanciasEuclidianas extends JFrame {

    private JPanel panelPuntos;
    private JPanel panelDistancias;
    private JTextField dimensionesField;
    private JTextField puntosField;
    private JPanel mainPanel;
    private double distanciaMinima = Double.MAX_VALUE;
    private int puntoMinimoA = -1;
    private int puntoMinimoB = -1;

    public CalculadoraDistanciasEuclidianas() {
        setTitle("Calculadora de Distancias Euclidianas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 300);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240)); // Color de fondo

        JPanel panelEntrada = new JPanel(new GridLayout(2, 1, 10, 20));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelEntrada.setBackground(new Color(200, 200, 200)); // Color de fondo

        JPanel dimensionesPanel = new JPanel(new GridLayout(2, 1));
        JLabel dimensionesLabel = new JLabel("Ingresa el tamaño de las dimensiones:");
        dimensionesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dimensionesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dimensionesLabel.setForeground(Color.WHITE); // Color del texto
        dimensionesField = new JTextField();
        dimensionesField.setFont(new Font("Arial", Font.PLAIN, 16));
        dimensionesField.setHorizontalAlignment(SwingConstants.CENTER);
        dimensionesPanel.add(dimensionesLabel);
        dimensionesPanel.add(dimensionesField);
        dimensionesPanel.setBackground(new Color(59, 89, 182)); // Color de fondo

        JPanel puntosPanel = new JPanel(new GridLayout(2, 1));
        JLabel puntosLabel = new JLabel("Número de puntos:");
        puntosLabel.setFont(new Font("Arial", Font.BOLD, 16));
        puntosLabel.setHorizontalAlignment(SwingConstants.CENTER);
        puntosLabel.setForeground(Color.WHITE); // Color del texto
        puntosField = new JTextField();
        puntosField.setFont(new Font("Arial", Font.PLAIN, 16));
        puntosField.setHorizontalAlignment(SwingConstants.CENTER);
        puntosPanel.add(puntosLabel);
        puntosPanel.add(puntosField);
        puntosPanel.setBackground(new Color(59, 89, 182)); // Color de fondo

        panelEntrada.add(dimensionesPanel);
        panelEntrada.add(puntosPanel);

        add(panelEntrada, BorderLayout.NORTH);

        JButton calcularButton = new JButton("Calcular");
        calcularButton.setBackground(new Color(59, 89, 182));
        calcularButton.setForeground(Color.WHITE);
        calcularButton.setFocusPainted(false);
        calcularButton.setFont(new Font("Arial", Font.BOLD, 16));
        calcularButton.addActionListener(e -> {
            int dimensiones = Integer.parseInt(dimensionesField.getText());
            int puntos = Integer.parseInt(puntosField.getText());
            generarInterfaz(dimensiones, puntos);
        });

        JButton graficarButton = new JButton("Graficar"); // Nuevo botón
        graficarButton.setBackground(new Color(59, 89, 182));
        graficarButton.setForeground(Color.WHITE);
        graficarButton.setFocusPainted(false);
        graficarButton.setFont(new Font("Arial", Font.BOLD, 16));
        graficarButton.addActionListener(e -> {
            int dimensiones = Integer.parseInt(dimensionesField.getText());
            int puntos = Integer.parseInt(puntosField.getText());
            double[][] coordenadas = generarPuntos(dimensiones, puntos);
            graficarPuntos(coordenadas);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(calcularButton);
        buttonPanel.add(graficarButton); // Agregar el nuevo botón
        buttonPanel.setBackground(new Color(200, 200, 200)); // Color de fondo
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void generarInterfaz(int dimensiones, int puntos) {
        getContentPane().removeAll();
        revalidate();
        repaint();

        panelPuntos = new JPanel();
        panelPuntos.setLayout(new BoxLayout(panelPuntos, BoxLayout.Y_AXIS));

        panelDistancias = new JPanel();
        panelDistancias.setLayout(new BoxLayout(panelDistancias, BoxLayout.Y_AXIS));

        double[][] coordenadas = generarPuntos(dimensiones, puntos);

        mostrarPuntos(coordenadas);
        mostrarDistancias(coordenadas);

        JScrollPane scrollPanePuntos = new JScrollPane(panelPuntos);
        JScrollPane scrollPaneDistancias = new JScrollPane(panelDistancias);

        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(scrollPanePuntos);
        mainPanel.add(scrollPaneDistancias);

        add(mainPanel);
        pack();
    }

    public void mostrarPuntos(double[][] coordenadas) {
        panelPuntos.add(new JLabel("Coordenadas de los puntos:"));
        for (int i = 0; i < coordenadas.length; i++) {
            StringBuilder puntoStr = new StringBuilder("Punto " + (i + 1) + ": ");
            for (int j = 0; j < coordenadas[0].length; j++) {
                puntoStr.append("{x: ").append(coordenadas[i][j]).append(", y: ").append(coordenadas[i][j]).append("} ");
            }
            JLabel puntoLabel = new JLabel(puntoStr.toString());
            puntoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panelPuntos.add(puntoLabel);
        }
    }
    
  public void mostrarDistancias(double[][] coordenadas) {
    panelDistancias.add(new JLabel("Distancias Euclidianas:"));
    for (int i = 0; i < coordenadas.length; i++) {
        for (int j = i + 1; j < coordenadas.length; j++) {
            double distancia = distanciaEuclidiana(coordenadas[i], coordenadas[j]);
            JLabel distanciaLabel = new JLabel("Distancia entre Punto " + (i + 1) + " y Punto " + (j + 1) + ": " + distancia);
            distanciaLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panelDistancias.add(distanciaLabel);

            // Verificar si esta distancia es menor que la mínima actual
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                puntoMinimoA = i + 1;
                puntoMinimoB = j + 1;
            }
        }
    }

    JLabel distanciaMinimaLabel = new JLabel("Distancia mínima entre Punto " + puntoMinimoA + " y Punto " + puntoMinimoB + ": " + distanciaMinima);
    distanciaMinimaLabel.setFont(new Font("Arial", Font.BOLD, 16));
    panelDistancias.add(distanciaMinimaLabel);
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
        return Math.sqrt(sumaCuadrados);
    }

    public void graficarPuntos(double[][] coordenadas) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Dibujar eje X
                g2d.drawLine(50, getHeight() / 2, getWidth() - 50, getHeight() / 2);
                g2d.drawString("X", getWidth() - 20, getHeight() / 2 - 10);

                // Dibujar eje Y
                g2d.drawLine(getWidth() / 2, 50, getWidth() / 2, getHeight() - 50);
                g2d.drawString("Y", getWidth() / 2 + 10, 20);

                // Dibujar los puntos
                for (double[] punto : coordenadas) {
                    int x = (int) punto[0];
                    int y = (int) punto[1];
                    // Ajustar los puntos al plano
                    int xReal = getWidth() / 2 + x;
                    int yReal = getHeight() / 2 - y;
                    g2d.setColor(new Color(255, 0, 0)); // Color de los puntos
                    g2d.fillOval(xReal - 3, yReal - 3, 6, 6); // Dibujar un círculo para cada punto
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculadoraDistanciasEuclidianas::new);
    }
}
