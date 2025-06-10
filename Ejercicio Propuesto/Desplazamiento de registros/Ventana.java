package proyectomysql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Ventana extends JFrame {

    JLabel LblId, LblNombre, LblDescripcion;
    JTextField TxtId, TxtNombre, TxtDescripcion;
    JButton BtnPrimero, BtnSiguiente, BtnAnterior, BtnUltimo;
    ResultSet resultado;

    public Ventana(String titulo) {
        super(titulo);

        LblId = new JLabel("Id Categoria");
        LblNombre = new JLabel("Nombre");
        LblDescripcion = new JLabel("Descripcion");
        TxtId = new JTextField();
        TxtNombre = new JTextField();
        TxtDescripcion = new JTextField();

        BtnPrimero = new JButton("Primero");
        BtnAnterior = new JButton("Anterior");
        BtnSiguiente = new JButton("Siguiente");
        BtnUltimo = new JButton("Ultimo");

        BtnPrimero.addActionListener(new EventoBoton(this));
        BtnAnterior.addActionListener(new EventoBoton(this));
        BtnSiguiente.addActionListener(new EventoBoton(this));
        BtnUltimo.addActionListener(new EventoBoton(this));

        JPanel Panel11 = new JPanel();
        Panel11.setLayout(new BoxLayout(Panel11, BoxLayout.Y_AXIS));
        Panel11.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel11.add(LblId);
        Panel11.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel11.add(LblNombre);
        Panel11.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel11.add(LblDescripcion);

        JPanel Panel12 = new JPanel();
        Panel12.setLayout(new BoxLayout(Panel12, BoxLayout.Y_AXIS));
        Panel12.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel12.add(TxtId);
        Panel12.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel12.add(TxtNombre);
        Panel12.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel12.add(TxtDescripcion);

        JPanel Panel1 = new JPanel();
        Panel1.setLayout(new BoxLayout(Panel1, BoxLayout.X_AXIS));
        Panel1.add(Box.createRigidArea(new Dimension(5, 10)));
        Panel1.add(Panel11);
        Panel1.add(Box.createRigidArea(new Dimension(5, 10)));
        Panel1.add(Panel12);

        JPanel Panel2 = new JPanel();
        Panel2.setLayout(new BoxLayout(Panel2, BoxLayout.X_AXIS));
        Panel2.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel2.add(BtnPrimero);
        Panel2.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel2.add(BtnAnterior);
        Panel2.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel2.add(BtnSiguiente);
        Panel2.add(Box.createRigidArea(new Dimension(10, 10)));
        Panel2.add(BtnUltimo);
        Panel2.setBackground(Color.orange);

        JPanel Panel = new JPanel();
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
        Panel.add(Panel1);
        Panel.add(Box.createRigidArea(new Dimension(0, 20)));
        Panel.add(Panel2);
        Panel.setBackground(new Color(255, 0, 0));

        getContentPane().add(Panel, BorderLayout.CENTER);
        addWindowListener(new EventosVentana(this));

        Conexion();
    }

    private void Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/EmpresaMSQL?serverTimezone=UTC&useSSL=false";
            Connection conexion = DriverManager.getConnection(url, "root", "user1234");

            Statement sentencia = conexion.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );

            boolean tieneResultados = sentencia.execute("SELECT * FROM Categorias");

            if (tieneResultados) {
                resultado = sentencia.getResultSet();
                if (resultado != null) {
                    IrPrimero(); // Muestra primer registro al abrir
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Controlador no encontrado: " + e);
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e);
        }
    }

    public void IrPrimero() {
        try {
            resultado.first();
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (Exception e) {
            System.out.println("Error al ir al primer registro: " + e);
        }
    }

    public void IrAnterior() {
        try {
            if (!resultado.previous()) {
                resultado.first();
            }
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (Exception e) {
            System.out.println("Error al ir al registro anterior: " + e);
        }
    }

    public void IrSiguiente() {
        try {
            if (!resultado.next()) {
                resultado.last();
            }
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (Exception e) {
            System.out.println("Error al ir al siguiente registro: " + e);
        }
    }

    public void IrUltimo() {
        try {
            resultado.last();
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (Exception e) {
            System.out.println("Error al ir al último registro: " + e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana miVentana = new Ventana("Desplazamiento de Registros");
            miVentana.pack();
            miVentana.setLocationRelativeTo(null);
            miVentana.setVisible(true);
        });
    }
}

class EventoBoton implements ActionListener {

    Ventana fuente;

    public EventoBoton(Ventana pWnd) {
        fuente = pWnd;
    }

    public void actionPerformed(ActionEvent evento) {
        switch (evento.getActionCommand()) {
            case "Primero":
                fuente.IrPrimero();
                break;
            case "Anterior":
                fuente.IrAnterior();
                break;
            case "Siguiente":
                fuente.IrSiguiente();
                break;
            case "Ultimo":
                fuente.IrUltimo();
                break;
        }
    }
}

class EventosVentana extends WindowAdapter {

    private Ventana fuente;

    public EventosVentana(Ventana pWnd) {
        this.fuente = pWnd;
    }

    public void windowClosing(WindowEvent evento) {
        fuente.dispose();
    }
}
