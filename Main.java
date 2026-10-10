import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// ==========================================
// CLASE 1: CajeroAutomatico (Encapsulamiento y Lógica)
// ==========================================
class CajeroAutomatico {
    // Atributos de la clase
    private double saldo;
    private String clavePersonal;
    private String tipoCuenta;
    private int contadorOperaciones;

    // Constructor de la clase
    public CajeroAutomatico(double saldoInicial, String claveInicial, String tipoCuenta) {
        this.saldo = saldoInicial;
        this.clavePersonal = claveInicial;
        this.tipoCuenta = tipoCuenta;
        this.contadorOperaciones = 1000;
    }

    // Validar clave personal
    public boolean validarClave(String claveIngresada) {
        return this.clavePersonal.equals(claveIngresada);
    }

    // Consulta de saldo
    public double consultarSaldo() {
        return this.saldo;
    }

    // Retiro de efectivo con comprobante
    public boolean retirarEfectivo(double monto) {
        if (monto <= 0) {
            System.out.println("[-] Error: El monto a retirar debe ser mayor a cero.");
            return false;
        }

        if (monto > this.saldo) {
            System.out.println("[-] Error: Saldo insuficiente para realizar la operación.");
            return false;
        }

        this.saldo -= monto;
        this.contadorOperaciones++;
        imprimirComprobante(monto);
        return true;
    }

    // Cambio de clave
    public boolean cambiarClave(String claveActual, String nuevaClave) {
        if (!validarClave(claveActual)) {
            System.out.println("[-] Error: La clave actual ingresada es incorrecta.");
            return false;
        }

        if (nuevaClave == null || nuevaClave.trim().isEmpty() || nuevaClave.length() < 4) {
            System.out.println("[-] Error: La nueva clave debe tener al menos 4 caracteres.");
            return false;
        }

        this.clavePersonal = nuevaClave;
        System.out.println("[+] ¡Cambio de clave realizado con éxito!");
        return true;
    }

    // Impresión de comprobante tras retiro
    private void imprimirComprobante(double montoRetirado) {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println("\n========================================");
        System.out.println("        COMPROBANTE DE RETIRO           ");
        System.out.println("========================================");
        System.out.println("Tipo de Cuenta    : " + this.tipoCuenta);
        System.out.println("Monto Retirado    : $" + String.format("%.2f", montoRetirado));
        System.out.println("Saldo Restante    : $" + String.format("%.2f", this.saldo));
        System.out.println("Código Operación  : OP-" + this.contadorOperaciones);
        System.out.println("Fecha de Emisión  : " + ahora.format(formatoFecha));
        System.out.println("========================================");
    }

    // Getters y Setters
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
}

// ==========================================
// CLASE PRINCIPAL: Main (Interfaz por Consola)
// ==========================================
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Instancia del objeto cajero mediante su constructor
        CajeroAutomatico cajero = new CajeroAutomatico(1500.00, "1234", "Cuenta Ahorros");

        System.out.println("=== BIENVENIDO AL SISTEMA DE CAJERO AUTOMÁTICO ===");
        
        // 1. Seleccionar Tipo de Cuenta
        System.out.println("Seleccione el Tipo de Cuenta:");
        System.out.println("1. Ahorros");
        System.out.println("2. Corriente");
        System.out.print("Opción: ");
        int opcionCuenta = scanner.nextInt();
        scanner.nextLine();

        if (opcionCuenta == 1) {
            cajero.setTipoCuenta("Cuenta Ahorros");
        } else if (opcionCuenta == 2) {
            cajero.setTipoCuenta("Cuenta Corriente");
        } else {
            System.out.println("Opción inválida. Se asignó 'Cuenta Ahorros' por defecto.");
            cajero.setTipoCuenta("Cuenta Ahorros");
        }

        // 2. Ingreso y Validación de Clave Personal
        System.out.print("\nIngrese su Clave Personal de 4 dígitos: ");
        String claveIngresada = scanner.nextLine();

        if (!cajero.validarClave(claveIngresada)) {
            System.out.println("[-] Acceso Denegado: Clave incorrecta. Finalizando sesión.");
            scanner.close();
            return;
        }

        System.out.println("[+] Autenticación exitosa. Tipo de cuenta activa: " + cajero.getTipoCuenta());

        // 3. Menú de Operaciones
        int opcion = 0;
        do {
            System.out.println("\n--- MENÚ DE OPERACIONES ---");
            System.out.println("1. Consulta de Saldo");
            System.out.println("2. Retiro de Efectivo");
            System.out.println("3. Cambio de Clave");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("[-] Entrada no válida. Por favor ingrese un número.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.println("\n[+] Saldo Actual (" + cajero.getTipoCuenta() + "): $" 
                            + String.format("%.2f", cajero.consultarSaldo()));
                    break;

                case 2:
                    System.out.print("\nIngrese el monto a retirar: $");
                    if (scanner.hasNextDouble()) {
                        double monto = scanner.nextDouble();
                        scanner.nextLine();
                        cajero.retirarEfectivo(monto);
                    } else {
                        System.out.println("[-] Error: Ingrese un monto numérico válido.");
                        scanner.nextLine();
                    }
                    break;

                case 3:
                    System.out.print("\nIngrese su clave actual: ");
                    String claveActual = scanner.nextLine();
                    System.out.print("Ingrese la nueva clave: ");
                    String nuevaClave = scanner.nextLine();
                    cajero.cambiarClave(claveActual, nuevaClave);
                    break;

                case 4:
                    System.out.println("\nGracias por utilizar nuestros servicios. ¡Hasta luego!");
                    break;

                default:
                    System.out.println("[-] Opción fuera de rango. Seleccione una opción entre 1 y 4.");
            }

        } while (opcion != 4);

        scanner.close();
    }
}