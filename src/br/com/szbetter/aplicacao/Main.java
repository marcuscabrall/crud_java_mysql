package br.com.szbetter.aplicacao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import br.com.szbetter.dao.UsuarioDAO;
import br.com.szbetter.dao.DiasRegistroDAO;
import br.com.szbetter.model.Usuario;
import br.com.szbetter.model.DiasRegistro;

public class Main {

    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static DiasRegistroDAO diasRegistroDAO = new DiasRegistroDAO();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        exibirMenu();
    }

    private static void exibirMenu() {
        int opcao = 0;
        do {
            System.out.println("-------------------| MENU SZ BETTER |------------------");
            System.out.println("1. Cadastrar usuário");
            System.out.println("2. Atualizar usuário");
            System.out.println("3. Excluir usuário");
            System.out.println("4. Listar usuários");
            System.out.println("5. Cadastrar registro de sono");
            System.out.println("6. Exibir qualidade de sono");
            System.out.println("7. Atualizar registro de sono");
            System.out.println("8. Excluir registro de sono");
            System.out.println("9. Sair");
            System.out.println("--------------------------------------------------------");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    atualizarUsuario();
                    break;
                case 3:
                    excluirUsuario();
                    break;
                case 4:
                    listarUsuarios();
                    break;
                case 5:
                    cadastrarRegistroSono();
                    break;
                case 6:
                    exibirQualidadeSono();
                    break;
                case 7:
                    atualizarRegistroSono();
                    break;
                case 8:
                    excluirRegistroSono();
                    break;
                case 9:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
            System.out.println();
        } while (opcao != 9);

        scanner.close();
    }

    private static void cadastrarUsuario() {
        System.out.println("----- CADASTRO DE USUÁRIO -----");
        Usuario usuario = new Usuario();

        System.out.print("Digite o nome do usuário: ");
        String nome = scanner.nextLine();
        usuario.setNome(nome);

        System.out.print("Digite a idade do usuário: ");
        int idade = scanner.nextInt();
        usuario.setIdade(idade);

        usuario.setDataCadastro(new Date());

        usuarioDAO.save(usuario);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void atualizarUsuario() {
        System.out.println("----- ATUALIZAÇÃO DE USUÁRIO -----");

        System.out.print("Digite o ID do usuário que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Usuario usuario = usuarioDAO.getUsuarioById(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado com o ID fornecido.");
            return;
        }

        System.out.print("Digite o novo nome do usuário (ou deixe em branco para manter o mesmo): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            usuario.setNome(nome);
        }

        System.out.print("Digite a nova idade do usuário (ou deixe em branco para manter a mesma): ");
        String idadeStr = scanner.nextLine();
        if (!idadeStr.isEmpty()) {
            int idade = Integer.parseInt(idadeStr);
            usuario.setIdade(idade);
        }

        usuario.setDataCadastro(new Date());

        usuarioDAO.update(usuario);
        System.out.println("Usuário atualizado com sucesso!");
    }

    private static void excluirUsuario() {
        System.out.println("----- EXCLUSÃO DE USUÁRIO -----");

        System.out.print("Digite o ID do usuário que deseja excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Usuario usuario = usuarioDAO.getUsuarioById(id);
        if (usuario == null) {
            System.out.println("Usuário não encontrado com o ID fornecido.");
            return;
        }

        List<DiasRegistro> registrosSono = diasRegistroDAO.getRegistrosSonoByUsuario(usuario);
        for (DiasRegistro registroSono : registrosSono) {
            diasRegistroDAO.delete(registroSono);
        }

        usuarioDAO.deleteByID(id);
        System.out.println("Usuário excluído com sucesso!");
    }

    private static void listarUsuarios() {
        System.out.println("----- LISTA DE USUÁRIOS -----");

        List<Usuario> usuarios = usuarioDAO.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println("ID: " + usuario.getId());
                System.out.println("Nome: " + usuario.getNome());
                System.out.println("Idade: " + usuario.getIdade());
                System.out.println("Data de Cadastro: " + usuario.getDataCadastro());
                System.out.println("-------------------------");
            }
        }
    }
    
    private static void atualizarRegistroSono() {
        System.out.println("----- ATUALIZAÇÃO DE REGISTRO DE SONO -----");

        System.out.print("Digite o ID do registro de sono que deseja atualizar: ");
        int idRegistro = scanner.nextInt();
        scanner.nextLine();

        DiasRegistro registroSono = diasRegistroDAO.getById(idRegistro);
        if (registroSono == null) {
            System.out.println("Registro de sono não encontrado com o ID fornecido.");
            return;
        }

        System.out.print("Digite a nova hora de dormir (no formato HH:mm): ");
        String horaDormirStr = scanner.nextLine();
        Date horaDormir = parseTime(horaDormirStr);
        if (horaDormir == null) {
            System.out.println("Hora inválida. Certifique-se de usar o formato HH:mm.");
            return;
        }

        System.out.print("Digite a nova hora de acordar (no formato HH:mm): ");
        String horaAcordarStr = scanner.nextLine();
        Date horaAcordar = parseTime(horaAcordarStr);
        if (horaAcordar == null) {
            System.out.println("Hora inválida. Certifique-se de usar o formato HH:mm.");
            return;
        }

        String qualidadeSono = calcularQualidadeSono(horaDormir, horaAcordar);

        registroSono.setHoraDormir(horaDormir);
        registroSono.setHoraAcordar(horaAcordar);
        registroSono.setQualidadeSono(qualidadeSono);

        diasRegistroDAO.update(registroSono);
        System.out.println("Registro de sono atualizado com sucesso!");
    }

    private static void excluirRegistroSono() {
        System.out.println("----- EXCLUSÃO DE REGISTRO DE SONO -----");

        System.out.print("Digite o ID do registro de sono que deseja excluir: ");
        int idRegistro = scanner.nextInt();
        scanner.nextLine();

        DiasRegistro registroSono = diasRegistroDAO.getById(idRegistro);
        if (registroSono == null) {
            System.out.println("Registro de sono não encontrado com o ID fornecido.");
            return;
        }

        diasRegistroDAO.delete(registroSono);
        System.out.println("Registro de sono excluído com sucesso!");
    }

    private static void cadastrarRegistroSono() {
        System.out.println("----- CADASTRO DE REGISTRO DE SONO -----");

        System.out.print("Digite o ID do usuário: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();

        Usuario usuario = usuarioDAO.getUsuarioById(idUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado com o ID fornecido.");
            return;
        }

        System.out.print("Digite a data do registro (no formato dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        Date dataRegistro = parseDate(dataStr);
        if (dataRegistro == null) {
            System.out.println("Data inválida. Certifique-se de usar o formato dd/MM/yyyy.");
            return;
        }

        System.out.print("Digite a hora de dormir (no formato HH:mm): ");
        String horaDormirStr = scanner.nextLine();
        Date horaDormir = parseTime(horaDormirStr);
        if (horaDormir == null) {
            System.out.println("Hora inválida. Certifique-se de usar o formato HH:mm.");
            return;
        }

        System.out.print("Digite a hora de acordar (no formato HH:mm): ");
        String horaAcordarStr = scanner.nextLine();
        Date horaAcordar = parseTime(horaAcordarStr);
        if (horaAcordar == null) {
            System.out.println("Hora inválida. Certifique-se de usar o formato HH:mm.");
            return;
        }

        String qualidadeSono = calcularQualidadeSono(horaDormir, horaAcordar);

        DiasRegistro registroSono = new DiasRegistro();
        registroSono.setUsuario(usuario);
        registroSono.setDataRegistro(dataRegistro);
        registroSono.setHoraDormir(horaDormir);
        registroSono.setHoraAcordar(horaAcordar);
        registroSono.setQualidadeSono(qualidadeSono);

        diasRegistroDAO.save(registroSono);
        System.out.println("Registro de sono cadastrado com sucesso!");
    }

    private static String calcularQualidadeSono(Date horaDormir, Date horaAcordar) {
        LocalTime dormirTime = LocalDateTime.ofInstant(horaDormir.toInstant(), ZoneId.systemDefault()).toLocalTime();
        LocalTime acordarTime = LocalDateTime.ofInstant(horaAcordar.toInstant(), ZoneId.systemDefault()).toLocalTime();

        if (dormirTime.isBefore(acordarTime)) {
            Duration duracaoSono = Duration.between(dormirTime, acordarTime);
            long duracaoMinutos = duracaoSono.toMinutes();

            if (duracaoMinutos >= 480) {
                return "Bom sono";
            } else if (duracaoMinutos >= 360) {
                return "Sono regular";
            } else {
                return "Sono ruim";
            }
        } else {
            Duration duracaoSono = Duration.between(dormirTime, LocalTime.MAX).plus(Duration.between(LocalTime.MIN, acordarTime));
            long duracaoMinutos = duracaoSono.toMinutes();

            if (duracaoMinutos >= 480) {
                return "Sono bom!";
            } else if (duracaoMinutos >= 360) {
                return "Sono regular!";
            } else {
                return "Sono ruim!";
            }
        }
    }

    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date parseTime(String timeString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            return formatter.parse(timeString);
        } catch (ParseException e) {
            return null;
        }
    }

    private static void exibirQualidadeSono() {
        System.out.println("----- QUALIDADE DO SONO -----");

        System.out.print("Digite o ID do usuário: ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();

        Usuario usuario = usuarioDAO.getUsuarioById(idUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado com o ID fornecido.");
            return;
        }

        List<DiasRegistro> registrosSono = diasRegistroDAO.getRegistrosSonoByUsuario(usuario);
        if (registrosSono.isEmpty()) {
            System.out.println("Não há registros de sono para o usuário.");
        } else {
            System.out.println("Registros de sono do usuário:");
            for (DiasRegistro registroSono : registrosSono) {
                System.out.println("Data do registro: " + registroSono.getDataRegistro());
                System.out.println("Qualidade do sono: " + registroSono.getQualidadeSono());
                System.out.println("-------------------------");
            }
        }
    }
}