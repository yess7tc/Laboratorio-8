package proyectomysql;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Ventana extends JFrame {
    JLabel LblId, LblNombre, LblDescripcion;
    JTextField TxtId, TxtNombre, TxtDescripcion;
    JButton BtnPrimero, BtnSiguiente, BtnAnterior, BtnUltimo;
    JButton BtnInsertar, BtnModificar, BtnEliminar, BtnActualizar;

    ResultSet resultado;
    Statement sentencia;
    Connection conexion;

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

        BtnInsertar = new JButton("Insertar");
        BtnModificar = new JButton("Modificar");
        BtnEliminar = new JButton("Eliminar");
        BtnActualizar = new JButton("Actualizar");

        // Listeners
        EventoBoton handler = new EventoBoton(this);
        BtnPrimero.addActionListener(handler);
        BtnAnterior.addActionListener(handler);
        BtnSiguiente.addActionListener(handler);
        BtnUltimo.addActionListener(handler);
        BtnInsertar.addActionListener(handler);
        BtnModificar.addActionListener(handler);
        BtnEliminar.addActionListener(handler);
        BtnActualizar.addActionListener(handler);

        JPanel Panel1 = new JPanel();
        JPanel Panel11 = new JPanel();
        JPanel Panel12 = new JPanel();
        JPanel Panel2 = new JPanel();
        JPanel Panel3 = new JPanel();
        JPanel Panel = new JPanel();

        Panel11.setLayout(new BoxLayout(Panel11, BoxLayout.Y_AXIS));
        Panel12.setLayout(new BoxLayout(Panel12, BoxLayout.Y_AXIS));
        Panel1.setLayout(new BoxLayout(Panel1, BoxLayout.X_AXIS));
        Panel2.setLayout(new BoxLayout(Panel2, BoxLayout.X_AXIS));
        Panel3.setLayout(new BoxLayout(Panel3, BoxLayout.X_AXIS));
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));

        // Etiquetas y campos
        Panel11.add(LblId);
        Panel11.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel11.add(LblNombre);
        Panel11.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel11.add(LblDescripcion);

        Panel12.add(TxtId);
        Panel12.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel12.add(TxtNombre);
        Panel12.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel12.add(TxtDescripcion);

        Panel1.add(Box.createRigidArea(new Dimension(5, 10)));
        Panel1.add(Panel11);
        Panel1.add(Box.createRigidArea(new Dimension(5, 10)));
        Panel1.add(Panel12);

        Panel2.add(BtnInsertar);
        Panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel2.add(BtnModificar);
        Panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel2.add(BtnActualizar);
        Panel2.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel2.add(BtnEliminar);
        Panel2.setBackground(new Color(0, 0, 255));

        Panel3.add(BtnPrimero);
        Panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel3.add(BtnAnterior);
        Panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel3.add(BtnSiguiente);
        Panel3.add(Box.createRigidArea(new Dimension(5, 0)));
        Panel3.add(BtnUltimo);
        Panel3.setBackground(Color.orange);

        Panel.add(Panel1);
        Panel.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel.add(Panel2);
        Panel.add(Box.createRigidArea(new Dimension(0, 10)));
        Panel.add(Panel3);
        Panel.setBackground(new Color(255, 0, 0));

        getContentPane().add(Panel);
        addWindowListener(new EventosVentana(this));

        Conexion();
    }

    private void Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/EmpresaMSQL?serverTimezone=UTC&useSSL=false",
                "root",
                "user1234"
            );

            sentencia = conexion.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
            resultado = sentencia.executeQuery("SELECT * FROM Categorias");

            if (resultado.next()) {
                mostrarRegistro();
            }
        } catch (Exception e) {
            System.out.println("Error de Conexión: " + e);
        }
    }

    private void mostrarRegistro() throws SQLException {
        TxtId.setText(resultado.getString("IdCategoria"));
        TxtNombre.setText(resultado.getString("Nombre"));
        TxtDescripcion.setText(resultado.getString("Descripcion"));
    }

    public void IrPrimero() {
        try {
            resultado.first();
            mostrarRegistro();
        } catch (Exception e) {
            System.out.println("Error al ir al primero: " + e);
        }
    }

    public void IrAnterior() {
        try {
            if (!resultado.previous()) resultado.first();
            mostrarRegistro();
        } catch (Exception e) {
            System.out.println("Error al ir al anterior: " + e);
        }
    }

    public void IrSiguiente() {
        try {
            if (!resultado.next()) resultado.last();
            mostrarRegistro();
        } catch (Exception e) {
            System.out.println("Error al ir al siguiente: " + e);
        }
    }

    public void IrUltimo() {
        try {
            resultado.last();
            mostrarRegistro();
        } catch (Exception e) {
            System.out.println("Error al ir al último: " + e);
        }
    }

    public void IrInsertar() {
        try {
            String nombre = TxtNombre.getText();
            String descripcion = TxtDescripcion.getText();
            sentencia.executeUpdate(
                "INSERT INTO Categorias (Nombre, Descripcion) VALUES ('" + nombre + "', '" + descripcion + "')"
            );
            IrActualizar();
        } catch (Exception e) {
            System.out.println("Error al insertar: " + e);
        }
    }

    public void IrEliminar() {
        try {
            String id = TxtId.getText();
            sentencia.executeUpdate("DELETE FROM Categorias WHERE IdCategoria = " + id);
            IrActualizar();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e);
        }
    }

    public void IrModificar() {
        try {
            String id = TxtId.getText();
            String nombre = TxtNombre.getText();
            String descripcion = TxtDescripcion.getText();

            sentencia.executeUpdate(
                "UPDATE Categorias SET Nombre = '" + nombre + "', Descripcion = '" + descripcion + "' WHERE IdCategoria = " + id
            );
            IrActualizar();
        } catch (Exception e) {
            System.out.println("Error al modificar: " + e);
        }
    }

    public void IrActualizar() {
        try {
            resultado = sentencia.executeQuery("SELECT * FROM Categorias");
            if (resultado.next()) {
                mostrarRegistro();
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana v = new Ventana("CRUD Categorias");
            v.pack();
            v.setLocationRelativeTo(null);
            v.setVisible(true);
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
            case "Primero": fuente.IrPrimero(); break;
            case "Anterior": fuente.IrAnterior(); break;
            case "Siguiente": fuente.IrSiguiente(); break;
            case "Ultimo": fuente.IrUltimo(); break;
            case "Insertar": fuente.IrInsertar(); break;
            case "Modificar": fuente.IrModificar(); break;
            case "Eliminar": fuente.IrEliminar(); break;
            case "Actualizar": fuente.IrActualizar(); break;
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
        System.exit(0);
    }
}
